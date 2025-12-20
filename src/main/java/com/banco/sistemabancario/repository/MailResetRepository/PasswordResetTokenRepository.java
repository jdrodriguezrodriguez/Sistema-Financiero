package com.banco.sistemabancario.repository.MailResetRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.MailReset.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer>{
    Optional<PasswordResetToken> findByToken(String token);
}
