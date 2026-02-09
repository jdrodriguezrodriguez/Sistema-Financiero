package com.banco.sistemabancario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.failureAuthentication.SeguridadUsuario;

public interface SeguridadUsuarioRepository extends JpaRepository<SeguridadUsuario, Integer>{
    
}
