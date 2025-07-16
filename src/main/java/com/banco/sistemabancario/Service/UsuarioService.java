package com.banco.sistemabancario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;


    //INICIAR SESION
    public Usuario autenticar(String username, String password){

        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }

    //REGISTRAR USUARIO
    public Usuario registrarUsuario(String nombre, String apellido, String password, Persona persona){

        String username = UsuarioService.GenerarUsername(nombre, apellido);
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
    public static String GenerarUsername(String nombre, String apellido){
        return nombre.substring(0, Math.min(4, nombre.length())) + apellido.substring(0, Math.min(2, apellido.length()));
    }
}
