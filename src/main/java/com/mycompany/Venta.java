package com.mycompany;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Venta {
    private static int idCounter = 1;
    private int id;
    private int idVendedor;
    private int idProducto;
    private int cantidad;
    private double precioVenta;
    private LocalDate fecha;

    public Venta(int idVendedor, int idProducto, int cantidad, double precioVenta, LocalDate fecha) {
        this.id = idCounter++;
        this.idVendedor = idVendedor;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public int getIdVendedor() { return idVendedor; }
    public int getIdProducto() { return idProducto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioVenta() { return precioVenta; }
    public LocalDate getFecha() { return fecha; }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public static Venta crearVenta(Scanner scanner) {
        System.out.print("ID Vendedor: ");
        int idVendedor = scanner.nextInt();

        System.out.print("ID Producto: ");
        int idProducto = scanner.nextInt();

        System.out.print("Cantidad: ");
        int cantidad = scanner.nextInt();

        System.out.print("Precio Venta: ");
        double precioVenta = scanner.nextDouble();

        scanner.nextLine(); // Limpiar buffer

        System.out.print("Fecha de venta (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        return new Venta(idVendedor, idProducto, cantidad, precioVenta, fecha);
    }
    
    public static void cargarVentasDesdeCSV(List<Venta> ventas) {
        String filename = "Datos/ventas.csv";
        File file = new File(filename);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (!file.exists()) {
            System.out.println("El archivo " + filename + " no existe. No se cargarán ventas.");
            return;
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Leer y descartar la cabecera
            if(line == null || !line.equals("id,id_vendedor,id_producto,cantidad,precio_venta,fecha")) {
                System.out.println("El archivo " + filename + " no tiene la cabecera correcta. No se cargarán ventas.");
                return;
            }
    
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    try{
                        int id = Integer.parseInt(data[0]);
                        int idVendedor = Integer.parseInt(data[1]);
                        int idProducto = Integer.parseInt(data[2]);
                        int cantidad = Integer.parseInt(data[3]);
                        double precioVenta = Double.parseDouble(data[4]);
                        LocalDate fecha = LocalDate.parse(data[5], formatter);
                        Venta venta = new Venta(idVendedor, idProducto, cantidad, precioVenta, fecha);
                        if (id >= idCounter) {
                            idCounter = id + 1;
                        }
                        ventas.add(venta);
                    } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                        System.err.println("Error al parsear una línea en " + filename + ": " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + filename + ": " + e.getMessage());
        }
    }
}