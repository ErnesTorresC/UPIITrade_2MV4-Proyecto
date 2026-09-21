package com.upiitrade.control;

// Excepción en caso de no contar con el inventario suficiente para la venta
public class InventarioInsuficienteException extends Exception {

    public InventarioInsuficienteException(String simbolo, int disponible, int seleccion) {
        super("No puedes vender " + seleccion + " unidades de " + simbolo
                + ". Solo tienes " + disponible + ".");
    }
}