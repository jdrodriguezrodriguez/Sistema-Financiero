package com.banco.sistemabancario.service;

import java.util.List;
import java.util.Optional;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;

public interface UsuarioService {
    Usuario actualizarDatosUsuario (ActualizarUsuarioDto datos, int idUsuario);
    List<Usuario> obtenerUsuarios();
    Usuario registrarUsuario(String nombre, String apellido, String password, Persona persona);
    Usuario adminRegistrarUsuario(String username, String password, Persona persona, String rol, String permisos);
    Optional<Usuario> obtenerUsuarioPorId(int idPersona);
    Persona obtenerPersonaPorUsuarioId(int idUsuario);

    void validarNombreUsuario(String username, int idActual);
} 