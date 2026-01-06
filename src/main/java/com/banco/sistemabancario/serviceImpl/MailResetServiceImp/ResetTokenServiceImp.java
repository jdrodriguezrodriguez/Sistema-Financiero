package com.banco.sistemabancario.serviceImpl.MailResetServiceImp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.dto.MailReset.ForgotRequest;
import com.banco.sistemabancario.dto.MailReset.ResetPasswordTokenDto;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.MailReset.ResetToken;
import com.banco.sistemabancario.exception.PasswordInvalidaException;
import com.banco.sistemabancario.exception.TokenExpiradoException;
import com.banco.sistemabancario.exception.TokenInvalidoException;
import com.banco.sistemabancario.exception.TokenUsadoException;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
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
    private EmailService emailService;

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
                () -> logger.error("Correo no encontrado."));
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
        usuario.setPassword(pTokenDto.getNewPassword());
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
}
