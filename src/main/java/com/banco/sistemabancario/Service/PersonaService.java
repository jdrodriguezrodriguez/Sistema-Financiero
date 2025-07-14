package com.banco.sistemabancario.Service;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Repository.PersonaRepository;

@Service
public class PersonaService {
    
    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CuentaService cuentaService;

    //REGISTRAR PERSONA
    public Persona registrarPersona(String nombre, String apellido, String documento, String nacimiento, String correo, String password){
        
        if (!ValidarRegistro(nombre, apellido, documento, nacimiento, correo, password)) {
            return null;
        }

        Persona persona = new Persona();
        LocalDate fechanacimiento = LocalDate.parse(nacimiento);

        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setDocumento(documento);
        persona.setNacimiento(Date.valueOf(fechanacimiento));
        persona.setCorreo(correo);
      
        Persona personaRegistro = personaRepository.save(persona);
        if (personaRegistro == null) {
            System.out.println("No se logro registrar a la persona");
            return null;
        }

        Usuario usuarioRegistro = usuarioService.registrarUsuario(nombre, apellido, password, persona);
        if (usuarioRegistro == null) {
            System.out.println("No se logro registrar el usuario");
            return null;
        }

        cuentaService.registrarCuenta(usuarioRegistro);

        return personaRegistro;
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
