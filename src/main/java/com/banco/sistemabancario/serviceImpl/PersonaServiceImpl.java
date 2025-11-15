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
import com.banco.sistemabancario.exception.*;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.service.PersonaService;
import com.banco.sistemabancario.util.PersonaUtils;
import com.banco.sistemabancario.util.UsuarioUtils;

@Service
public class PersonaServiceImpl implements PersonaService {

    private PersonaRepository personaRepository;
    private UsuarioUtils usuarioUtils;
    private PersonaUtils personaUtils;

    public PersonaServiceImpl(PersonaRepository personaRepository,
            UsuarioUtils usuarioUtils,
            PersonaUtils personaUtils) {

        this.personaRepository = personaRepository;
        this.usuarioUtils = usuarioUtils;
        this.personaUtils = personaUtils;
    }

    @Override
    public Persona obtenerPersonaPorId(int idPersona) {
        return personaRepository.findById(idPersona)
                .orElseThrow(
                        () -> new PersonaNoEncontradaException("No se encontro a la persona con el ID: " + idPersona));
    }

    @Override
    public List<Persona> obtenerPersonas() {
        return personaRepository.findAll();
    }

    // ACTUALIZAR PERSONA EXISTENTE
    @Transactional
    @Override
    public Persona actualizarDatosPersona(ActualizarPersonaDto actualizarPersonaDto, int idPersona) {

        Persona persona = obtenerPersonaPorId(idPersona);

        persona.setNombre(actualizarPersonaDto.getNombre());
        persona.setApellido(actualizarPersonaDto.getApellido());
        persona.setCorreo(actualizarPersonaDto.getCorreo());
        persona.setNacimiento(Date.valueOf(LocalDate.parse(actualizarPersonaDto.getNacimiento())));

        return personaRepository.save(persona);
    }

    // REGISTRAR PERSONA
    @Transactional
    @Override
    public Persona registrarPersona(RegistroPersonaDto datos) {
        validarDatosRegistro(datos);
        Persona persona = personaUtils.convertirAObjeto(datos);
        return  personaRepository.save(persona);
    }

    // ELIMINAR PERSONA
    @Transactional
    public void eliminarPersona(int idPersona) {
        Persona persona = obtenerPersonaPorId(idPersona);
        personaRepository.delete(persona);
    }

    // VALIDAR REGISTRO DEL DOCUMENTO
    @Override
    public boolean documentoYaRegistrado(String documento) {
        return personaRepository.existsByDocumento(documento);
    }

    // VALIDAR REGISTRO DEL CORREO
    @Override
    public boolean correoYaRegistrado(String correo) {
        return personaRepository.existsByCorreo(correo);
    }

    // VALIDAR DATOS REGISTRO
    @Override
    public void validarDatosRegistro(RegistroPersonaDto datos) {
        if (documentoYaRegistrado(datos.getDocumento())) {
            throw new DocumentoYaRegistradoException(
                    "Ya existe una persona registrada con el documento: " + datos.getDocumento());
        }

        if (correoYaRegistrado(datos.getCorreo())) {
            throw new CorreoYaRegistradoException(
                    "Ya existe una persona registrada con el correo electronico: " + datos.getCorreo());
        }

        if (!usuarioUtils.validarPassword(datos.getPassword())) {
            throw new PasswordInvalidaException("La contraseña debe tener exactamente cuatro digitos.");
        }
    }
}