package com.banco.sistemabancario.repository.MailResetRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.MailReset.RegisterToken;

public interface RegisterTokenRepository extends JpaRepository <RegisterToken, Integer>{
    Optional<RegisterToken> findByToken(String token);
}
