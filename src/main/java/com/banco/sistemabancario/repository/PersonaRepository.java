package com.banco.sistemabancario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;

public interface PersonaRepository extends JpaRepository<Persona, Integer>{
    Persona findByUsuario(Usuario usuario);
    boolean existsByDocumento(String documento);
    boolean existsByCorreo(String correo);
} 