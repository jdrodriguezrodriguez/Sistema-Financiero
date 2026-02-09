package com.banco.sistemabancario.repository.mailResetRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.mailReset.RegisterToken;

public interface RegisterTokenRepository extends JpaRepository <RegisterToken, Integer>{
    Optional<RegisterToken> findByToken(String token);
}
