package com.banco.sistemabancario.Entity;

public class Usuario {
    private String Nombre, Contraseña;


    public Usuario(String Nombre, String Contraseña){
        this.Nombre = Nombre;
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
}