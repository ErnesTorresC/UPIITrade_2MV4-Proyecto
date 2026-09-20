package com.upiitrade.archivos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.upiitrade.modelos.Accion;
import com.upiitrade.modelos.Criptomoneda;
import com.upiitrade.motor.Mercado; 

public class GestorArchivos {

    public void cargarActivosIniciales() {
        String archivo = "activos.txt";
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                
                if (datos.length >= 4) {
                    String tipo = datos[0].trim().toUpperCase();
                    String simbolo = datos[1].trim();
                    String nombre = datos[2].trim();
                    double precio = Double.parseDouble(datos[3].trim());

                    if (tipo.equals("ACCION")) {
                        Accion nuevaAccion = new Accion(simbolo, nombre, precio);
                        Mercado.getInstancia().agregarActivo(nuevaAccion);
                    } else if (tipo.equals("CRIPTO")) {
                        Criptomoneda nuevaCripto = new Criptomoneda(simbolo, nombre, precio);
                        Mercado.getInstancia().agregarActivo(nuevaCripto);
                    }
                }
            }
            System.out.println("Activos iniciales cargados correctamente.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de activos: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error de formato numérico en activos.txt: " + e.getMessage());
        }
    }

    public void registrarOperacion(String operacion) {
        String archivo = "historial_operaciones.txt";
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String marcaDeTiempo = ahora.format(formatter);
            
            bw.write("[" + marcaDeTiempo + "] " + operacion);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al registrar la operación en el historial: " + e.getMessage());
        }
    }
}