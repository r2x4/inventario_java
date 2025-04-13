package com.mycompany;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Vendedor {
    private static int idCounter = 1;
    private int id;
    private String nombre;
    private String idIdentificacion;
    private String ciudad;
    private String pais;
    private LocalDate fechaIngreso;

    public Vendedor(String nombre, String idIdentificacion, String ciudad, String pais, LocalDate fechaIngreso) {
        this.id = idCounter++;
        this.nombre = nombre;
        this.idIdentificacion = idIdentificacion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.fechaIngreso = fechaIngreso;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getIdIdentificacion() { return idIdentificacion; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }

    public static Vendedor crearVendedor(Scanner scanner) {
        System.out.println("Ingrese el nombre del vendedor:");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese el ID de identificación:");
        String idIdentificacion = scanner.nextLine();

        System.out.println("Ingrese la ciudad:");
        String ciudad = scanner.nextLine();

        System.out.println("Ingrese el país:");
        String pais = scanner.nextLine();

        System.out.println("Ingrese la fecha de ingreso (YYYY-MM-DD):");
        LocalDate fechaIngreso = LocalDate.parse(scanner.nextLine());

        return new Vendedor(nombre, idIdentificacion, ciudad, pais, fechaIngreso);
    }
    public static void cargarVendedoresDesdeCSV(List<Vendedor> vendedores) throws IOException {
        String carpetaDatos = "Datos/";
        String filename = "vendedores.csv";
        File archivo = new File(carpetaDatos + filename);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (!archivo.exists()) {
            System.out.println("El archivo " + filename + " no existe.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String line;
            reader.readLine(); // Saltar la cabecera
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    int id = Integer.parseInt(data[0]);
                    String nombre = data[1];
                    String idIdentificacion = data[2];
                    String ciudad = data[3];
                    String pais = data[4];
                    LocalDate fechaIngreso = LocalDate.parse(data[5], formatter);
                    Vendedor vendedor = new Vendedor(nombre, idIdentificacion, ciudad, pais, fechaIngreso);
                    vendedor.id = id;
                    if (id >= idCounter){
                        idCounter = id + 1;
                    }
                    vendedores.add(vendedor);
                }
            }
        }
    }
}