package com.upiitrade.comun;

public abstract class ActivoFinanciero {
    protected String simbolo;
    protected String nombre;
    protected double precioActual;

    public ActivoFinanciero(String simbolo, String nombre, double precioActual) {
        this.simbolo = simbolo;
        this.nombre = nombre;
        this.precioActual = precioActual;
    }

    public String getSimbolo() { return simbolo; }
    public String getNombre() { return nombre; }
    public double getPrecioActual() { return precioActual; }
    public void setPrecioActual(double precioActual) { this.precioActual = precioActual; }

    // Método que implementará el Alumno 1
    public abstract void aplicarVariacionAleatoria();
}
