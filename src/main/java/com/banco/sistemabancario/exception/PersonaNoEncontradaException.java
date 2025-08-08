package com.banco.sistemabancario.exception;

public class PersonaNoEncontradaException extends RuntimeException{
    public PersonaNoEncontradaException(String mensaje){
        super(mensaje);
    }

    public PersonaNoEncontradaException(String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}
