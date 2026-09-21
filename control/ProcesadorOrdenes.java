package com.upiitrade.control;

import java.util.ArrayList;

import com.upiitrade.archivos.GestorArchivos;
import com.upiitrade.comun.ActivoFinanciero;
import com.upiitrade.comun.Mercado;
import com.upiitrade.comun.Operable;

public class ProcesadorOrdenes implements Operable {

    private GestorArchivos gestor;

    private ArrayList<String> monedaCartera;
    private ArrayList<Integer> cantidadCartera;

    public ProcesadorOrdenes(GestorArchivos gestor) {
        this.gestor = gestor;
        this.monedaCartera = new ArrayList<String>();
        this.cantidadCartera = new ArrayList<Integer>();
    }
    
    // ------------ COMPRA ------------ 
    @Override
    public void procesarCompra(String simbolo, int cantidad)
            throws SimboloNoValidoException, FondosInsuficientesException {

        // Buscar el activo, si no existe manda excepción
        ActivoFinanciero activo = buscarActivo(simbolo);
        if (activo == null) {
            throw new SimboloNoValidoException(simbolo);
        }

        // Cálculo del costo
        double costo = activo.getPrecioActual() * cantidad;
        double saldo = Mercado.getInstancia().getSaldoUsuario();

        // Comprueba fondos, si no hay suficientes manda excepción
        if (costo > saldo) {
            throw new FondosInsuficientesException(costo, saldo);
        }

        // Restar saldo y suma al portafolio
        Mercado.getInstancia().modificarSaldo(-costo);

        int posicion = buscarPosicionEnCartera(simbolo);
        if (posicion == -1) {
            //  Si es la primera vez que compra una moneda, la agrega al final
            monedaCartera.add(simbolo);
            cantidadCartera.add(cantidad);
        } else {
            // Si ya tenia de esta moneda, suma a lo que ya habia en esa posicion
            int actuales = cantidadCartera.get(posicion);
            cantidadCartera.set(posicion, actuales + cantidad);
        }

        // Registrar la operacion en el archivo
        gestor.registrarOperacion("COMPRA | " + cantidad + " x " + simbolo
                + " $" + String.format("%.2f", activo.getPrecioActual())
                + " | Total: $" + String.format("%.2f", costo));
    }

    // ------------ VENTA ------------ 
    @Override
    public void procesarVenta(String simbolo, int cantidad)
            throws SimboloNoValidoException, InventarioInsuficienteException {

        // Buscar el activo, si no existe manda excepción
        ActivoFinanciero activo = buscarActivo(simbolo);
        if (activo == null) {
            throw new SimboloNoValidoException(simbolo);
        }

        // Busca cuanto tiene de esta moneda en su cartera (0 si nunca ha comprado)
        int posicion = buscarPosicionEnCartera(simbolo);
        int enCartera = 0;
        if (posicion != -1) {
            enCartera = cantidadCartera.get(posicion);
        }

        // Comprueba que tenga suficientes unidades, si no manda excepción
        if (cantidad > enCartera) {
            throw new InventarioInsuficienteException(simbolo, enCartera, cantidad);
        }

        // Cálculo de la venta
        double ganancia = activo.getPrecioActual() * cantidad;

        // Sumar saldo y restar del portafolio
        Mercado.getInstancia().modificarSaldo(ganancia);
        cantidadCartera.set(posicion, enCartera - cantidad);

        // Registrar la operacion en el archivo
        gestor.registrarOperacion("VENTA  | " + cantidad + " x " + simbolo
                + " $" + String.format("%.2f", activo.getPrecioActual())
                + " | Total: $" + String.format("%.2f", ganancia));
    }

    // ------------ AUXILIARES ------------ 

    /* Recorre la lista del mercado y devuelve el activo con ese simbolo,
       o null si no lo encuentra*/
    private ActivoFinanciero buscarActivo(String simbolo) {
        for (ActivoFinanciero a : Mercado.getInstancia().getActivos()) {
            if (a.getSimbolo().equalsIgnoreCase(simbolo)) {
                return a;
            }
        }
        return null;
    }

    /* Recorre simbolosCartera buscando el simbolo dado y devuelve
       la posicion donde esta*/
    private int buscarPosicionEnCartera(String simbolo) {
        for (int i = 0; i < monedaCartera.size(); i++) {
            if (monedaCartera.get(i).equalsIgnoreCase(simbolo)) {
                return i;
            }
        }
        return -1;
    }

    // Se muestra en la GUI las unidades que tiene el usuario
    public int getCantidadEnCartera(String simbolo) {
        int posicion = buscarPosicionEnCartera(simbolo);
        if (posicion == -1) {
            return 0;
        }
        return cantidadCartera.get(posicion);
    }
}