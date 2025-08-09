package com.banco.sistemabancario.exception;

public class CamposVaciosException extends RuntimeException{
    public CamposVaciosException (String mensaje){
        super(mensaje);
    }

    public CamposVaciosException (String mensaje, Throwable causa){
        super(mensaje, causa);
    }
}
