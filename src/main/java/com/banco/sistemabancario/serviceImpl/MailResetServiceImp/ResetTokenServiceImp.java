package com.banco.sistemabancario.serviceImpl.MailResetServiceImp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.dto.MailReset.ForgotRequest;
import com.banco.sistemabancario.dto.MailReset.ResetPasswordTokenDto;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.MailReset.RegisterToken;
import com.banco.sistemabancario.entity.MailReset.ResetToken;
import com.banco.sistemabancario.exception.PasswordInvalidaException;
import com.banco.sistemabancario.exception.TokenExpiradoException;
import com.banco.sistemabancario.exception.TokenInvalidoException;
import com.banco.sistemabancario.exception.TokenUsadoException;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.repository.MailResetRepository.RegisterTokenRepository;
import com.banco.sistemabancario.repository.MailResetRepository.ResetTokenRepository;
import com.banco.sistemabancario.service.MailResetService.EmailService;
import com.banco.sistemabancario.service.MailResetService.ResetTokenService;

import jakarta.transaction.Transactional;

@Service
public class ResetTokenServiceImp implements ResetTokenService {

    private static final Logger logger = LoggerFactory.getLogger(ResetTokenServiceImp.class);

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    RegisterTokenRepository registerTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void almacenarTokenPassword(ForgotRequest request) {

        personaRepository.findByCorreo(request.getEmail()).ifPresentOrElse(
                persona -> {

                    Usuario usuario = usuarioRepository.findByPersona(persona);

                    if (usuario == null) {
                        logger.warn("No se encontró usuario para persona {}", persona.getIdPersona());
                        return;
                    }

                    ResetToken tokenPass = new ResetToken();

                    tokenPass.setToken(UUID.randomUUID().toString());
                    tokenPass.setUsuario(usuario);
                    tokenPass.setExpiracion(LocalDateTime.now().plusMinutes(5));
                    tokenPass.setUso(false);
                    tokenPass.setCreacion(generarFechaActual());

                    resetTokenRepository.save(tokenPass);

                    try {
                        emailService.enviarResetPassword(
                                persona.getCorreo(),
                                tokenPass.getToken());
                    } catch (Exception e) {
                        logger.error("Error enviando email para {}", persona.getCorreo(), e);
                    }

                },
                () -> logger.error("Correo no encontrado."));
    }

    @Transactional
    @Override
    public void almacenarTokenRegister(String email, String datos, Usuario usuario) {
        RegisterToken regToken = new RegisterToken();

        regToken.setToken(UUID.randomUUID().toString());
        regToken.setUsuario(usuario);
        regToken.setExpiracion(LocalDateTime.now().plusHours(24));
        regToken.setUso(false);

        registerTokenRepository.save(regToken);

        try {
            emailService.enviarTokenRegistro(
                    datos,
                    email,
                    regToken.getToken());
        } catch (Exception e) {
            logger.error("Error enviando email para {}", email, e);
        }
    }

    @Transactional
    @Override
    public void forgotUsernameUsuario(ForgotRequest request) {
        personaRepository.findByCorreo(request.getEmail()).ifPresentOrElse(

                persona -> {
                    Usuario usuario = usuarioRepository.findByPersona(persona);

                    if (usuario == null) {
                        logger.warn("No se encontró usuario para persona {}", persona.getIdPersona());
                        return;
                    }

                    try {
                        emailService.enviarUsername(
                                request.getEmail(),
                                usuario.getUsername());
                    } catch (Exception e) {
                        logger.error("Error enviando email para {}", persona.getCorreo(), e);
                    }
                },
                () -> logger.error("Solicitud de recuperacion recibida."));
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordTokenDto pTokenDto) {
        ResetToken resetToken = resetTokenRepository.findByToken(pTokenDto.getToken())
                .orElseThrow(() -> new TokenInvalidoException("Token invalido."));

        if (resetToken.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new TokenExpiradoException("Token expirado");
        }

        if (resetToken.isUso()) {
            throw new TokenUsadoException("Token ya utilizado");
        }

        if (!pTokenDto.getPassword().equals(pTokenDto.getNewPassword())) {
            throw new PasswordInvalidaException("Las contraseña no coindicen.");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(pTokenDto.getNewPassword()));
        usuarioRepository.save(usuario);

        resetToken.setUso(true);

        resetTokenRepository.save(resetToken);

        logger.info("Se reseteo correctamente la contraseña.");
    }

    public static String generarFechaActual() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ahora.format(formato);
    }

    @Override
    public void activarUsuario(String token) {

        RegisterToken registerToken = registerTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenInvalidoException("Token invalido"));

        if (registerToken.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new TokenExpiradoException("Token expirado");
        }

        if (registerToken.isUso()) {
            throw new TokenUsadoException("Token usado");
        }

        Usuario usuario = registerToken.getUsuario();

        usuario.setAccountNoLocked(true);
        usuarioRepository.save(usuario);

        registerToken.setUso(true);
        registerTokenRepository.save(registerToken);

        logger.info("Se activo correctamente el usuario.");
    }
}
