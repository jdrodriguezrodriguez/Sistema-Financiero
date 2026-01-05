package com.banco.sistemabancario.service.MailResetService;

import com.banco.sistemabancario.dto.MailReset.ForgotRequest;
import com.banco.sistemabancario.dto.MailReset.ResetPasswordTokenDto;

public interface ResetTokenService {
    void almacenarTokenPassword(ForgotRequest request);
    void resetPassword(ResetPasswordTokenDto pTokenDto);
    void forgotUsernameUsuario(ForgotRequest request);
}
