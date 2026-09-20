package com.upiitrade.modelos;

import com.upiitrade.comun.ActivoFinanciero;
import java.util.Random;

public class Accion extends ActivoFinanciero {

    private static final double VARIACION_MIN = -0.02; 
    private static final double VARIACION_MAX = 0.02;  

    private final Random random = new Random();

    public Accion(String simbolo, String nombre, double precioActual) {
        super(simbolo, nombre, precioActual);
    }

    @Override
    public void aplicarVariacionAleatoria() {
        // Genera un porcentaje de variación aleatorio entre -2% y +2%
        double variacion = VARIACION_MIN + (VARIACION_MAX - VARIACION_MIN) * random.nextDouble();
        double nuevoPrecio = precioActual * (1 + variacion);
        setPrecioActual(nuevoPrecio);
    }
}