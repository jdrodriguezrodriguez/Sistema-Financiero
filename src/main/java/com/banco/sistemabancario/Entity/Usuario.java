package com.banco.sistemabancario.Entity;

public class Usuario {
    private String Nombre, Contraseña, Correo;
    private int Documento, Celular;


    public Usuario(String Nombre, int Documento, String Correo,  int Celular, String Contraseña){
        this.Nombre = Nombre;
        this.Documento = Documento;
        this.Correo = Correo;
        this.Celular = Celular;
        this.Contraseña = Contraseña;
    }

    public String getNombre(){
        return Nombre;
    }
    public void setNombre(String Nombre){
        this.Nombre = Nombre;
    }

    public String getContraseña(){
        return Contraseña;
    }
    public void setContraseña(String Contraseña){
        this.Contraseña = Contraseña;
    }

    public String getCorreo(){
        return Correo;
    }
    public void setCorreo(String Correo){
        this.Correo = Correo;
    }

    public int getDocumento() {
        return Documento;
    }
    public void setDocumento(int Documento) {
        this.Documento = Documento;
    }

    public int getCelular() {
        return Celular;
    }
    public void setCelular(int Celular) {
        this.Celular = Celular;
    }

    
}