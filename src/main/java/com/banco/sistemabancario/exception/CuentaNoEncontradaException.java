package com.banco.sistemabancario.exception;

public class CuentaNoEncontradaException extends RuntimeException{

    public CuentaNoEncontradaException (String mensaje){
        super(mensaje);
    }

    public CuentaNoEncontradaException (String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}
