package com.banco.sistemabancario.serviceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.exception.*;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.service.PersonaService;
import com.banco.sistemabancario.serviceImpl.mailResetServiceImp.EmailServiceImpl;
import com.banco.sistemabancario.util.PersonaUtils;

@Service
public class PersonaServiceImpl implements PersonaService {

    private static final Logger logger =  LoggerFactory.getLogger(EmailServiceImpl.class);

    private PersonaRepository personaRepository;
    private PersonaUtils personaUtils;

    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public Persona obtenerPersonaPorNumeroCuenta(String numCuenta) {
        return personaRepository.findByUsuario_Cuenta_numCuenta(numCuenta)
                .orElseThrow(() -> new PersonaNoEncontradaException("No se encontro a la persona con el numero de cuenta: " + numCuenta));
    }

    @Override
    public Persona obtenerPersonaPorId(int idPersona) {
        return personaRepository.findById(idPersona)
                .orElseThrow(
                    () -> new PersonaNoEncontradaException("No se encontro a la persona con el ID: " + idPersona));
    }

    @Override
    public Persona obtenerPersonaPorDocumento(String documento) {
        return personaRepository.findByDocumento(documento)
                .orElseThrow(
                    () -> new PersonaNoEncontradaException("No existe persona con ese documento"));
    }

    @Override
    public List<Persona> obtenerPersonas() {
        return personaRepository.findAll();
    }

    @Transactional
    @Override
    public Persona actualizarDatosPersona(ActualizarPersonaDto actualizarPersonaDto, Persona persona) {

        persona.setNombre(actualizarPersonaDto.getNombre());
        persona.setApellido(actualizarPersonaDto.getApellido());
        persona.setCorreo(actualizarPersonaDto.getCorreo());
        persona.setNacimiento(Date.valueOf(LocalDate.parse(actualizarPersonaDto.getNacimiento())));

        return personaRepository.save(persona);
    }

    @Transactional
    @Override
    public Persona registrarPersona(RegistroPersonaDto datos) {
        validarDatosRegistro(datos);
        Persona persona = personaUtils.convertirAObjeto(datos);
        return  personaRepository.save(persona);
    }

    @Transactional
    public void eliminarPersona(int idPersona) {
        Persona persona = obtenerPersonaPorId(idPersona);
        logger.info("Se elimino la persona con id: ", idPersona);
        personaRepository.delete(persona);
    }

    @Override
    public boolean documentoYaRegistrado(String documento) {
        return personaRepository.existsByDocumento(documento);
    }

    @Override
    public boolean correoYaRegistrado(String correo) {
        return personaRepository.existsByCorreo(correo);
    }

    @Override
    public void validarDatosRegistro(RegistroPersonaDto datos) {
        if (documentoYaRegistrado(datos.getDocumento())) {
            throw new DocumentoYaRegistradoException(
                    "Documento ya registrado");
        }

        if (correoYaRegistrado(datos.getCorreo())) {
            throw new CorreoYaRegistradoException(
                    "Correo ya registrado");
        }
    }
}