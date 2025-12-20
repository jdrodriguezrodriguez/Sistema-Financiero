package com.banco.sistemabancario.service;

public interface EmailService {
    void enviarResetPassword(String email, String token);
    void enviarResetUsername(String email, String token);
}
