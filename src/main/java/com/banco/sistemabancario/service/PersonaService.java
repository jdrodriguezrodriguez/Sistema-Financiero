package com.banco.sistemabancario.service;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.CamposVaciosException;
import com.banco.sistemabancario.exception.CorreoYaRegistradoException;
import com.banco.sistemabancario.exception.DocumentoYaRegistradoException;
import com.banco.sistemabancario.exception.PasswordInvalidaException;
import com.banco.sistemabancario.exception.PersonaNoEncontradaException;
import com.banco.sistemabancario.repository.PersonaRepository;

@Service
public class PersonaService {
    
    private PersonaRepository personaRepository;
    private UsuarioService usuarioService;
    private CuentaService cuentaService;

    public PersonaService(PersonaRepository personaRepository, UsuarioService usuarioService, CuentaService cuentaService) {
        this.personaRepository = personaRepository;
        this.usuarioService = usuarioService;
        this.cuentaService = cuentaService;
    }

    //ACTUALIZAR PERSONA EXISTENTE
    public Persona actualizarPersona(ActualizarPersonaDto actualizarPersonaDto, int idPersona){

        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new PersonaNoEncontradaException("No se encontro a la persona con el ID: " + idPersona));
                
        persona.setNombre(actualizarPersonaDto.getNombre());
        persona.setApellido(actualizarPersonaDto.getApellido());
        persona.setCorreo(actualizarPersonaDto.getCorreo());
        persona.setNacimiento(Date.valueOf(LocalDate.parse(actualizarPersonaDto.getNacimiento())));

        return personaRepository.save(persona);
    }

    //REGISTRAR PERSONA
    public Persona registrarPersona(RegistroPersonaDto datos){
    
        validarDatosRegistro(datos);
        Persona persona = convertirAObjeto(datos);
      
        Persona personaRegistro = personaRepository.save(persona);
        Usuario usuarioRegistro = usuarioService.registrarUsuario(datos.getNombre(), datos.getApellido(), datos.getPassword(), persona);
        cuentaService.registrarCuenta(usuarioRegistro);

        return personaRegistro;
    }

    //DTO A PERSONA
    public Persona convertirAObjeto(RegistroPersonaDto datos){
        Persona persona = new Persona();
        persona.setNombre(datos.getNombre());
        persona.setApellido(datos.getApellido());
        persona.setDocumento(datos.getDocumento());
        persona.setNacimiento(Date.valueOf(LocalDate.parse(datos.getNacimiento())));
        persona.setCorreo(datos.getCorreo());
        return persona;
    }

    //VALIDAR REGISTRO DEL DOCUMENTO
    public boolean documentoYaRegistrado(String documento){
        return personaRepository.existsByDocumento(documento);
    }

    //VALIDAR REGISTRO DEL CORREO
    public boolean correoYaRegistrado(String correo){
        return personaRepository.existsByCorreo(correo);
    }

    //VALIDAR CAMPOS REGISTRO
    public static boolean camposRegistroValidos(RegistroPersonaDto datos){
        if (datos.getNombre().isEmpty() 
        || datos.getApellido().isEmpty() 
        || datos.getDocumento().isEmpty() 
        || datos.getNacimiento().isEmpty() 
        || datos.getCorreo().isEmpty() 
        || datos.getPassword().isEmpty()) {
            return false;
        }
        return true;
    }

    //VALIDAR DATOS REGISTRO
    public void validarDatosRegistro(RegistroPersonaDto datos){
        if (documentoYaRegistrado(datos.getDocumento())) {
            throw new DocumentoYaRegistradoException("Ya existe una persona registrada con el documento: " + datos.getDocumento());
        }

        if (correoYaRegistrado(datos.getCorreo())) {
            throw new CorreoYaRegistradoException("Ya existe una persona registrada con el correo electronico: " + datos.getCorreo());
        }

        if (!UsuarioService.validarPassword(datos.getPassword())) {
            throw new PasswordInvalidaException("La contraseña debe tener exactamente cuatro digitos.");
        }
    }
}