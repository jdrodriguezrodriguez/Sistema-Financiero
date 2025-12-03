package com.banco.sistemabancario.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Roles;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.enums.RoleEnum;
import com.banco.sistemabancario.exception.PasswordInvalidaException;
import com.banco.sistemabancario.exception.UsuarioNoencontradoException;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.security.controller.CustomUserDetails;
import com.banco.sistemabancario.service.RolesService;
import com.banco.sistemabancario.service.UsuarioService;
import com.banco.sistemabancario.util.UsuarioUtils;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {
    
    private UsuarioRepository usuarioRepository;
    private PersonaRepository personaRepository;
    private RolesService rolesService;
    private UsuarioUtils usuarioUtils;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PersonaRepository personaRepository, RolesService rolesService) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.rolesService = rolesService;
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

    @Transactional
    @Override
    public Usuario actualizarDatosUsuario(ActualizarUsuarioDto datos, int idUsuario){
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new UsuarioNoencontradoException("No se encontro el usuario"));

        validarNombreUsuario(datos.getUsername(), usuario.getIdUsuario());

        usuario.setUsername(datos.getUsername());
        usuario.setPassword(datos.getPassword());

        return usuarioRepository.save(usuario);
    }

    //CONSULTAS
    @Override
    public List<Usuario> obtenerUsuarios(){
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorPersonaId(int idPersona){
        return usuarioRepository.findByPersona_IdPersona(idPersona);
    }

    @Override
    public Persona obtenerPersonaPorUsuarioId(int idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new UsuarioNoencontradoException("No se encontro el usuario con ID: " + idUsuario));

        return personaRepository.findByUsuario(usuario);
    }

    @Transactional
    @Override
    public Usuario registrarUsuario(String nombre, String apellido, String password, Persona persona){

        String username = usuarioUtils.generarUsername(nombre, apellido);
        validarContraseñaUsuario(password);
        
        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setPersona(persona);
        usuario.setRol(RoleEnum.CLIENTE);

        //SECURITY
        usuario.setAccountNoExpired(true);
        usuario.setAccountNoLocked(true);
        usuario.setCredentialNoExpired(true);
        usuario.setEnabled(true);

        //ROLES/PERMISOS
        Roles rol = rolesService.buscarRoles(RoleEnum.CLIENTE);
        usuario.setRoles(Set.of(rol));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public Usuario adminRegistrarUsuario(String username, String password, Persona persona, String rol, String permisos){

        Usuario usuario = new Usuario();
        
        validarNombreUsuario(username, 0);

        usuario.setUsername(username);

        validarContraseñaUsuario(password);
        
        usuario.setPassword(password);
        usuario.setPersona(persona);
        usuario.setRol(RoleEnum.valueOf(rol));
        

        usuario.setAccountNoExpired(true);
        usuario.setAccountNoLocked(true);
        usuario.setCredentialNoExpired(true);
        usuario.setEnabled(true);

        Roles roles = rolesService.buscarRoles(RoleEnum.valueOf(permisos));
        usuario.setRoles(Set.of(roles));

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

    public void validarContraseñaUsuario(String password){
        if (!usuarioUtils.validarPassword(password)) {
            throw new PasswordInvalidaException("La contraseña debe tener exactamente cuatro digitos.");
        }
    }
}
