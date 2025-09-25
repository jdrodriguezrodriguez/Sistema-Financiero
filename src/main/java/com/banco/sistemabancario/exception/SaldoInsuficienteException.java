package com.banco.sistemabancario.exception;

public class SaldoInsuficienteException extends RuntimeException{
    public SaldoInsuficienteException(String mensaje){
        super(mensaje);
    }

    public SaldoInsuficienteException(String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}
