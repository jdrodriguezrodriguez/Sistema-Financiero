package com.banco.sistemabancario.service.MailResetService;

public interface EmailService {
    void enviarResetPassword(String email, String token);
    void enviarResetUsername(String email, String token);
}
