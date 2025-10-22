package com.banco.sistemabancario.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.UsuarioNoencontradoException;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.security.controller.CustomUserDetails;
import com.banco.sistemabancario.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {
    
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

    private UsuarioRepository usuarioRepository;

    private PersonaRepository personaRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
    }       

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("El usuario: " + username + "no existe"));


        //TOMAR ROLES y PERMISOS DE USUARIO PARA CONVERTIR A OBJETO DE SPRING SECURITY
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();   //CREAR LISTA DE PERMISOS,  YA QUE SPRING MANEJA PERMISOS CON GRANTEDAUTHORITY Y ESTA ES UNA DE SUS IMPLEMENTACIONES

        usuario.getRoles()
            .forEach( rol -> 
                authorityList.add(
                    new SimpleGrantedAuthority("ROLE_" .concat(rol.getRoleEnum().name()))));      //TOMAMOS LOS ROLES Y LOS CONVERTIMOS A SimpleGrantedAuthority - PREFIJO ROLE_ OBLIGATORIO
                                                                                                       
        usuario.getRoles().stream()     
            .flatMap(rol -> rol.getPermisosList().stream())  
            .forEach(permiso -> 
                authorityList.add(new SimpleGrantedAuthority(permiso.getName())));  //AGREGAR CADA PERMISO A CADA ROL

        //CONSTRUCCION DEL OBJETO USERDETAILS DE SPRING SECURITY PARA AUTENTICAR

            return new CustomUserDetails(usuario.getIdUsuario(),
                usuario.getUsername(), 
                usuario.getPassword(), 
                usuario.isEnabled(),
                usuario.isAccountNoExpired(),
                usuario.isCredentialNoExpired(),
                usuario.isAccountNoLocked(),
                authorityList
                );
        }

    @Override
    public Usuario actualizarDatosUsuario(ActualizarUsuarioDto datos, int idPersona){
        Usuario usuario = obtenerUsuarioPorId(idPersona)
            .orElseThrow(() -> new UsuarioNoencontradoException("No se encontró el usuario con el ID"));

        validarNombreUsuario(datos.getUsername(), usuario.getIdUsuario());

        usuario.setUsername(datos.getUsername());
        usuario.setPassword(datos.getPassword());

        return usuario;
    }

    //CONSULTAS
    @Override
    public List<Usuario> obtenerUsuarios(){
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(int idPersona){
        Optional<Usuario> usuario = usuarioRepository.findByPersona_IdPersona(idPersona);

        if (!usuario.isPresent()) {
            throw new UsuarioNoencontradoException("No se encontro a la persona con el ID" + idPersona);
        }

        return usuario;
    }

    //REGISTRAR USUARIO
    @Override
    public Usuario registrarUsuario(String nombre, String apellido, String password, Persona persona){

        String username = UsuarioServiceImpl.generarUsername(nombre, apellido);
        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setPersona(persona);

        if (usuario.getRol() == null) {
            usuario.setRol("CLIENTE");
        }

        return usuarioRepository.save(usuario);
    }

    //VALIDAR QUE EL USERNAME NO EXISTA
    @Override
    public void validarNombreUsuario(String username, int idActual){

        Optional<Usuario> existente = usuarioRepository.findByUsername(username);

        if (existente.isPresent() && !existente.get().getIdUsuario().equals(Integer.valueOf(idActual))) {
            throw new IllegalArgumentException("El usuario ya existe, cambiar username " + username);
        }
    }
}
