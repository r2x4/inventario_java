package com.mycompany;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Producto {
    private static int idCounter = 1;
    private int id;
    private String nombre;
    private String marca;
    private double valorCompra;
    private int stock;
    private LocalDate fechaIngreso;

    public Producto(String nombre, String marca, double valorCompra, int cantidadIngreso, LocalDate fechaIngreso) {
        this.id = idCounter++;
        this.nombre = nombre;
        this.marca = marca;
        this.valorCompra = valorCompra;
        this.stock = cantidadIngreso;
        this.fechaIngreso = fechaIngreso;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public double getValorCompra() { return valorCompra; }
    public int getStock() { return stock; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }

    public void reducirStock(int cantidad) {
        if (cantidad > 0 && cantidad <= stock) {
            stock -= cantidad;
        } else {
            System.out.println("Cantidad inválida para reducir stock.");
        }
    }

    public static Producto crearProducto(Scanner scanner) {
        System.out.println("Ingrese el nombre del producto:");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese la marca del producto:");
        String marca = scanner.nextLine();

        System.out.println("Ingrese el valor de compra del producto:");
        double valorCompra = scanner.nextDouble();

        System.out.println("¿Cuántas unidades ingresaron al inventario?");
        int cantidadIngreso = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        System.out.println("Ingrese la fecha de ingreso (YYYY-MM-DD):");
        LocalDate fechaIngreso = LocalDate.parse(scanner.nextLine());

        return new Producto(nombre, marca, valorCompra, cantidadIngreso, fechaIngreso);
    }
    public static void cargarProductosDesdeCSV(List<Producto> productos) {
        String filename = "Datos/productos.csv";
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("El archivo " + filename + " no existe. No se cargarán productos.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Leer y descartar la cabecera
            if(line == null || !line.equals("id,nombre,marca,valorCompra,stock,fechaIngreso")){
                System.out.println("El archivo " + filename + " no tiene la cabecera correcta. No se cargarán productos.");
                return;
            }

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    try {
                        int id = Integer.parseInt(data[0]);
                        String nombre = data[1];
                        String marca = data[2];
                        double valorCompra = Double.parseDouble(data[3]);
                        int stock = Integer.parseInt(data[4]);
                        LocalDate fechaIngreso = LocalDate.parse(data[5], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        Producto producto = new Producto(nombre, marca, valorCompra, stock, fechaIngreso);
                        producto.id = id;
                        productos.add(producto);
                    } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                        System.err.println("Error al parsear una línea en " + filename + ": " + line);
                    }
                } else {
                    System.err.println("Formato incorrecto en una línea de " + filename + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + filename + ": " + e.getMessage());
        }
    }
}