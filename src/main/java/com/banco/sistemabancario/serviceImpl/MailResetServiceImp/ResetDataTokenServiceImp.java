package com.banco.sistemabancario.serviceImpl.MailResetServiceImp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.dto.MailReset.ResetPasswordTokenDto;
import com.banco.sistemabancario.dto.MailReset.ResetUsernameTokenDto;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.MailReset.PasswordResetToken;
import com.banco.sistemabancario.entity.MailReset.UsernameResetToken;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.repository.MailResetRepository.PasswordResetTokenRepository;
import com.banco.sistemabancario.repository.MailResetRepository.UsernameResetTokenRepository;
import com.banco.sistemabancario.service.EmailService;
import com.banco.sistemabancario.service.ResetDataTokenService;

import jakarta.transaction.Transactional;

@Service
public class ResetDataTokenServiceImp implements ResetDataTokenService {

    private static final Logger logger = LoggerFactory.getLogger(ResetDataTokenServiceImp.class);

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UsernameResetTokenRepository usernameResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    @Override
    public void almacenarTokenPassword(String email) {

        System.out.println(email);

        personaRepository.findByCorreo(email).ifPresentOrElse(
                persona -> {

                    logger.info("Correo encontrado: {}", persona.getCorreo());

                    Usuario usuario = usuarioRepository.findByPersona(persona);

                    if (usuario == null) {
                        logger.warn("No se encontró usuario para persona {}", persona.getIdPersona());
                        return;
                    }

                    PasswordResetToken token = new PasswordResetToken();

                    token.setToken(UUID.randomUUID().toString());
                    token.setUsuario(usuario);
                    token.setExpiracion(LocalDateTime.now().plusMinutes(5));
                    token.setUso(false);
                    token.setCreacion(generarFechaActual());

                    passwordResetTokenRepository.save(token);

                    try {
                        emailService.enviarResetPassword(
                                persona.getCorreo(),
                                token.getToken());
                    } catch (Exception e) {
                        logger.error("Error enviando email para {}", persona.getCorreo(), e);
                    }

                },
                () -> logger.error("Correo no encontrado."));
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordTokenDto pTokenDto) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(pTokenDto.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        if (resetToken.isUso()) {
            throw new RuntimeException("Token ya utilizado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(pTokenDto.getNewPassword());
        usuarioRepository.save(usuario);

        resetToken.setUso(true);

        passwordResetTokenRepository.save(resetToken);

        logger.info("Se reseteo correctamente la contraseña.");
    }

    @Transactional
    @Override
    public void almacenarTokenUsername(String email) {
        personaRepository.findByCorreo(email).ifPresent(persona -> {
            Usuario usuario = usuarioRepository.findByPersona(persona);

            UsernameResetToken tokenUsername = new UsernameResetToken();

            tokenUsername.setToken(UUID.randomUUID().toString());
            tokenUsername.setUsuario(usuario);
            tokenUsername.setExpiracion(LocalDateTime.now().plusMinutes(5));
            tokenUsername.setUso(false);
            tokenUsername.setCreacion(generarFechaActual());

            usernameResetTokenRepository.save(tokenUsername);

            emailService.enviarResetUsername(
                    email,
                    tokenUsername.getToken());
        });
    }

    @Transactional
    @Override
    public void resetUsername(ResetUsernameTokenDto sTokenDto) {
        UsernameResetToken resetToken = usernameResetTokenRepository.findByToken(sTokenDto.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }
        if (resetToken.isUso()) {
            throw new RuntimeException("Token ya utilizado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setUsername(sTokenDto.getNewUsername());
        usuarioRepository.save(usuario);

        resetToken.setUso(true);
        usernameResetTokenRepository.save(resetToken);

        logger.info("Se reseteo correctamente el usuario.");
    }

    public static String generarFechaActual() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ahora.format(formato);
    }
}
