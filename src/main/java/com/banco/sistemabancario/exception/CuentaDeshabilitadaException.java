package com.banco.sistemabancario.exception;

public class CuentaDeshabilitadaException extends RuntimeException{
    public CuentaDeshabilitadaException(String mensaje){
        super(mensaje);
    }

    public CuentaDeshabilitadaException(String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}

