package com.banco.sistemabancario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Repository.PersonaRepository;

@Service
public class PersonaService {
    
    @Autowired
    private PersonaRepository personaRepository;

    public boolean Login(String nombre, String contraseña){

        Persona persona = personaRepository.findByNombre(nombre);

        if (persona != null && persona.getDocumento().equals(contraseña)) {
            return true;
        }
        return false;
    }

    public Persona registrar(Persona persona){
        return personaRepository.save(persona);
    }
}
