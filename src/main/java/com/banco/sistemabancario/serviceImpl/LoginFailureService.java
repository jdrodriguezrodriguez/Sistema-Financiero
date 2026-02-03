package com.banco.sistemabancario.serviceImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.service.UsuarioService;

@Service
public class LoginFailureService {
    
    private static final Logger logger =  LoggerFactory.getLogger(LoginFailureService.class);
    private static final int INTENTOS_MAX = 3;

    Map<String, Integer> usersAuthenFailure = new ConcurrentHashMap<>();
    Integer bloqueo;

    private UsuarioService usuarioService;
    public LoginFailureService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public boolean agregarUsuario(String username) {
        usersAuthenFailure.merge(username, 1, Integer::sum);
        if (validarUsuario(username)) {
            return true;
        }
        return false;
    }

    public boolean validarUsuario(String username) {
        bloqueo = usersAuthenFailure.getOrDefault(username, 0);
        System.out.println("Usuario: " + username + " - " +  "id: " + bloqueo);

        if (bloqueo >= INTENTOS_MAX) {
            bloquearUsuario(username);
            return true;
        }
        return false;
    }

    public void bloquearUsuario(String username) {
        usersAuthenFailure.remove(username);
        usuarioService.BloqueoUserFailureAuthentication(username);

        logger.warn("Usuario {} bloqueado por multiples fallos de autenticación", username);
    }
}
