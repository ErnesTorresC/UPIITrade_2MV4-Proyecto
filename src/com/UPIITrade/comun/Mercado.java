package com.upiitrade.comun;

import java.util.ArrayList;
import java.util.List;

public class Mercado {
    private static Mercado instancia;
    private final List<ActivoFinanciero> activos;
    private double saldoUsuario;
    private String nombreUsuario;

    private Mercado() {
        this.activos = new ArrayList<>();
        this.saldoUsuario = 0.0;
    }

    public static synchronized Mercado getInstancia() {
        if (instancia == null) {
            instancia = new Mercado();
        }
        return instancia;
    }

    public synchronized void registrarCliente(String nombre, double montoInicial) {
        this.nombreUsuario = nombre;
        this.saldoUsuario = montoInicial;
    }

    public synchronized List<ActivoFinanciero> getActivos() {
        return new ArrayList<>(activos);
    }

    public synchronized void agregarActivo(ActivoFinanciero activo) {
        this.activos.add(activo);
    }

    public synchronized void modificarPrecio(String simbolo, double nuevoPrecio) {
        for (ActivoFinanciero a : activos) {
            if (a.getSimbolo().equalsIgnoreCase(simbolo)) {
                a.setPrecioActual(nuevoPrecio);
                break;
            }
        }
    }

    public synchronized double getSaldoUsuario() { return saldoUsuario; }
    public synchronized void modificarSaldo(double cantidad) { this.saldoUsuario += cantidad; }
    public String getNombreUsuario() { return nombreUsuario; }
}
