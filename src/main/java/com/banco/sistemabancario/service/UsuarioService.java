package com.banco.sistemabancario.service;

import java.util.List;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.dto.LoginUsuarioDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;

public interface UsuarioService {
    Usuario actualizarDatosUsuario (ActualizarUsuarioDto datos, int idPersona);
    List<Usuario> obtenerUsuarios();
    Usuario registrarUsuario(String nombre, String apellido, String password, Persona persona);
    Usuario autenticar(LoginUsuarioDto datos);
    
    void validarUsuario(String username, int idActual);
} 