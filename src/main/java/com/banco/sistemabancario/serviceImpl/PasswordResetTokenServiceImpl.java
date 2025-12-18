package com.banco.sistemabancario.serviceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.PasswordResetToken;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.repository.PasswordResetTokenRepository;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.service.EmailService;
import com.banco.sistemabancario.service.PasswordResetTokenService;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void almacenarTokenPassword(String email) {
        
        personaRepository.findByCorreo(email).ifPresent(persona ->{
            Usuario usuario = usuarioRepository.findByPersona(persona);
            PasswordResetToken passwordResetToken = new PasswordResetToken();

            passwordResetToken.setToken(UUID.randomUUID().toString());
            passwordResetToken.setUsuario(usuario);
            passwordResetToken.setExpiration(LocalDateTime.now().plusMinutes(2));

            passwordResetTokenRepository.save(passwordResetToken);

            emailService.enviarResetPassword(
                persona.getCorreo(), 
                passwordResetToken.getToken()
            );
        });;
    }

    @Override 
    public void resetPassword(String token, String newPassword){
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(newPassword);
        usuarioRepository.save(usuario);

        passwordResetTokenRepository.delete(resetToken);
    }
}
