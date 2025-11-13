package com.banco.sistemabancario.util;

public final class UsuarioUtils {
    
    private UsuarioUtils(){}
    
    public static String generarUsername(String nombre, String apellido){
        return nombre.substring(0, Math.min(4, nombre.length())) + apellido.substring(0, Math.min(2, apellido.length()));
    }

    public static boolean validarPassword(String password){
        if (password.length() != 4) {
            return false;
        }
        return true;
    }

}
