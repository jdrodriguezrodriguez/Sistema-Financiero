package com.banco.sistemabancario.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;

public interface UsuarioService {
    Usuario actualizarDatosUsuario (ActualizarUsuarioDto datos, int idPersona);
    List<Usuario> obtenerUsuarios();
    Usuario registrarUsuario(String nombre, String apellido, String password, Persona persona);
    Optional<Usuario> obtenerUsuarioPorId(int idPersona);

    void validarNombreUsuario(String username, int idActual);
} 