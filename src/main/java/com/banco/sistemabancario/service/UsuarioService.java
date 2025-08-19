package com.banco.sistemabancario.service;

import org.springframework.stereotype.Service;
import java.util.Optional;

import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.PersonaNoEncontradaException;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    private UsuarioRepository usuarioRepository;
    private PersonaRepository personaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
    }

    //ACTUALIZAR USUARIO
    public Usuario actualizarUsuario(String username, String password, int idPersona){
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new PersonaNoEncontradaException("No se encontro a la persona con el ID: " + idPersona));
        Usuario usuario = usuarioRepository.findByPersona(persona);

        validarUsuario(username, usuario.getIdUsuario());

        usuario.setUsername(username);
        usuario.setPassword(password);

        return usuarioRepository.save(usuario);
    }

    //INICIAR SESION
    public Usuario autenticar(String username, String password){
        return usuarioRepository.findByUsername(username)
                .filter(e -> e.getPassword().equals(password))
                .orElse(null);
    }

    //REGISTRAR USUARIO
    public Usuario registrarUsuario(String nombre, String apellido, String password, Persona persona){

        String username = UsuarioService.generarUsername(nombre, apellido);
        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setPersona(persona);

        if (usuario.getRol() == null) {
            usuario.setRol("CLIENTE");
        }

        return usuarioRepository.save(usuario);
    }

    //GENERAR NOMBRE DE USUARIO
    public static String generarUsername(String nombre, String apellido){
        return nombre.substring(0, Math.min(4, nombre.length())) + apellido.substring(0, Math.min(2, apellido.length()));
    }

    //VALIDAR CONTRASEÑA
    public static boolean validarPassword(String password){
        if (password.length() != 4) {
            return false;
        }
        return true;
    }

    //VALIDAR NOMBRE PARA USUARIO
    public void validarUsuario(String username, int idActual){

        Optional<Usuario> existente = usuarioRepository.findByUsername(username);

        if (existente.isPresent() && !existente.get().getIdUsuario().equals(Integer.valueOf(idActual))) {
            throw new IllegalArgumentException("El usuario ya existe, cambiar username " + username);
        }
    }
}
