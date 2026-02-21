package com.banco.sistemabancario.service;

import com.banco.sistemabancario.entity.failureAuthentication.AuthFailerContext;
import com.banco.sistemabancario.entity.failureAuthentication.SeguridadUsuario;

public interface LoginFailureService{

    boolean registrarFalloLogin(AuthFailerContext aFailerContext);
    boolean validarFailerUsuario(SeguridadUsuario userSecurityLogin, AuthFailerContext aFailerContext);
    void bloqueoUserFailureAuthentication(SeguridadUsuario userSecurityLogin, AuthFailerContext aFailerContext);
    void limpiarUsuarioFailer(String username);
    boolean desbloqueoUserFailureAuthentication(String username);
} 
