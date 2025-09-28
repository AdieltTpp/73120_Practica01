package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MetodosBuffer {

    /**
     * Pide al usuario un nombre y un teléfono, y los agrega al final del archivo de contactos.
     * Utiliza BufferedWriter para una escritura eficiente en búfer.
     */
    public static void agregarContacto(String nombre, String telefono, String nombreArchivo) {
        if (nombre.trim().isEmpty()) {
            System.out.println("El nombre no puede estar vacío. No se agregó el contacto.");
            return;
        }

        if (telefono.trim().isEmpty()) {
            System.out.println("El teléfono no puede estar vacío. No se agregó el contacto.");
            return;
        }

        // 'try-with-resources' para cerrar el BufferedWriter automáticamente.
        // BufferedWriter envuelve a FileWriter para mejorar el rendimiento.
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            // Escribe la línea de texto.
            escritor.write(nombre.trim() + "," + telefono.trim());
            // Escribe un salto de línea. Es más eficiente que System.lineSeparator().
            escritor.newLine();
            System.out.println("Contacto agregado exitosamente en " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Ocurrió un error al agregar el contacto en " + nombreArchivo);
        }
    }

    /**
     * Lee y muestra en consola todos los contactos del archivo.
     * Utiliza BufferedReader para leer el archivo línea por línea de forma eficiente.
     */
    public static void imprimirContacto(String nombreArchivo) {
        System.out.println("\n--- LISTA DE CONTACTOS ---");
        File archivo = new File(nombreArchivo);

        if (!archivo.exists()) {
            System.out.println("No hay contactos registrados.");
            return;
        }

        // 'try-with-resources' para cerrar el BufferedReader.
        // BufferedReader envuelve a FileReader para leer texto de forma más eficiente.
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            // El método readLine() lee una línea completa y devuelve null al final del archivo.
            while ((linea = lector.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de contactos.");
        }
    }

    /**
     * Busca contactos cuyo nombre contenga el texto ingresado por el usuario.
     * Utiliza BufferedReader para una lectura optimizada línea por línea.
     */
    public static void buscarContacto(String nombreBuscado, String nombreArchivo) {
        File archivo = new File(nombreArchivo);

        if (!archivo.exists()) {
            System.out.println("No hay contactos registrados.");
            return;
        }

        boolean encontrado = false;
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                // Salta las líneas vacías para evitar errores.
                if (linea.trim().isEmpty()) continue;

                // Divide la línea y compara la parte del nombre.
                String nombreEnArchivo = linea.split(",")[0].toLowerCase().trim();
                if (nombreEnArchivo.contains(nombreBuscado.toLowerCase().trim())) {
                    System.out.println(linea);
                    encontrado = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de contactos.");
        }

        if (!encontrado) {
            System.out.println("No se encontró ningún contacto con ese nombre.");
        }
    }   

    /**
     * Elimina físicamente el archivo de contactos del disco.
     * Este método no usa buffers porque opera sobre el archivo como un todo, no su contenido.
     */
    public static void eliminarArchivo(String nombreArchivo) {
        try {
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
        } catch (SecurityException e) {
            System.out.println("Error de seguridad: No se tienen los permisos para eliminar el archivo " + nombreArchivo);
        }
    }

    /**
     * Modifica un contacto existente usando clases buffer.
     * Lee con BufferedReader y escribe con PrintWriter en un archivo temporal.
     */
    public static void reemplazarContacto(Scanner scanner, String nombreModificar, String nombreArchivo) {
        File archivoOriginal = new File(nombreArchivo);
        File archivoTemporal = new File("buffer_temp.txt");

        if (!archivoOriginal.exists()) {
            System.out.println("El archivo de contactos no existe. Nada que modificar.");
            return;
        }

        boolean encontrado = false;
        // 'try-with-resources' para cerrar lector y escritor.
        // PrintWriter es una clase cómoda para escribir texto formateado, como con println().
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoOriginal));
             PrintWriter escritor = new PrintWriter(new FileWriter(archivoTemporal))) {

            String linea;
            while ((linea = lector.readLine()) != null) {
                String nombreEnArchivo = linea.split(",")[0].trim();

                if (nombreEnArchivo.equalsIgnoreCase(nombreModificar)) {
                    encontrado = true;
                    System.out.println("Contacto encontrado en " + nombreArchivo + ". Ingrese los nuevos datos.");
                    
                    System.out.print("Nuevo nombre (dejar en blanco para mantener '" + nombreEnArchivo + "'): ");
                    String nuevoNombre = scanner.nextLine();
                    
                    System.out.print("Nuevo teléfono (dejar en blanco para mantener el anterior): ");
                    String nuevoTelefono = scanner.nextLine();

                    // Lógica para decidir si se usa el dato nuevo o se mantiene el viejo.
                    String nombreFinal = nuevoNombre.trim().isEmpty() ? nombreEnArchivo : nuevoNombre.trim();
                    String telefonoAnterior = linea.split(",").length > 1 ? linea.split(",")[1].trim() : "";
                    String telefonoFinal = nuevoTelefono.trim().isEmpty() ? telefonoAnterior : nuevoTelefono.trim();
                    
                    // PrintWriter.println escribe la línea y añade un salto de línea automáticamente.
                    escritor.println(nombreFinal + "," + telefonoFinal);
                } else {
                    // Si no es el contacto, se escribe la línea original.
                    escritor.println(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al modificar el contacto: " + e.getMessage());
            return;
        }

        // Lógica para reemplazar el archivo original con el temporal.
        if (archivoOriginal.delete()) {
            archivoTemporal.renameTo(archivoOriginal);
        } else {
            System.out.println("Error: No se pudo actualizar el archivo de contactos.");
            return;
        }

        if (encontrado) {
            System.out.println("Contacto actualizado exitosamente en " + nombreArchivo);
            imprimirContacto(nombreArchivo);
        } else {
            System.out.println("No se encontró un contacto con ese nombre en " + nombreArchivo);
            archivoTemporal.delete();
        }
    }

    /**
     * Elimina un contacto específico por nombre usando clases buffer.
     * Lee con BufferedReader y escribe con PrintWriter, omitiendo la línea a eliminar.
     */
    public static void eliminarContacto(String nombreEliminar, String nombreArchivo) {
        File archivoOriginal = new File(nombreArchivo);
        File archivoTemporal = new File("buffer_temp.txt");

        if (!archivoOriginal.exists()) {
            System.out.println("No hay contactos para eliminar.");
            return;
        }

        boolean eliminado = false;
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoOriginal));
             PrintWriter escritor = new PrintWriter(new FileWriter(archivoTemporal))) {

            String linea;
            while ((linea = lector.readLine()) != null) {
                String nombreEnArchivo = linea.split(",")[0].trim();
                // Si la línea NO es la que se busca, se escribe en el archivo temporal.
                if (!nombreEnArchivo.equalsIgnoreCase(nombreEliminar)) {
                    escritor.println(linea);
                } else {
                    // Si es la línea a eliminar, se omite y se marca como 'eliminado'.
                    eliminado = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al eliminar el contacto: " + e.getMessage());
            return;
        }

        // Reemplaza el archivo original por el temporal.
        if (archivoOriginal.delete()) {
            archivoTemporal.renameTo(archivoOriginal);
        } else {
            System.out.println("Error: No se pudo actualizar el archivo de contactos.");
        }

        if (eliminado) {
            System.out.println("Contacto eliminado exitosamente de " + nombreArchivo);
        } else {
            System.out.println("No se encontró un contacto con ese nombre en " + nombreArchivo);
            archivoTemporal.delete();
        }
    }
}