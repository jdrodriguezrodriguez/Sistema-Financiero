package com.banco.sistemabancario.serviceImpl.Admin;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.dto.Admin.ActualizarEstadoAdmin;
import com.banco.sistemabancario.dto.Admin.ActualizarUsuarioAdmin;
import com.banco.sistemabancario.dto.Admin.ConsultarUsuarioAdmin;
import com.banco.sistemabancario.dto.Admin.CrearUsuarioAdmin;
import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.Events.AuditoriaEvent;
import com.banco.sistemabancario.entity.enums.AuditoriaActionEnums;
import com.banco.sistemabancario.entity.enums.CuentaEnum;
import com.banco.sistemabancario.entity.enums.TipoEnum;
import com.banco.sistemabancario.exception.CorreoYaRegistradoException;
import com.banco.sistemabancario.exception.CuentaNoEncontradaException;
import com.banco.sistemabancario.exception.DocumentoYaRegistradoException;
import com.banco.sistemabancario.exception.PersonaNoEncontradaException;
import com.banco.sistemabancario.exception.UsuarioNoencontradoException;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.security.service.AuditorProvider;
import com.banco.sistemabancario.service.CuentaService;
import com.banco.sistemabancario.service.PersonaService;
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

    private AuditorProvider auditorProvider;
    private ApplicationEventPublisher applicationEventPublisher;

    public AdminServiceImpl(PersonaRepository personaRepository, UsuarioRepository usuarioRepository,
            CuentaRepository cuentaRepository, PersonaService personaService, UsuarioService usuarioService,
            CuentaService cuentaService, ApplicationEventPublisher applicationEventPublisher,
            AuditorProvider auditorProvider) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
        this.personaService = personaService;
        this.usuarioService = usuarioService;
        this.cuentaService = cuentaService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.auditorProvider = auditorProvider;
    }

    @Override
    public ConsultarUsuarioAdmin adminBuscarUsuario(String documento) {
        Persona persona = personaService.obtenerPersonaPorDocumento(documento);

        Usuario usuario = usuarioService.obtenerUsuarioPorPersonaId(persona.getIdPersona())
                .orElseThrow(() -> new UsuarioNoencontradoException("No existe usuario para esa persona"));

        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);

        if (usuario.getRol().equals(TipoEnum.ADMIN)) {
            return new ConsultarUsuarioAdmin(
                    persona.getNombre(),
                    persona.getApellido(),
                    persona.getDocumento(),
                    persona.getCorreo(),
                    usuario.getUsername(),
                    usuario.getRol(),
                    "SIN CUENTA",
                    CuentaEnum.CERRADA,
                    persona.getNacimiento(),
                    usuario.isEnabled(),
                    usuario.isAccountNoLocked());
        }

        if (cuenta == null) {
            throw new CuentaNoEncontradaException(
                    "Usuario no ADMIN sin cuenta asociada. Usuario ID: " + usuario.getIdUsuario());
        }

        return new ConsultarUsuarioAdmin(
                persona.getNombre(),
                persona.getApellido(),
                persona.getDocumento(),
                persona.getCorreo(),
                usuario.getUsername(),
                usuario.getRol(),
                cuenta.getNum_cuenta(),
                cuenta.getEstado(),
                persona.getNacimiento(),
                usuario.isEnabled(),
                usuario.isAccountNoLocked());
    }

    @Transactional
    @Override
    public void adminActualizarUsuario(ActualizarUsuarioAdmin datos) {
        Persona persona = personaService.obtenerPersonaPorDocumento(datos.getDocumentoActual());

        Usuario usuario = usuarioService.obtenerUsuarioPorPersonaId(persona.getIdPersona())
                .orElseThrow(() -> new UsuarioNoencontradoException("No existe usuario para esa persona"));

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
        persona.setNacimiento(datos.getNacimiento());

        usuarioService.validarNombreUsuario(datos.getUsername(), usuario.getIdUsuario());
        usuario.setUsername(datos.getUsername());

        usuario.setRol(TipoEnum.valueOf(datos.getRol()));

        Map<String, Object> cambio = new HashMap<>();
        int contador = 0;

        if (!datos.getDocumentoActual().equals(datos.getDocumentoNuevo())) {
            cambio.put("documento", datos.getDocumentoNuevo());
            contador++;
        }
        if (!persona.getCorreo().equals(datos.getEmail())) {
            cambio.put("correo", datos.getEmail());
            contador++;
        }
        if (!usuario.getRol().name().equals(datos.getRol())) {
            cambio.put("rol", datos.getRol() + " CHANGE_ROLE");
            contador++;
        }
        if (contador == 0) {
            cambio.put("Actualizacion", "Datos terceros se cambiaron.");
        }

        applicationEventPublisher.publishEvent(
                new AuditoriaEvent(
                        AuditoriaActionEnums.UPDATE_USER,
                        auditorProvider.getCustomUserId(),
                        usuario.getIdUsuario(),
                        cambio));
    }

    @Transactional
    @Override
    public void adminCrearUsuario(CrearUsuarioAdmin datos) {

        RegistroPersonaDto personaDto = new RegistroPersonaDto(
                datos.getNombre(),
                datos.getApellido(),
                datos.getDocumento(),
                datos.getNacimiento(),
                datos.getCorreo(),
                datos.getPassword());

        Persona persona = personaService.registrarPersona(personaDto);

        Usuario usuario = usuarioService.adminRegistrarUsuario(
                datos.getUsername(),
                datos.getPassword(),
                persona,
                datos.getRol(),
                datos.getPermisos());

        if (usuario.getRol() != TipoEnum.ADMIN) {
            cuentaService.registrarCuenta(usuario);
        }

        Map<String, Object> cambio = new HashMap<>();

        cambio.put("persona", Map.of(
                "persona", persona.getNombre() + " " + persona.getApellido(),
                "documento", persona.getDocumento().substring(0, 3) + "***",
                "correo", persona.getCorreo()));

        applicationEventPublisher.publishEvent(
                new AuditoriaEvent(
                        AuditoriaActionEnums.CREATE_USER,
                        auditorProvider.getCustomUserId(),
                        usuario.getIdUsuario(),
                        cambio));
    }

    @Transactional
    @Override
    public void adminActualizarEstado(ActualizarEstadoAdmin datos) {

        Persona persona = personaService.obtenerPersonaPorDocumento(datos.getDocumento());

        Usuario usuario = usuarioService.obtenerUsuarioPorPersonaId(persona.getIdPersona())
                .orElseThrow(() -> new UsuarioNoencontradoException("No existe usuario para esa persona"));

        boolean estadoAnterior = usuario.isEnabled();
        boolean bloqueoAnterior = usuario.isAccountNoLocked();

        usuario.setAccountNoLocked(datos.isBloqueo());
        usuario.setEnabled(datos.isEstado());

        Map<String, Object> cambio = new HashMap<>();

        cambio.put("Bloqueo", datos.isBloqueo() ? "Desbloqueado" : "Bloqueado");
        cambio.put("Estado", datos.isEstado() ? "Habilitado" : "Deshabilitado");

        AuditoriaActionEnums tempEnum = null;

        if (datos.isEstado() != estadoAnterior) {
            if (datos.isEstado()) {
                tempEnum = AuditoriaActionEnums.ACTIVATE_USER;
            } else {
                tempEnum = AuditoriaActionEnums.DEACTIVATE_USER;
            }
        } else if (datos.isBloqueo() != bloqueoAnterior) {
            if (datos.isBloqueo()) {
                tempEnum = AuditoriaActionEnums.UNBLOCK_USER;
            } else {
                tempEnum = AuditoriaActionEnums.BLOCK_USER;
            }
        }

        applicationEventPublisher.publishEvent(
                new AuditoriaEvent(
                        tempEnum,
                        auditorProvider.getCustomUserId(),
                        usuario.getIdUsuario(),
                        cambio));

        usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public void adminEliminarUsuario(String documento) {
        Persona persona = personaRepository.findByDocumento(documento)
                .orElseThrow(() -> new PersonaNoEncontradaException("No existe persona con ese documento"));

        Usuario usuario = usuarioService.obtenerUsuarioPorPersonaId(persona.getIdPersona())
                .orElseThrow(() -> new UsuarioNoencontradoException("No existe usuario para esa persona"));

        Map<String, Object> cambio = new HashMap<>();

        cambio.put("persona", Map.of(
                "nombre", persona.getNombre() + " " + persona.getApellido(),
                "documento", persona.getDocumento().substring(0, 3) + "***"));

        personaRepository.delete(persona);

        applicationEventPublisher.publishEvent(
                new AuditoriaEvent(
                        AuditoriaActionEnums.DELETE_USER,
                        auditorProvider.getCustomUserId(),
                        usuario.getIdUsuario(),
                        cambio));
    }
}
