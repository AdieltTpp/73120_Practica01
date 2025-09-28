# Administracion de archivos
# Práctica 01: Gestor de Contactos
# Alumno: Adiel Jafet Poot Pech
## 1. Resumen del Proyecto

Este proyecto es una aplicación de consola que funciona como un gestor de contactos simple. Su objetivo principal es servir como una herramienta educativa para **demostrar y comparar dos enfoques de manejo de archivos (I/O) en Java**:

1.  **I/O sin Búfer (`MetodosFile`)**: Utiliza las clases base `java.io.FileReader` y `java.io.FileWriter`. Estas clases interactúan directamente con el sistema de archivos, lo que puede ser menos eficiente para grandes volúmenes de datos.

2.  **I/O con Búfer (`MetodosBuffer`)**: Utiliza las clases "decoradoras" `java.io.BufferedReader` y `java.io.BufferedWriter`. Estas clases añaden un búfer de memoria intermedio, reduciendo el número de accesos al disco y mejorando significativamente el rendimiento.

Al iniciar la aplicación, el usuario elige cuál de los dos métodos de I/O desea utilizar durante toda la sesión. Todas las operaciones (agregar, buscar, etc.) se realizarán sobre un único archivo (`contactos.txt`), permitiendo así una comparación directa de la implementación y el comportamiento de cada enfoque.

## 2. Características

El programa ofrece un menú interactivo con las siguientes funcionalidades:

*   **Selección de Modo de I/O**: Al inicio, permite elegir si la sesión actual usará los métodos de `File` (básico) o `Buffer` (optimizado).
*   **1. Agregar Contacto**: Pide un nombre y un teléfono y los guarda en una nueva línea dentro de `contactos.txt`.
*   **2. Imprimir Contactos**: Lee y muestra en consola todos los contactos almacenados en el archivo.
*   **3. Buscar Contacto**: Pide un nombre y muestra todos los contactos que contengan ese texto (no distingue mayúsculas/minúsculas).
*   **4. Reemplazar Contacto**: Busca un contacto por nombre exacto y permite actualizar su nombre y/o teléfono.
*   **5. Eliminar Contacto**: Busca un contacto por nombre exacto y lo elimina del archivo.
*   **6. Eliminar Archivo de Contactos**: Borra permanentemente el archivo `contactos.txt`.
*   **7. Salir**: Termina la aplicación.

## 3. Estructura del Proyecto

El código está organizado de forma modular para separar responsabilidades:

```
Practica_01/
├── src/
│   ├── MetodosFile.java      # Lógica de negocio con I/O sin búfer.
│   └── MetodosBuffer.java    # Lógica de negocio con I/O con búfer.
│
├── GestorContactos.java      # Clase principal con el método main() y la interfaz de usuario.
│
├── contactos.txt             # Archivo de datos (se crea al agregar el primer contacto).
│
└── readme.md                 # Este archivo.
```

-   **`GestorContactos.java`**: Es el orquestador. Contiene el menú principal, gestiona la entrada del usuario y llama a los métodos estáticos correspondientes según el modo de I/O seleccionado.
-   **`src/`**: Este directorio contiene las clases de lógica, separadas por el tipo de I/O que utilizan.

## 4. Requisitos Previos

-   Tener instalado el **JDK (Java Development Kit)** en tu sistema.
-   Tener configurada la variable de entorno `PATH` para poder usar los comandos `javac` y `java` desde cualquier ubicación de la terminal.

## 5. Cómo Compilar y Ejecutar

Para compilar y ejecutar el proyecto desde la línea de comandos, sigue estos pasos:

1.  Abre una terminal o símbolo del sistema.
2.  Navega hasta el directorio raíz del proyecto (`Practica_01/`).

3.  **Compilar el proyecto:**
    El siguiente comando compila todas las clases Java y las organiza correctamente según sus paquetes.

    ```sh
    javac -d . src/MetodosFile.java src/MetodosBuffer.java GestorContactos.java
    ```
    *   `javac`: Es el compilador de Java.
    *   `-d .`: Le indica al compilador que coloque los archivos `.class` compilados en el directorio actual (`.`), respetando la estructura de paquetes (creará una carpeta `src/` para las clases de ese paquete).

4.  **Ejecutar la aplicación:**
    Una vez compilado, ejecuta la clase principal.

    ```sh
    java GestorContactos
    ```
    *   `java`: Es la Máquina Virtual de Java (JVM) que ejecuta el código.
    *   `GestorContactos`: Es el nombre de la clase que contiene el método `public static void main(String[] args)`.

## 6. Cómo Usar el Programa

1.  Al ejecutar la aplicación, primero se te pedirá que elijas el modo de operación:
    ```
    === BIENVENIDO AL GESTOR DE CONTACTOS ===
    ¿Qué métodos de archivo desea usar en esta sesión?
    1. Métodos File (Lectura/Escritura básica)
    2. Métodos Buffer (Lectura/Escritura optimizada)
    Seleccione una opción:
    ```
2.  Ingresa `1` o `2` y presiona Enter. Tu elección determinará cómo se manejarán los archivos durante el resto del programa.
3.  A continuación, verás el menú principal de operaciones.
4.  Selecciona una opción del menú (del 1 al 7) y sigue las instrucciones en pantalla para cada operación.
5.  El programa continuará mostrando el menú después de cada operación hasta que elijas la opción `7. Salir`.