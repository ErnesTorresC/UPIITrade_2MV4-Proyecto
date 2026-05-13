package com.upiitrade.comun;

// Interfaz regulatoria que implementará el Alumno 5
public interface Operable {
    void procesarCompra(String simbolo, int cantidad) throws Exception;
    void procesarVenta(String simbolo, int cantidad) throws Exception;
}
