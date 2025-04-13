package com.mycompany;

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
        String csvFile = "Datos/ventas.csv";
        String line = "";
        String cvsSplitBy = ",";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                int idVendedor = Integer.parseInt(data[1]);
                int idProducto = Integer.parseInt(data[2]);
                int cantidad = Integer.parseInt(data[3]);
                double precioVenta = Double.parseDouble(data[4]);
                LocalDate fecha = LocalDate.parse(data[5], formatter);
                Venta venta = new Venta(idVendedor, idProducto, cantidad, precioVenta, fecha);
                ventas.add(venta);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar ventas desde CSV: " + e.getMessage());
        }
    }
}