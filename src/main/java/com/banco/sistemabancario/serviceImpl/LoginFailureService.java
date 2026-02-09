package com.banco.sistemabancario.serviceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.failureAuthentication.SeguridadUsuario;
import com.banco.sistemabancario.repository.SeguridadUsuarioRepository;
import com.banco.sistemabancario.service.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class LoginFailureService {

    private static final Logger logger = LoggerFactory.getLogger(LoginFailureService.class);
    private static final int INTENTOS_MAX = 3;
    private static final Duration BLOQUEO_1 = Duration.ofMinutes(1);
    private static final Duration BLOQUEO_2 = Duration.ofMinutes(2);
    private static final Duration BLOQUEO_3 = Duration.ofHours(1);

    private UsuarioService usuarioService;
    private SeguridadUsuarioRepository seguridadUsuarioRepository;

    public LoginFailureService(UsuarioService usuarioService, SeguridadUsuarioRepository seguridadUsuarioRepository) {
        this.usuarioService = usuarioService;
        this.seguridadUsuarioRepository = seguridadUsuarioRepository;
    }

    @Transactional
    public boolean registrarFalloLogin(String username) {
        Usuario userFailerLogin = usuarioService.obtenerUsuarioPorUsername(username);
        return seguridadUsuarioRepository.findById(userFailerLogin.getIdUsuario())
                .map(existenteFailer -> {
                    if (validarFailerUsuario(existenteFailer, userFailerLogin)) {
                        return true;
                    }
                    existenteFailer.setFailed_attempts(existenteFailer.getFailed_attempts() + 1);
                    seguridadUsuarioRepository.save(existenteFailer);
                    return false;

                }).orElseGet(() -> {
                    Usuario newUserFailerLogin = usuarioService.obtenerUsuarioPorUsername(username);
                    SeguridadUsuario newSecurityFailer = new SeguridadUsuario(
                            newUserFailerLogin.getIdUsuario(),
                            1,
                            null,
                            null,
                            0,
                            0);

                    seguridadUsuarioRepository.save(newSecurityFailer);

                    return false;
                });
    };

    public boolean validarFailerUsuario(SeguridadUsuario userSecurityLogin, Usuario userFailerLogin) {
        if (userSecurityLogin.getFailed_attempts() + 1 >= INTENTOS_MAX) {
            BloqueoUserFailureAuthentication(userSecurityLogin, userFailerLogin.getUsername());
            return true;
        }
        return false;
    }

    // BLOQUEAR USUARIO TEMPORALMENTE
    @Transactional
    public void BloqueoUserFailureAuthentication(SeguridadUsuario userSecurityLogin, String username) {
        usuarioService.BloqueoUserFailureAuthentication(username);

        int Lock_count = 0;

        if (userSecurityLogin.getLock_count() != 3) {
            Lock_count = userSecurityLogin.getLock_count() + 1;
        } else {
            Lock_count = userSecurityLogin.getLock_count();
        }

        Duration duration = Lock_count == 1 ? BLOQUEO_1
                : Lock_count == 2 ? BLOQUEO_2 : BLOQUEO_3;

        userSecurityLogin.setFailed_attempts(3);
        userSecurityLogin.setLast_failed_at(LocalDateTime.now());
        userSecurityLogin.setLock_count(Lock_count);
        userSecurityLogin.setLocked_until(LocalDateTime.now().plus(duration));

        seguridadUsuarioRepository.save(userSecurityLogin);
    }

    // USUARIO LOGEADO CORRECTAMENTE
    @Transactional
    public void limpiarUsuarioFailer(String username) {
        Usuario userFailerLogin = usuarioService.obtenerUsuarioPorUsername(username);
        Optional<SeguridadUsuario> securityFailer = seguridadUsuarioRepository.findById(userFailerLogin.getIdUsuario());

        if (!securityFailer.isPresent() && securityFailer.get().getFailed_attempts() == 0 && securityFailer.get().getLock_count() == 0) {
            return;
        }

        securityFailer.get().setFailed_attempts(0);
        securityFailer.get().setLast_failed_at(securityFailer.get().getLast_failed_at()); // @CreationTimestamp
        securityFailer.get().setLock_count(0);
        securityFailer.get().setLocked_until(null);

        seguridadUsuarioRepository.save(securityFailer.get());
    }

    // TIEMPO BLOQUEO DE EXPIRACION COMPLETADO
    @Transactional
    public boolean DesbloqueoUserFailureAuthentication(String username) {
        Usuario userBlock = usuarioService.obtenerUsuarioPorUsername(username);
        SeguridadUsuario securityFailer = seguridadUsuarioRepository.findById(userBlock.getIdUsuario())
                .orElseThrow(() -> new IllegalStateException("Usuario sin problemas de login."));

        if (securityFailer.getLocked_until() != null && LocalDateTime.now().isAfter(securityFailer.getLocked_until())) {

            usuarioService.DesbloqueoUserFailureAuthentication(userBlock.getUsername());

            securityFailer.setFailed_attempts(0);
            //securityFailer.setLock_count(0);
            securityFailer.setLocked_until(null);

            seguridadUsuarioRepository.save(securityFailer);
            return true;
        }
        logger.warn("Usuario {} sigue bloqueado", userBlock.getUsername());
        return false;
    }
}
