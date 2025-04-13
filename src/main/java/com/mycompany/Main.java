package com.mycompany;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

    private static final String CARPETA_DATOS = "Datos/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Listas en memoria
    private static List<Vendedor> vendedores = new ArrayList<>();
    private static List<Producto> productos = new ArrayList<>();
    private static List<Venta> ventas = new ArrayList<>();

    public static void main(String[] args) {
        // Configurar la codificación UTF-8 para la consola
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new java.io.PrintStream(System.err, true, StandardCharsets.UTF_8));

        try {
            crearDirectorioSiNoExiste();
            cargarDatosDesdeCSV();
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

            while (true) {
                System.out.println("\nMENÚ PRINCIPAL");
                System.out.println("1. Registrar vendedores");
                System.out.println("2. Registrar productos");
                System.out.println("3. Registrar ventas");
                System.out.println("4. Generar archivos CSV");
                System.out.println("5. Salir");
                System.out.println("6. Generar datos de ejemplo");
                System.out.print("Seleccione una opción (1-6): ");

                String opcion = scanner.nextLine();

                try {
                    switch (opcion) {
                        case "1":
                            Vendedor.crearVendedor(scanner);
                            break;
                        case "2":
                            Producto.crearProducto(scanner);
                            break;
                        case "3":
                            registrarVentas(scanner);
                            break;
                        case "4":
                            generarArchivosCSV();
                            break;
                        case "5":
                            System.out.println("Saliendo del sistema...");
                            return;
                        case "6":
                            GenerateInfoFiles.generarTodosLosDatos();
                            break;
                        default:
                            System.out.println("Opción inválida. Intente nuevamente.");
                    }
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error inesperado en la ejecucion del sistema");
            e.printStackTrace();
        }
    }
    
    private static void registrarVentas(Scanner scanner) throws IOException {
        System.out.println("\nREGISTRO DE VENTAS");
        System.out.print("¿Cuántas ventas desea registrar? ");
        int cantidad = leerEnteroPositivo(scanner);

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\nVenta #" + (i + 1));
            System.out.print("Fecha de venta (YYYY-MM-DD): ");
            LocalDate fecha = LocalDate.parse(scanner.nextLine(), DATE_FORMAT);
            Venta venta = Venta.crearVenta(scanner);
            venta.setFecha(fecha);
            ventas.add(venta);
        }
        System.out.println("\n" + cantidad + " ventas registradas en memoria.");
    }

    private static void generarArchivosCSV() throws IOException {
        File directorio = new File(CARPETA_DATOS);
        if (!directorio.exists()) {
            if (!directorio.mkdir()) {
                System.out.println("No se pudo crear el directorio " + CARPETA_DATOS);
                return;
            }
        }

        if (vendedores.isEmpty() && productos.isEmpty() && ventas.isEmpty()) {
            System.out.println("\nNo hay datos registrados para generar archivos.");
            return;
        }

        guardarEnCSV("vendedores.csv",
            "id,nombre,idIdentificacion,ciudad,pais\n",
            vendedores, v -> String.format("%d,%s,%s,%s,%s",
                v.getId(), v.getNombre(), v.getIdIdentificacion(),
                v.getCiudad(), v.getPais()));

        guardarEnCSV("productos.csv",
            "id,nombre,marca,valorCompra,stock\n",
            productos, p -> String.format("%d,%s,%s,%.2f,%d",
                p.getId(), p.getNombre(), p.getMarca(),
                p.getValorCompra(), p.getStock()));

        guardarEnCSV("ventas.csv",
            "id,id_vendedor,id_producto,cantidad,precio_venta,fecha\n",
            ventas, v -> String.format("%d,%d,%d,%d,%.2f,%s",
                v.getId(), v.getIdVendedor(), v.getIdProducto(),
                v.getCantidad(), v.getPrecioVenta(), v.getFecha()));

        System.out.println("\nArchivos CSV generados exitosamente en: " + CARPETA_DATOS);
    }

    private static <T> void guardarEnCSV(String filename, String cabecera,
                                         List<T> datos, FormateadorCSV<T> formateador)
                                         throws IOException {
        File archivo = new File(CARPETA_DATOS + filename);
         if (!archivo.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, StandardCharsets.UTF_8))) {
                writer.write(cabecera);
                for (T item : datos) {
                    writer.write(formateador.formatear(item) + "\n");
                }
            }
        }
    }

    private static void crearDirectorioSiNoExiste() {
        File directorio = new File(CARPETA_DATOS);
        if (!directorio.exists()) {
            if (directorio.mkdir()) {
                System.out.println("Directorio " + CARPETA_DATOS + " creado");
            } else {
                System.out.println("No se pudo crear el directorio " + CARPETA_DATOS);
            }
        }
    }

    private static int leerEnteroPositivo(Scanner scanner) {
        while (true) {
            try {
                int numero = Integer.parseInt(scanner.nextLine());
                if (numero > 0) {
                    return numero;
                }
                System.out.print("Debe ser mayor a 0. Intente nuevamente: ");
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número: ");
            }
        }
    }

    private static void cargarDatosDesdeCSV() {
        try {
            cargarVendedoresDesdeCSV();
            cargarProductosDesdeCSV();
            cargarVentasDesdeCSV();
            System.out.println("Datos cargados exitosamente desde archivos CSV.");
        } catch (IOException e) {
            System.out.println("Error al cargar datos desde CSV: " + e.getMessage());
        }
    }

    private static void cargarVendedoresDesdeCSV() throws IOException {
        File archivo = new File(CARPETA_DATOS + "vendedores.csv");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            reader.readLine(); // Ignorar la cabecera
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length == 5) {
                    String nombre = campos[1];
                    String idIdentificacion = campos[2];
                    String ciudad = campos[3];
                    String pais = campos[4];
                    Vendedor vendedor = new Vendedor(nombre, idIdentificacion, ciudad, pais, LocalDate.now());
                    vendedores.add(vendedor);
                }
            }
        }
    }

    private static void cargarProductosDesdeCSV() throws IOException {
        File archivo = new File(CARPETA_DATOS + "productos.csv");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            reader.readLine(); // Ignorar la cabecera
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length == 5) {
                    String nombre = campos[1];
                    String marca = campos[2];
                    double valorCompra = Double.parseDouble(campos[3]);
                    int stock = Integer.parseInt(campos[4]);
                    Producto producto = new Producto(nombre, marca, valorCompra, stock, LocalDate.now());
                    productos.add(producto);
                }
            }
        }
    }

    private static void cargarVentasDesdeCSV() throws IOException {
        File archivo = new File(CARPETA_DATOS + "ventas.csv");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            reader.readLine(); // Ignorar la cabecera
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length == 6) {
                    int idVendedor = Integer.parseInt(campos[1]);
                    int idProducto = Integer.parseInt(campos[2]);
                    int cantidad = Integer.parseInt(campos[3]);
                    double precioVenta = Double.parseDouble(campos[4]);
                    LocalDate fecha = LocalDate.parse(campos[5]);
                    Venta venta = new Venta(idVendedor, idProducto, cantidad, precioVenta, fecha);
                    ventas.add(venta);
                }
            }
        }
    }

    @FunctionalInterface
    private interface FormateadorCSV<T> {
        String formatear(T item);
    }
}