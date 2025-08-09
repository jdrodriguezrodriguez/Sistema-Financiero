package com.banco.sistemabancario.exception;

public class PasswordInvalidaException extends RuntimeException{
    public PasswordInvalidaException (String mensaje){
        super(mensaje);
    }

    public PasswordInvalidaException (String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}
