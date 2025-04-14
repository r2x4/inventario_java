package com.example.demo.mycompany;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenerateInfoFiles {

    private static final String CARPETA_DATOS = "Datos/";
    private static final String SEPARADOR = ",";

    public static void generarVendedores() throws IOException {
        List<Vendedor> vendedores = new ArrayList<>();
        vendedores.add(new Vendedor("Vendedor 1", "ID123", "Ciudad A", "Pais X", LocalDate.of(2023, 1, 15)));
        vendedores.add(new Vendedor("Vendedor 2", "ID456", "Ciudad B", "Pais Y", LocalDate.of(2023, 2, 20)));
        vendedores.add(new Vendedor("Vendedor 3", "ID789", "Ciudad C", "Pais Z", LocalDate.of(2023, 3, 10)));

        guardarEnCSV("vendedores.csv",
                "id,nombre,idIdentificacion,ciudad,pais,fechaIngreso\n",
                vendedores, v -> String.format("%d,%s,%s,%s,%s,%s",
                        v.getId(), v.getNombre(), v.getIdIdentificacion(),
                        v.getCiudad(), v.getPais(), v.getFechaIngreso()));
    }

    public static void generarProductos() throws IOException {
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto("Producto A", "Marca 1", 10.50, 50, LocalDate.of(2023, 4, 5)));
        productos.add(new Producto("Producto B", "Marca 2", 25.75, 30, LocalDate.of(2023, 5, 12)));
        productos.add(new Producto("Producto C", "Marca 1", 5.99, 100, LocalDate.of(2023, 6, 20)));

        guardarEnCSV("productos.csv",
                "id,nombre,marca,valorCompra,stock,fechaIngreso\n",
                productos, p -> String.format("%d,%s,%s,%.2f,%d,%s",
                        p.getId(), p.getNombre(), p.getMarca(),
                        p.getValorCompra(), p.getStock(),p.getFechaIngreso()));
    }

    public static void generarVentas() throws IOException {
        List<Venta> ventas = new ArrayList<>();
        ventas.add(new Venta(1, 1, 5, 15.00, LocalDate.of(2023, 7, 1)));
        ventas.add(new Venta(2, 2, 2, 30.50, LocalDate.of(2023, 7, 10)));
        ventas.add(new Venta(1, 3, 10, 7.50, LocalDate.of(2023, 7, 20)));

        guardarEnCSV("ventas.csv",
                "id,id_vendedor,id_producto,cantidad,precio_venta,fecha\n",
                ventas, v -> String.format("%d,%d,%d,%d,%.2f,%s",
                        v.getId(), v.getIdVendedor(), v.getIdProducto(),
                        v.getCantidad(), v.getPrecioVenta(), v.getFecha()));
    }

    public static void generarTodosLosDatos() throws IOException {
        generarVendedores();
        generarProductos();
        generarVentas();
    }

    private static <T> void guardarEnCSV(String filename, String cabecera,
                                         List<T> datos, FormateadorCSV<T> formateador)
            throws IOException {
        crearDirectorioSiNoExiste();
        File archivo = new File(CARPETA_DATOS + filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo, StandardCharsets.UTF_8))) {
            writer.write(cabecera);
            for (T item : datos) {
                writer.write(formateador.formatear(item) + "\n");
            }
        }
    }

    private static void crearDirectorioSiNoExiste() {
        File directorio = new File(CARPETA_DATOS);
        if (!directorio.exists()) {
            directorio.mkdir();
        }
    }

    @FunctionalInterface
    private interface FormateadorCSV<T> {
        String formatear(T item);
    }
}