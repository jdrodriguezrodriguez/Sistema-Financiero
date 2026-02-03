package com.banco.sistemabancario.util;

import java.util.Scanner;

import com.banco.sistemabancario.entity.Usuario;

public final class UsuarioUtils {

    private UsuarioUtils() {
    }

    public static String generarUsername(String nombre, String apellido) {
        return nombre.substring(0, Math.min(4, nombre.length()))
                + apellido.substring(0, Math.min(2, apellido.length()));
    }

    public static boolean validarPassword(String password) {
        return password != null && password.length() == 4;
    }
}
