package com.banco.sistemabancario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer>{
    Optional<PasswordResetToken> findByToken(String token);
}
