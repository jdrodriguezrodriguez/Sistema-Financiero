package com.banco.sistemabancario.security.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.UsuarioNoRegistrado;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.security.controller.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoRegistrado("El usuario: " + username + " no existe"));

        // TOMAR ROLES y PERMISOS DE USUARIO PARA CONVERTIR A OBJETO DE SPRING SECURITY
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>(); // CREAR LISTA DE PERMISOS, YA QUE SPRING MANEJA
                                                                        // PERMISOS CON GRANTEDAUTHORITY Y ESTA ES UNA
                                                                        // DE SUS IMPLEMENTACIONES
        usuario.getRoles()
                .forEach(rol -> authorityList.add(
                        new SimpleGrantedAuthority("ROLE_".concat(rol.getRoleEnum().name())))); // TOMAMOS LOS ROLES Y
                                                                                                // LOS CONVERTIMOS A
                                                                                                // SimpleGrantedAuthority
                                                                                                // - PREFIJO ROLE_
                                                                                                // OBLIGATORIO
        usuario.getRoles().stream()
                .flatMap(rol -> rol.getPermisosList().stream())
                .forEach(permiso -> authorityList.add(new SimpleGrantedAuthority(permiso.getName()))); // AGREGAR CADA
                                                                                                       // PERMISO A CADA
                                                                                                       // ROL
        return new CustomUserDetails(usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.isEnabled(),
                usuario.isAccountNoExpired(),
                usuario.isCredentialNoExpired(),
                usuario.isAccountNoLocked(),
                authorityList);
    }
}
