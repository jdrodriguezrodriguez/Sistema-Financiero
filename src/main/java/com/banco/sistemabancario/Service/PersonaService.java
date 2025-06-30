package com.banco.sistemabancario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Repository.PersonaRepository;

@Service
public class PersonaService {
    
    @Autowired
    private PersonaRepository personaRepository;

    public Persona buscarPorNombre(String nombre){
        PersonaRegistrada = personaRepository.findByNombre(nombre);
    }
}
