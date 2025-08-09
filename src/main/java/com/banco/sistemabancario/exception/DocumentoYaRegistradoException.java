package com.banco.sistemabancario.exception;

public class DocumentoYaRegistradoException extends RuntimeException{
    public DocumentoYaRegistradoException (String mensaje){
        super(mensaje);
    }

    public DocumentoYaRegistradoException (String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}
