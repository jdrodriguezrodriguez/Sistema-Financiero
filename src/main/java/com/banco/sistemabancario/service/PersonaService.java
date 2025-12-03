package com.banco.sistemabancario.service;

import java.util.List;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.entity.Persona;

public interface PersonaService {
    List<Persona> obtenerPersonas();
    Persona obtenerPersonaPorId(int idPersona);
    Persona obtenerPersonaPorDocumento(String documento);
    Persona actualizarDatosPersona(ActualizarPersonaDto actualizarPersonaDto, int idPersona);
    Persona registrarPersona(RegistroPersonaDto datos);
    void eliminarPersona(int idPersona);

    boolean documentoYaRegistrado(String documento);
    boolean correoYaRegistrado(String correo);
    void validarDatosRegistro(RegistroPersonaDto datos);
}
