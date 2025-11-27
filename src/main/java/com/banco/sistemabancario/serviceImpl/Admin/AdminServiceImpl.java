package com.banco.sistemabancario.serviceImpl.Admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.Admin.ActualizarUsuarioAdmin;
import com.banco.sistemabancario.dto.Admin.ConsultarUsuarioAdmin;
import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.CorreoYaRegistradoException;
import com.banco.sistemabancario.exception.DocumentoYaRegistradoException;
import com.banco.sistemabancario.exception.PersonaNoEncontradaException;
import com.banco.sistemabancario.exception.UsuarioNoencontradoException;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.service.PersonaService;
import com.banco.sistemabancario.service.UsuarioService;
import com.banco.sistemabancario.service.Admin.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    private PersonaRepository personaRepository;
    private UsuarioRepository usuarioRepository;
    private CuentaRepository cuentaRepository;

    private PersonaService personaService;
    private UsuarioService usuarioService;

    public AdminServiceImpl(PersonaRepository personaRepository, UsuarioRepository usuarioRepository,
            CuentaRepository cuentaRepository, PersonaService personaService, UsuarioService usuarioService) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
        this.personaService = personaService;
        this.usuarioService = usuarioService;

    }

    @Override
    public ConsultarUsuarioAdmin adminBuscarUsuario(String documento) {
        Persona persona = personaRepository.findByDocumento(documento)
                .orElseThrow(() -> new PersonaNoEncontradaException("No existe persona con ese documento"));

        Usuario usuario = usuarioRepository.findByPersona(persona);
        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);

        return new ConsultarUsuarioAdmin(
                persona.getNombre(),
                persona.getApellido(),
                persona.getDocumento(),
                persona.getCorreo(),
                usuario.getUsername(),
                usuario.getRol(),
                cuenta.getNum_cuenta(),
                cuenta.getEstado(),
                persona.getNacimiento());
    }

    @Transactional
    @Override
    public void adminActualizarUsuario(ActualizarUsuarioAdmin datos) {
        Persona persona = personaRepository.findByDocumento(datos.getDocumentoActual())
                .orElseThrow(() -> new PersonaNoEncontradaException("No existe persona con ese documento"));

        Usuario usuario = usuarioRepository.findByPersona(persona);
        if (usuario == null) {
            throw new UsuarioNoencontradoException("No existe un usuario asociado a esa persona");
        }

        if (!persona.getDocumento().equals(datos.getDocumentoNuevo())) {
            if (personaService.documentoYaRegistrado(datos.getDocumentoNuevo())) {
                throw new DocumentoYaRegistradoException(
                        "Ya existe una persona registrada con el documento: " + datos.getDocumentoNuevo());
            }
            persona.setDocumento(datos.getDocumentoNuevo());
        }

        if (!persona.getCorreo().equals(datos.getEmail())) {
            if (personaService.correoYaRegistrado(datos.getEmail())) {
                throw new CorreoYaRegistradoException(
                        "Ya existe una persona registrada con el correo electronico: " + datos.getEmail());
            }
            persona.setCorreo(datos.getEmail());
        }

        persona.setNombre(datos.getNombre());
        persona.setApellido(datos.getApellido());
        persona.setNacimiento(datos.getNacimiento());;

        if (!usuario.getUsername().equals(datos.getUsername())) {
            usuarioService.validarNombreUsuario(datos.getUsername(), usuario.getIdUsuario());
            usuario.setUsername(datos.getUsername());
        }
        
        usuario.setRol(datos.getRol());
    }
}
