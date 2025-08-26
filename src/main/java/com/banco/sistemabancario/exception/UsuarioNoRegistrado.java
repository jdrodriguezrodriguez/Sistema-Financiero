package com.banco.sistemabancario.exception;

public class UsuarioNoRegistrado extends RuntimeException{
    public UsuarioNoRegistrado(String mensaje){
        super(mensaje);
    }

    public UsuarioNoRegistrado(String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}
