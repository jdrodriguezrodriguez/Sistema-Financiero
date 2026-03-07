package com.banco.sistemabancario.serviceImpl.mailResetServiceImp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.dto.mailReset.ForgotRequest;
import com.banco.sistemabancario.dto.mailReset.ResetPasswordTokenDto;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.mailReset.RegisterToken;
import com.banco.sistemabancario.entity.mailReset.ResetPasswordToken;
import com.banco.sistemabancario.events.EmailForgotUsername;
import com.banco.sistemabancario.events.EmailResetPassword;
import com.banco.sistemabancario.events.EmailTokenRegistro;
import com.banco.sistemabancario.exception.PasswordInvalidaException;
import com.banco.sistemabancario.exception.TokenExpiradoException;
import com.banco.sistemabancario.exception.TokenInvalidoException;
import com.banco.sistemabancario.exception.TokenUsadoException;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.repository.mailResetRepository.RegisterTokenRepository;
import com.banco.sistemabancario.repository.mailResetRepository.ResetPasswordTokenRepository;
import com.banco.sistemabancario.service.mailResetService.ResetTokenService;

import jakarta.transaction.Transactional;

@Service
public class ResetTokenServiceImp implements ResetTokenService {

    private static final Logger logger = LoggerFactory.getLogger(ResetTokenServiceImp.class);

    private PersonaRepository personaRepository;
    private UsuarioRepository usuarioRepository;
    private ResetPasswordTokenRepository resetPasswordTokenRepository;
    private RegisterTokenRepository registerTokenRepository;

    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher applicationEventPublisher;

    public ResetTokenServiceImp(PersonaRepository personaRepository, UsuarioRepository usuarioRepository,
            ResetPasswordTokenRepository resetPasswordTokenRepository, RegisterTokenRepository registerTokenRepository,
            PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.registerTokenRepository = registerTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    @Override
    public void almacenarTokenResetPassword(ForgotRequest request) {

        personaRepository.findByCorreo(request.getEmail()).ifPresentOrElse(
                persona -> {

                    Usuario usuario = usuarioRepository.findByPersona(persona);

                    if (usuario == null) {
                        logger.warn("No se encontró usuario para persona {}", persona.getIdPersona());
                        return;
                    }

                    ResetPasswordToken tokenPass = new ResetPasswordToken();

                    tokenPass.setToken(UUID.randomUUID().toString());
                    tokenPass.setUsuario(usuario);
                    tokenPass.setExpiracion(LocalDateTime.now().plusMinutes(5));
                    tokenPass.setUso(false);
                    tokenPass.setCreacion(generarFechaActual());

                    resetPasswordTokenRepository.save(tokenPass);

                    applicationEventPublisher.publishEvent(
                            new EmailResetPassword(persona.getCorreo(), tokenPass.getToken()));
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

        applicationEventPublisher.publishEvent(
                new EmailTokenRegistro(
                        datos, email, regToken.getToken()));
    }

    @Override
    public void forgotUsernameUsuario(ForgotRequest request) {
        personaRepository.findByCorreo(request.getEmail()).ifPresentOrElse(

                persona -> {
                    Usuario usuario = usuarioRepository.findByPersona(persona);

                    if (usuario == null) {
                        logger.warn("No se encontró usuario para persona {}", persona.getIdPersona());
                        return;
                    }

                    applicationEventPublisher.publishEvent(
                            new EmailForgotUsername(request.getEmail(), usuario.getUsername()));
                },
                () -> logger.error("Error con solicitud de recuperacion."));
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordTokenDto pTokenDto) {
        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByToken(pTokenDto.getToken())
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

        resetPasswordTokenRepository.save(resetToken);

        logger.info("Se reseteo correctamente la contraseña.");
    }

    public static String generarFechaActual() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ahora.format(formato);
    }

    @Transactional
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
