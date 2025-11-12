package com.banco.sistemabancario.serviceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.*;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.service.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService{

    private PersonaRepository personaRepository;
    private UsuarioServiceImpl usuarioService;
    private CuentaServiceImpl cuentaService;

    public PersonaServiceImpl(PersonaRepository personaRepository, UsuarioServiceImpl usuarioService, CuentaServiceImpl cuentaService) {
        this.personaRepository = personaRepository;
        this.usuarioService = usuarioService;
        this.cuentaService = cuentaService;
    }

    @Override
    public Optional<Persona> obtenerPersonaPorId(int idPersona){
        Optional<Persona> persona =  personaRepository.findById(idPersona);

        if (!persona.isPresent()) {
            throw new PersonaNoEncontradaException("No se encontro a la persona con el ID: " + idPersona);
        }
        return persona;
    }

    @Override
    public List<Persona> obtenerPersonas(){
        return personaRepository.findAll();
    }

    //ACTUALIZAR PERSONA EXISTENTE
    @Override
    public Persona actualizarDatosPersona(ActualizarPersonaDto actualizarPersonaDto, int idPersona){
        return obtenerPersonaPorId(idPersona)
            .map(p->{
                p.setNombre(actualizarPersonaDto.getNombre());
                p.setApellido(actualizarPersonaDto.getApellido());
                p.setCorreo(actualizarPersonaDto.getCorreo());
                p.setNacimiento(Date.valueOf(LocalDate.parse(actualizarPersonaDto.getNacimiento())));

                return personaRepository.save(p);
            }).orElseThrow(() -> new PersonaNoEncontradaException("No se encontro a la persona con el ID: " + idPersona));
    }

    //REGISTRAR PERSONA
    @Override
    public Persona registrarPersona(RegistroPersonaDto datos){
    
        validarDatosRegistro(datos);
        Persona persona = convertirAObjeto(datos);
      
        Persona personaRegistro = personaRepository.save(persona);
        Usuario usuarioRegistro = usuarioService.registrarUsuario(datos.getNombre(), datos.getApellido(), datos.getPassword(), persona);
        cuentaService.registrarCuenta(usuarioRegistro);
        

        return personaRegistro;
    }

    //ELIMINAR PERSONA
    public boolean eliminarPersona(int idPersona){
        try {

            if (!personaRepository.existsById(idPersona)) {
            throw new PersonaNoEncontradaException("No se encontro a la persona con el ID: " + idPersona);
            } 
            personaRepository.deleteById(idPersona);
            return true;
            
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo eliminar la persona.", e);
        }
    }

    //DTO A PERSONA
    @Override
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
    @Override
    public boolean documentoYaRegistrado(String documento){
        return personaRepository.existsByDocumento(documento);
    }

    //VALIDAR REGISTRO DEL CORREO
    @Override
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
    @Override
    public void validarDatosRegistro(RegistroPersonaDto datos){
        if (documentoYaRegistrado(datos.getDocumento())) {
            throw new DocumentoYaRegistradoException("Ya existe una persona registrada con el documento: " + datos.getDocumento());
        }

        if (correoYaRegistrado(datos.getCorreo())) {
            throw new CorreoYaRegistradoException("Ya existe una persona registrada con el correo electronico: " + datos.getCorreo());
        }

        if (!UsuarioServiceImpl.validarPassword(datos.getPassword())) {
            throw new PasswordInvalidaException("La contraseña debe tener exactamente cuatro digitos.");
        }
    }
}