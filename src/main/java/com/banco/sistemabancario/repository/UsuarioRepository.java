package com.banco.sistemabancario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByPersona_IdPersona(int idPersona);
    Usuario findByPersona(Persona persona);
} 
