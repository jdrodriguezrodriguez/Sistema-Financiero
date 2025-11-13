package com.banco.sistemabancario.util;

import java.sql.Date;
import java.time.LocalDate;

import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.entity.Persona;

public final class PersonaUtils {
    private PersonaUtils() {
    }

    public static boolean camposRegistroValidos(RegistroPersonaDto datos) {
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

    //DTO A PERSONA
    public Persona convertirAObjeto(RegistroPersonaDto datos) {

        try {
            Persona persona = new Persona();
            persona.setNombre(datos.getNombre());
            persona.setApellido(datos.getApellido());
            persona.setDocumento(datos.getDocumento());
            persona.setNacimiento(Date.valueOf(LocalDate.parse(datos.getNacimiento())));
            persona.setCorreo(datos.getCorreo());
            return persona;
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Se esperaba yyyy-MM-dd");
        }

    }
}
