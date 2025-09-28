// Alumno: Adiel Jafet Poot Pech
import java.util.Scanner;
import src.MetodosFile;    // Importa la clase con los métodos de File
import src.MetodosBuffer;  // Importa la clase con los métodos de Buffer

/**
 * Clase principal que orquesta la aplicación de gestión de contactos.
 * Permite al usuario elegir al inicio si desea operar con I/O básico (File)
 * o con I/O optimizado (Buffer) durante toda la sesión. Todas las operaciones
 * se realizan sobre un único archivo para una comparación directa.
 */
public class GestorContactos {

    private static final String NOMBRE_ARCHIVO = "contactos.txt";
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Punto de entrada del programa. Primero, permite al usuario elegir el modo de
     * operación (File o Buffer) y luego entra en un bucle para gestionar las
     * operaciones de contacto.
     */
    public static void main(String[] args) {
        // 1. El usuario elige el modo de operación al inicio.
        System.out.println("=== BIENVENIDO AL GESTOR DE CONTACTOS ===");
        System.out.println("¿Qué métodos de archivo desea usar en esta sesión?");
        System.out.println("1. Métodos File (Lectura/Escritura básica)");
        System.out.println("2. Métodos Buffer (Lectura/Escritura optimizada)");
        System.out.print("Seleccione una opción: ");
        
        int modo = 0;
        // Bucle para asegurar que se elija una opción válida.
        while (modo != 1 && modo != 2) {
            try {
                modo = Integer.parseInt(scanner.nextLine());
                if (modo != 1 && modo != 2) {
                    System.out.print("Opción no válida. Por favor, elija 1 o 2: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Por favor, ingrese un número (1 o 2): ");
            }
        }

        boolean usarBuffer = (modo == 2);
        String tipoMetodo = usarBuffer ? "Buffer" : "File";
        System.out.println("\nHa elegido usar los métodos: " + tipoMetodo);

        // 2. Bucle del menú de operaciones.
        int opcionMenu;
        do {
            mostrarMenu(tipoMetodo);
            try {
                opcionMenu = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcionMenu = 0; // Asigna un valor por defecto para que entre en el 'default' del switch.
            }

            // Llama a los métodos correspondientes según el modo elegido.
            switch (opcionMenu) {
                case 1:
                    System.out.println("\n--- AGREGAR CONTACTO ---");
                    System.out.print("Ingrese el nombre del contacto: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese el teléfono del contacto: ");
                    String telefono = scanner.nextLine();
                    if (usarBuffer) MetodosBuffer.agregarContacto(nombre, telefono, NOMBRE_ARCHIVO);
                    else MetodosFile.agregarContacto(nombre, telefono, NOMBRE_ARCHIVO);
                    break;
                case 2:
                    if (usarBuffer) MetodosBuffer.imprimirContacto(NOMBRE_ARCHIVO);
                    else MetodosFile.imprimirContacto(NOMBRE_ARCHIVO);
                    break;
                case 3:
                    System.out.println("\n--- BUSCAR CONTACTO ---");
                    System.out.print("Ingrese el nombre a buscar: ");
                    String nombreBuscado = scanner.nextLine();
                    if (usarBuffer) MetodosBuffer.buscarContacto(nombreBuscado, NOMBRE_ARCHIVO);
                    else MetodosFile.buscarContacto(nombreBuscado, NOMBRE_ARCHIVO);
                    break;
                case 4:
                    System.out.println("\n--- REEMPLAZAR CONTACTO ---");
                    System.out.print("Ingrese el nombre exacto del contacto a reemplazar: ");
                    String nombreModificar = scanner.nextLine();
                    if (usarBuffer) MetodosBuffer.reemplazarContacto(scanner, nombreModificar, NOMBRE_ARCHIVO);
                    else MetodosFile.reemplazarContacto(scanner, nombreModificar, NOMBRE_ARCHIVO);
                    break;
                case 5:
                    System.out.println("\n--- ELIMINAR CONTACTO ---");
                    System.out.print("Ingrese el nombre exacto del contacto a eliminar: ");
                    String nombreEliminar = scanner.nextLine();
                    if (usarBuffer) MetodosBuffer.eliminarContacto(nombreEliminar, NOMBRE_ARCHIVO);
                    else MetodosFile.eliminarContacto(nombreEliminar, NOMBRE_ARCHIVO);
                    break;
                case 6:
                    // Esta operación es la misma para ambos, ya que usa la clase File.
                    MetodosFile.eliminarArchivo(NOMBRE_ARCHIVO);
                    break;
                case 7:
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción del 1 al 7.");
            }
        } while (opcionMenu != 7);

        scanner.close();
    }

    /**
     * Muestra el menú de operaciones disponible según el tipo de método elegido
     * (File o Buffer).
     * 
     * @param tipoMetodo Cadena que indica el tipo de método actual ("File" o "Buffer").
     */
    private static void mostrarMenu(String tipoMetodo) {
        System.out.println("\n=== MENÚ DE OPERACIONES (" + tipoMetodo + ") ===");
        System.out.println("1. Agregar contacto");
        System.out.println("2. Imprimir contactos");
        System.out.println("3. Buscar contacto");
        System.out.println("4. Reemplazar contacto");
        System.out.println("5. Eliminar contacto");
        System.out.println("6. Eliminar archivo de contactos");
        System.out.println("7. Salir");
        System.out.print("Seleccione una opción: ");
    }
}