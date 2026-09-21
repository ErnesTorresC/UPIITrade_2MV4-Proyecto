/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.upiitrade.motor;
import com.upiitrade.mercado.Mercado; 
import com.upiitrade.mercado.Activo;  
import java.util.List;
/**
 *
 * @author ERNESTO T C
 */





public class SimuladorPrecios implements Runnable {

    private final int segundosEspera;

    /**
     * 
     */
    public SimuladorPrecios() {
        this.segundosEspera = 2;
    }

    /**
     * 
     * @param segundosEspera Tiempo en segundos entre cada fluctuación.
     */
    public SimuladorPrecios(int segundosEspera) {
        this.segundosEspera = segundosEspera;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Detener el hilo durante segundos
                Thread.sleep(this.segundosEspera * 1000L);

                // Recuperar la lista de activos del mercado
                List<Activo> activos = Mercado.getInstancia().getActivos();

                // Iterar la lista y aplicar la variación aleatoria a cada activo
                for (Activo activo : activos) {
                    double nuevoPrecio = activo.aplicarVariacionAleatoria();
                    
                    // Actualizar el precio en el mercado usando el símbolo del activo
                    Mercado.getInstancia().modificarPrecio(activo.getSimbolo(), nuevoPrecio);
                }

            } catch (InterruptedException e) {
                System.err.println("El simulador de precios en segundo plano fue interrumpido.");
                
                Thread.currentThread().interrupt();
                break; // Finaliza el bucle si el hilo es interrumpido 
            }
        }
    }
}

