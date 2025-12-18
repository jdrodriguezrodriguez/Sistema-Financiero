package com.banco.sistemabancario.service;

public interface PasswordResetTokenService {
    void almacenarTokenPassword(String email);
    void resetPassword(String token, String newPassword);
}
