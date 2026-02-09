package com.banco.sistemabancario.exception;

public class TokenUsadoException extends RuntimeException{
    
    public TokenUsadoException(String mensaje){
        super(mensaje);    
    }

    public TokenUsadoException(String mensaje, Throwable causa){
        super(mensaje, causa);    
    }

}
