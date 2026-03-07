package com.banco.sistemabancario.repository.mailResetRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.mailReset.ResetPasswordToken;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer>{
    Optional<ResetPasswordToken> findByToken(String token);
}
