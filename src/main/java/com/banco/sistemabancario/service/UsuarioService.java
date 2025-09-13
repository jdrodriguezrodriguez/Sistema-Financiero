package com.banco.sistemabancario.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.dto.LoginUsuarioDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.PersonaNoEncontradaException;
import com.banco.sistemabancario.exception.UsuarioNoRegistrado;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService{
    
    private UsuarioRepository usuarioRepository;
    private PersonaRepository personaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
    }

    /* NUEVA IMPLEMENTACION*/
    /* */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("El usuario: " + username + "no existe"));


        //TOMAR ROLES y PERMISOS DE USUARIO PARA CONVERTIR A OBJETO DE SPRING SECURITY

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();   //SPRING MANEJA PERMISOS CON GRANTEDAUTHORITY Y ESTA ES UNA DE SUS IMPLEMENTACIONES

        usuario.getRoles()
            .forEach( roles -> authorityList.add(new SimpleGrantedAuthority("ROLE_" .concat(roles.getRoleEnum().name()))));         //TOMAMOS LOS ROLES Y LOS CONVERTIMOS A SimpleGrantedAuthority 
                                                                                                                                    //(Permiso o autorizacion)

        usuario.getRoles().stream()       //RECORRER CADA ROL
            .flatMap(role -> role.getPermisosList().stream())  //ROL RECORRE CADA PERMISO
            .forEach(permiso -> authorityList.add(new SimpleGrantedAuthority(permiso.getName())));

            return new User(usuario.getUsername(), 
                usuario.getPassword(), 
                usuario.isEnabled(),
                usuario.isAccountNoExpired(),
                usuario.isCredentialNoExpired(),
                usuario.isAccountNoLocked(),
                authorityList
                );
        }       

    /* */
    /* */
    

    //ACTUALIZAR USUARIO
    public Usuario actualizarUsuario(ActualizarUsuarioDto datos, int idPersona){
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new PersonaNoEncontradaException("No se encontro a la persona con el ID: " + idPersona));
        Usuario usuario = usuarioRepository.findByPersona(persona);

        validarUsuario(datos.getUsername(), usuario.getIdUsuario());

        usuario.setUsername(datos.getUsername());
        usuario.setPassword(datos.getPassword());

        return usuarioRepository.save(usuario);
    }

    //INICIAR SESION
    public Usuario autenticar(LoginUsuarioDto datos){
        loadUserByUsername(datos.getUsername()); 

        return usuarioRepository.findByUsername(datos.getUsername())
                .filter(e -> e.getPassword().equals(datos.getPassword()))
                .orElseThrow(() -> new UsuarioNoRegistrado("El usuario ingresado no se encuentra registrado."));
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

    //VALIDAR QUE EL USERNAME NO EXISTA
    public void validarUsuario(String username, int idActual){

        Optional<Usuario> existente = usuarioRepository.findByUsername(username);

        if (existente.isPresent() && !existente.get().getIdUsuario().equals(Integer.valueOf(idActual))) {
            throw new IllegalArgumentException("El usuario ya existe, cambiar username " + username);
        }
    }
}
