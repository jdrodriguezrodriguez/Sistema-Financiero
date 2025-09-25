package com.banco.sistemabancario.exception;

public class UsuarioNoencontradoException extends RuntimeException{
    public UsuarioNoencontradoException (String mensaje){
        super(mensaje);
    }

    public UsuarioNoencontradoException (String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}
