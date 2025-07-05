package com.banco.sistemabancario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.Entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Integer>{
    Persona findByNombre(String nombre);
    Persona save(Persona persona);

} 