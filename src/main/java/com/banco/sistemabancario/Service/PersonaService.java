package com.banco.sistemabancario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Repository.PersonaRepository;

@Service
public class PersonaService {
    
    @Autowired
    private PersonaRepository personaRepository;

    //REGISTRAR PERSONA
    public Persona registrar(Persona persona){
        return personaRepository.save(persona);
    }

    //VALIDAR CAMPOS REGISTRO
    public static boolean ValidarRegistro(String nombre, String apellido, String documento, String nacimiento, String correo, String password){
        if (nombre.isEmpty() && documento.isEmpty() && documento.isEmpty() && nacimiento.isEmpty() && correo.isEmpty() && password.isEmpty()) {
            System.out.println("Complete los campos vacios");
            return false;
        }

        return true;
    }
}
