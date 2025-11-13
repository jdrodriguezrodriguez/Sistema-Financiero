package com.banco.sistemabancario.serviceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.*;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.service.PersonaService;
import com.banco.sistemabancario.util.PersonaUtils;
import com.banco.sistemabancario.util.UsuarioUtils;

@Service
public class PersonaServiceImpl implements PersonaService{

    private PersonaRepository personaRepository;
    private UsuarioServiceImpl usuarioService;
    private CuentaServiceImpl cuentaService;
    private UsuarioUtils usuarioUtils;
    private PersonaUtils personaUtils;

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
    @Transactional
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
    @Transactional
    @Override
    public Persona registrarPersona(RegistroPersonaDto datos){
    
        validarDatosRegistro(datos);
        Persona persona =  personaUtils.convertirAObjeto(datos);
      
        Persona personaRegistro = personaRepository.save(persona);
        Usuario usuarioRegistro = usuarioService.registrarUsuario(datos.getNombre(), datos.getApellido(), datos.getPassword(), persona);
        cuentaService.registrarCuenta(usuarioRegistro);
        

        return personaRegistro;
    }

    //ELIMINAR PERSONA
    @Transactional
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

    //VALIDAR DATOS REGISTRO
    @Override
    public void validarDatosRegistro(RegistroPersonaDto datos){
        if (documentoYaRegistrado(datos.getDocumento())) {
            throw new DocumentoYaRegistradoException("Ya existe una persona registrada con el documento: " + datos.getDocumento());
        }

        if (correoYaRegistrado(datos.getCorreo())) {
            throw new CorreoYaRegistradoException("Ya existe una persona registrada con el correo electronico: " + datos.getCorreo());
        }

        if (!usuarioUtils.validarPassword(datos.getPassword())) {
            throw new PasswordInvalidaException("La contraseña debe tener exactamente cuatro digitos.");
        }
    }
}