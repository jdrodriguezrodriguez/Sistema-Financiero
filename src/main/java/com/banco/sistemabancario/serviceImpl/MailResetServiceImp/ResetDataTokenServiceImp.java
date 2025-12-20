package com.banco.sistemabancario.serviceImpl.MailResetServiceImp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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

        personaRepository.findByCorreo(email).ifPresent(persona -> {
            Usuario usuario = usuarioRepository.findByPersona(persona);
            PasswordResetToken token = new PasswordResetToken();

            token.setToken(UUID.randomUUID().toString());
            token.setUsuario(usuario);
            token.setExpiracion(LocalDateTime.now().plusMinutes(5));
            token.setUso(false);
            token.setCreacion(generarFechaActual());

            passwordResetTokenRepository.save(token);

            emailService.enviarResetPassword(
                    persona.getCorreo(),
                    token.getToken());
        });
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
    }

    @Transactional
    @Override
    public void almacenarTokenUsername(String email) {
        personaRepository.findByCorreo(email).ifPresent(persona->{
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
                tokenUsername.getToken()
            );
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
    }

    public static String generarFechaActual() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ahora.format(formato);
    }
}
