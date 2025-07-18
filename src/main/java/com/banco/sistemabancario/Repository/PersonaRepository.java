package com.banco.sistemabancario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Entity.Usuario;

public interface PersonaRepository extends JpaRepository<Persona, Integer>{
    Persona save(Persona persona);
    Persona findByUsuario(Usuario usuario);
    boolean existsByDocumento(String documento);
    boolean existsByCorreo(String correo);
} 