package com.banco.sistemabancario.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.service.CuentaService;
import com.banco.sistemabancario.service.PersonaService;
import com.banco.sistemabancario.service.UsuarioApplicationService;
import com.banco.sistemabancario.service.UsuarioService;
import com.banco.sistemabancario.service.MailResetService.ResetTokenService;

@Service
public class UsuarioApplicationServiceImpl implements UsuarioApplicationService {

    private PersonaService personaService;
    private UsuarioService usuarioService;
    private CuentaService cuentaService;
    private ResetTokenService resetTokenService;

    public UsuarioApplicationServiceImpl(PersonaService personaService,
            UsuarioService usuarioService,
            CuentaService cuentaService,
            ResetTokenService resetTokenService) {
        this.personaService = personaService;
        this.usuarioService = usuarioService;
        this.cuentaService = cuentaService;
        this.resetTokenService = resetTokenService;
    }

    @Transactional
    @Override
    public void registrarPersona(RegistroPersonaDto rPersonaDto) {

        Persona persona = personaService.registrarPersona(rPersonaDto);

        Usuario usuario = usuarioService.registrarUsuario(
                rPersonaDto.getNombre(),
                rPersonaDto.getApellido(),
                rPersonaDto.getPassword(),
                persona);

        cuentaService.registrarCuenta(usuario);

        resetTokenService.almacenarTokenRegister(
                persona.getCorreo(),
                persona.getNombre() + " " + persona.getApellido(),
                usuario);
    }

    @Transactional
    @Override
    public void actualizarPersona(Integer idUser, ActualizarPersonaDto aPersonaDto) {
        Persona persona = usuarioService.obtenerPersonaPorUsuarioId(idUser);
        personaService.actualizarDatosPersona(
                aPersonaDto,
                persona);
    }

}
