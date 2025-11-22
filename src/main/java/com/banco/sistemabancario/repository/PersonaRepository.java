package com.banco.sistemabancario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;

public interface PersonaRepository extends JpaRepository<Persona, Integer>{
    Persona findByUsuario(Usuario usuario);
    Optional<Persona> findByDocumento(String documento);
    boolean existsByDocumento(String documento);
    boolean existsByCorreo(String correo);
} 