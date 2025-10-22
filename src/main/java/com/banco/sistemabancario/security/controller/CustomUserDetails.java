package com.banco.sistemabancario.security.controller;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User{
    private Integer idUsuario;

    public CustomUserDetails(Integer idUsuario, String username, String password, 
                             boolean enabled, boolean accountNonExpired, 
                             boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities){

        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.idUsuario = idUsuario;
    }

    public Integer getId(){
        return idUsuario;
    }
}
