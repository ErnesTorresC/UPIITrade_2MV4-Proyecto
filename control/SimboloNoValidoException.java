package com.upiitrade.control;

// Excepción en caso de ingresar un símbolo inválido
public class SimboloNoValidoException extends Exception {

    public SimboloNoValidoException(String simbolo) {
        super("El simbolo '" + simbolo + "' no existe en el mercado.");
    }
}