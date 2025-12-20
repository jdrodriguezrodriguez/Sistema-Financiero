package com.banco.sistemabancario.service;

import com.banco.sistemabancario.dto.MailReset.ResetPasswordTokenDto;
import com.banco.sistemabancario.dto.MailReset.ResetUsernameTokenDto;

public interface ResetDataTokenService {
    void almacenarTokenPassword(String email);
    void resetPassword(ResetPasswordTokenDto pTokenDto);
    void almacenarTokenUsername(String email);
    void resetUsername(ResetUsernameTokenDto sTokenDto);
}
