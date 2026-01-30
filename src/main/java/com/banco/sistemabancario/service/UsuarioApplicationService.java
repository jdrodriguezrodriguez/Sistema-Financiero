package com.banco.sistemabancario.service;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;

public interface UsuarioApplicationService {
    void registrarPersona(RegistroPersonaDto rPersonaDto);
    void actualizarPersona(Integer idUser, ActualizarPersonaDto aPersonaDto);
}
