package com.banco.sistemabancario.service.MailResetService;

import com.banco.sistemabancario.dto.MailReset.ResetPasswordTokenDto;
import com.banco.sistemabancario.dto.MailReset.ResetUsernameTokenDto;

public interface ResetTokenService {
    void almacenarTokenPassword(String email);
    void resetPassword(ResetPasswordTokenDto pTokenDto);
    void almacenarTokenUsername(String email);
    void resetUsername(ResetUsernameTokenDto sTokenDto);
}
