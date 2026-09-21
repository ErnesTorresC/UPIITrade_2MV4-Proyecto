package com.upiitrade.control;

// Excepción en caso de no contar con los fondos suficientes
public class FondosInsuficientesException extends Exception {

    public FondosInsuficientesException(double costo, double saldo) {
        super("Fondos insuficientes. Costo: $" + String.format("%.2f", costo)
                + " | Saldo disponible: $" + String.format("%.2f", saldo));
    }
}