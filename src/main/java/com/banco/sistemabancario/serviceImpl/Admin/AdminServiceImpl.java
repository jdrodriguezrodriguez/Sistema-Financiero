package com.banco.sistemabancario.serviceImpl.Admin;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.dto.Admin.ActualizarEstadoAdmin;
import com.banco.sistemabancario.dto.Admin.ActualizarUsuarioAdmin;
import com.banco.sistemabancario.dto.Admin.ConsultarUsuarioAdmin;
import com.banco.sistemabancario.dto.Admin.CrearUsuarioAdmin;
import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Roles;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.enums.RoleEnum;
import com.banco.sistemabancario.exception.CorreoYaRegistradoException;
import com.banco.sistemabancario.exception.DocumentoYaRegistradoException;
import com.banco.sistemabancario.exception.PersonaNoEncontradaException;
import com.banco.sistemabancario.exception.UsuarioNoencontradoException;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.service.CuentaService;
import com.banco.sistemabancario.service.PersonaService;
import com.banco.sistemabancario.service.RolesService;
import com.banco.sistemabancario.service.UsuarioService;
import com.banco.sistemabancario.service.Admin.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    private PersonaRepository personaRepository;
    private UsuarioRepository usuarioRepository;
    private CuentaRepository cuentaRepository;
    private CuentaService cuentaService;

    private PersonaService personaService;
    private UsuarioService usuarioService;

    public AdminServiceImpl(PersonaRepository personaRepository, UsuarioRepository usuarioRepository,
            CuentaRepository cuentaRepository, PersonaService personaService, UsuarioService usuarioService,
            CuentaService cuentaService) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
        this.personaService = personaService;
        this.usuarioService = usuarioService;
        this.cuentaService = cuentaService;

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

        usuarioService.validarNombreUsuario(datos.getUsername(), usuario.getIdUsuario());
        usuario.setUsername(datos.getUsername());
    
        usuario.setRol(RoleEnum.valueOf(datos.getRol()));
    }

    @Transactional
    @Override
    public void adminCrearUsuario(CrearUsuarioAdmin datos) {

        RegistroPersonaDto personaDto = new RegistroPersonaDto(
            datos.getNombre(), 
            datos.getApellido(), 
            datos.getDocumento(), 
            datos.getFechaNacimiento(),
            datos.getCorreo(), datos.getContraseña());
        
        Persona persona = personaService.registrarPersona(personaDto);

        Usuario usuario = usuarioService.adminRegistrarUsuario(
            datos.getUsername(), 
            datos.getContraseña(), 
            persona, 
            datos.getRol(),
            datos.getPermisos());

        if (usuario.getRol() != RoleEnum.EMPLEADO && usuario.getRol() != RoleEnum.ADMIN) {
            cuentaService.registrarCuenta(usuario);
        }
    }

    @Transactional
    @Override
    public void adminActualizarEstado(ActualizarEstadoAdmin datos) {

        Persona persona = personaRepository.findByDocumento(datos.getDocumento())
                .orElseThrow(() -> new PersonaNoEncontradaException("No existe persona con ese documento"));

        Usuario usuario = usuarioRepository.findByPersona(persona);
        if (usuario == null) {
            throw new UsuarioNoencontradoException("No existe un usuario asociado a esa persona");
        }

        usuario.setAccountNoLocked(datos.isEstado());
        usuario.setEnabled(datos.isEstado());

        usuarioRepository.save(usuario);
    }

    @Override
    public void adminEliminarUsuario(String documento) {
        Persona persona = personaRepository.findByDocumento(documento)
                .orElseThrow(() -> new PersonaNoEncontradaException("No existe persona con ese documento"));

        personaRepository.delete(persona);
    }
}
