package com.upiitrade.modelos;

import com.upiitrade.comun.ActivoFinanciero;
import java.util.Random;

public class Criptomoneda extends ActivoFinanciero {

    private static final double VARIACION_MIN = -0.10; 
    private static final double VARIACION_MAX = 0.10;  

    private final Random random = new Random();

    public Criptomoneda(String simbolo, String nombre, double precioActual) {
        super(simbolo, nombre, precioActual);
    }

    @Override
    public void aplicarVariacionAleatoria() {
        double variacion = VARIACION_MIN + (VARIACION_MAX - VARIACION_MIN) * random.nextDouble();
        double nuevoPrecio = precioActual * (1 + variacion);
        setPrecioActual(nuevoPrecio);
    }
}