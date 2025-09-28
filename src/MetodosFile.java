//clase con métodos que usan clases file para manejar archivos de texto.
package src;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Contiene todos los métodos para la gestión de contactos utilizando
 * las clases base de Java I/O (File, FileReader, FileWriter).
 * Estas operaciones son directas y no utilizan un búfer de memoria intermedio.
 */
public class MetodosFile {

    /**
     * Agrega un nuevo contacto al final del archivo especificado.
     * Utiliza FileWriter en modo 'append' para no sobrescribir los datos existentes.
     * @param nombre El nombre del contacto a agregar.
     * @param telefono El teléfono del contacto a agregar.
     * @param nombreArchivo El nombre del archivo donde se guardará el contacto.
     */
    public static void agregarContacto(String nombre, String telefono, String nombreArchivo) {
        // 'try-with-resources' para asegurar que FileWriter se cierre automáticamente.
        // El 'true' en FileWriter indica que se abrirá en modo 'append' (añadir al final).
        try (FileWriter escritor = new FileWriter(nombreArchivo, true)) {
            // Valida que los datos no estén vacíos antes de escribir.
            if (nombre.trim().isEmpty() || telefono.trim().isEmpty()) {
                System.out.println("El nombre y el teléfono no pueden estar vacíos. No se agregó el contacto.");
                return;
            }
            // Escribe el nuevo contacto en el archivo, seguido de un salto de línea del sistema.
            escritor.write(nombre.trim() + "," + telefono.trim() + System.lineSeparator());
            System.out.println("Contacto agregado exitosamente en " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Ocurrió un error al agregar el contacto en " + nombreArchivo);
        }
    }

    /**
     * Lee y muestra en consola todos los contactos del archivo.
     * Utiliza FileReader para leer el archivo carácter por carácter.
     * @param nombreArchivo El nombre del archivo a leer.
     */
    public static void imprimirContacto(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        // Verifica si el archivo existe antes de intentar leerlo.
        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("No hay contactos registrados en " + nombreArchivo);
            return;
        }
        // 'try-with-resources' cierra automáticamente el FileReader al finalizar.
        try (FileReader lector = new FileReader(archivo)) {
            int caracter;
            // Lee el archivo carácter por carácter hasta que el método read() devuelve -1 (fin del archivo).
            while ((caracter = lector.read()) != -1) {
                // Convierte el valor entero del carácter a su representación char y lo imprime.
                System.out.print((char) caracter);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de contactos: " + nombreArchivo);
        }
    }

    /**
     * Busca contactos cuyo nombre contenga el texto proporcionado.
     * Lee el archivo carácter por carácter y reconstruye cada línea para analizarla.
     * @param nombreBuscado El texto a buscar en los nombres de los contactos.
     * @param nombreArchivo El archivo donde se realizará la búsqueda.
     */
    public static void buscarContacto(String nombreBuscado, String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            System.out.println("El archivo " + nombreArchivo + " no existe.");
            return;
        }

        boolean encontrado = false;
        try (FileReader lector = new FileReader(archivo)) {
            StringBuilder linea = new StringBuilder(); // Acumula los caracteres de una línea.
            int caracter;
            while ((caracter = lector.read()) != -1) {
                // Si se encuentra un salto de línea, se procesa la línea acumulada.
                if (caracter == '\n') {
                    if (linea.length() > 0) {
                        String lineaActual = linea.toString();
                        String nombreEnArchivo = lineaActual.split(",")[0].toLowerCase().trim();
                        if (nombreEnArchivo.contains(nombreBuscado.toLowerCase().trim())) {
                            System.out.println(lineaActual);
                            encontrado = true;
                        }
                    }
                    linea.setLength(0); // Limpia el StringBuilder para la siguiente línea.
                } else if (caracter != '\r') { // Ignora el carácter de retorno de carro.
                    linea.append((char) caracter);
                }
            }
            // Procesa la última línea si el archivo no termina con un salto de línea.
            if (linea.length() > 0) {
                String lineaActual = linea.toString();
                String nombreEnArchivo = lineaActual.split(",")[0].toLowerCase().trim();
                if (nombreEnArchivo.contains(nombreBuscado.toLowerCase().trim())) {
                    System.out.println(lineaActual);
                    encontrado = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de contactos: " + nombreArchivo);
        }

        if (!encontrado) {
            System.out.println("No se encontró ningún contacto coincidente en " + nombreArchivo);
        }
    }

    /**
     * Modifica un contacto existente. Lee el archivo original y escribe los cambios en un
     * archivo temporal, que luego reemplaza al original.
     * @param scanner Objeto Scanner para leer los nuevos datos del usuario.
     * @param nombreModificar El nombre del contacto a modificar.
     * @param nombreArchivo El archivo en el que se modificará el contacto.
     */
    public static void reemplazarContacto(Scanner scanner, String nombreModificar, String nombreArchivo) {
        File archivoOriginal = new File(nombreArchivo);
        File archivoTemporal = new File("file_temp.txt");

        if (!archivoOriginal.exists()) {
            System.out.println("El archivo " + nombreArchivo + " no existe. Nada que modificar.");
            return;
        }

        boolean encontrado = false;
        // Se usa un try-catch tradicional porque son varios flujos que se deben manejar.
        try {
            Scanner lector = new Scanner(archivoOriginal);
            FileWriter escritor = new FileWriter(archivoTemporal);

            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                String nombreEnArchivo = linea.split(",")[0].trim();

                if (nombreEnArchivo.equalsIgnoreCase(nombreModificar)) {
                    encontrado = true;
                    System.out.println("Contacto encontrado en " + nombreArchivo + ". Ingrese los nuevos datos.");
                    System.out.print("Nuevo nombre (dejar en blanco para mantener '" + nombreEnArchivo + "'): ");
                    String nuevoNombre = scanner.nextLine();
                    System.out.print("Nuevo teléfono (dejar en blanco para mantener el anterior): ");
                    String nuevoTelefono = scanner.nextLine();

                    String nombreFinal = nuevoNombre.trim().isEmpty() ? nombreEnArchivo : nuevoNombre.trim();
                    String telefonoAnterior = linea.split(",").length > 1 ? linea.split(",")[1].trim() : "";
                    String telefonoFinal = nuevoTelefono.trim().isEmpty() ? telefonoAnterior : nuevoTelefono.trim();
                    
                    escritor.write(nombreFinal + "," + telefonoFinal + System.lineSeparator());
                } else {
                    escritor.write(linea + System.lineSeparator());
                }
            }
            lector.close();
            escritor.close();

            if (encontrado) {
                if (archivoOriginal.delete()) {
                    archivoTemporal.renameTo(archivoOriginal);
                    System.out.println("Contacto actualizado exitosamente en " + nombreArchivo);
                    imprimirContacto(nombreArchivo);
                } else {
                    System.out.println("Error: No se pudo actualizar el archivo de contactos.");
                }
            } else {
                System.out.println("No se encontró un contacto con ese nombre en " + nombreArchivo);
                archivoTemporal.delete();
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al modificar el contacto: " + e.getMessage());
        }
    }

    /**
     * Elimina un contacto específico por su nombre.
     * @param nombreEliminar El nombre exacto del contacto a eliminar.
     * @param nombreArchivo El archivo del que se eliminará el contacto.
     */
    public static void eliminarContacto(String nombreEliminar, String nombreArchivo) {
        File archivoOriginal = new File(nombreArchivo);
        File archivoTemporal = new File("file_temp.txt");

        if (!archivoOriginal.exists()) {
            System.out.println("El archivo " + nombreArchivo + " no existe.");
            return;
        }

        boolean eliminado = false;
        try {
            Scanner lector = new Scanner(archivoOriginal);
            FileWriter escritor = new FileWriter(archivoTemporal);

            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                String nombreEnArchivo = linea.split(",")[0].trim();
                if (!nombreEnArchivo.equalsIgnoreCase(nombreEliminar)) {
                    escritor.write(linea + System.lineSeparator());
                } else {
                    eliminado = true;
                }
            }
            lector.close();
            escritor.close();

            if (eliminado) {
                if (archivoOriginal.delete()) {
                    archivoTemporal.renameTo(archivoOriginal);
                    System.out.println("Contacto eliminado exitosamente de " + nombreArchivo);
                } else {
                    System.out.println("Error al actualizar el archivo.");
                }
            } else {
                System.out.println("No se encontró un contacto con ese nombre en " + nombreArchivo);
                archivoTemporal.delete();
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al eliminar el contacto: " + e.getMessage());
        }
    }

    /**
     * Elimina físicamente el archivo de contactos del disco.
     * @param nombreArchivo El nombre del archivo a eliminar.
     */
    public static void eliminarArchivo(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (archivo.exists()) {
            if (archivo.delete()) {
                System.out.println("El archivo " + nombreArchivo + " ha sido eliminado.");
            } else {
                System.out.println("No se pudo eliminar el archivo " + nombreArchivo);
            }
        } else {
            System.out.println("El archivo " + nombreArchivo + " no existe.");
        }
    }
}