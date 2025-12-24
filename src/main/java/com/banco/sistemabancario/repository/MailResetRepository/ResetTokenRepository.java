package com.banco.sistemabancario.repository.MailResetRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.MailReset.ResetToken;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Integer>{
    Optional<ResetToken> findByToken(String token);
}
