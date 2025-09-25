package com.banco.sistemabancario.exception;

public class ValorInvalidoException extends RuntimeException{

    public ValorInvalidoException (String mensaje){
        super(mensaje);
    }

    public ValorInvalidoException (String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}
