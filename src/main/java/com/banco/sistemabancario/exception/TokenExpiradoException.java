package com.banco.sistemabancario.exception;

public class TokenExpiradoException extends RuntimeException{
    
    public TokenExpiradoException(String mensaje){
        super(mensaje);    
    }

    public TokenExpiradoException(String mensaje, Throwable causa){
        super(mensaje, causa);    
    }

}
