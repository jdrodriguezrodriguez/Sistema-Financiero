package com.banco.sistemabancario.repository.MailResetRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.MailReset.UsernameResetToken;

public interface UsernameResetTokenRepository extends JpaRepository<UsernameResetToken,Integer>{
    Optional<UsernameResetToken> findByToken(String token);
}