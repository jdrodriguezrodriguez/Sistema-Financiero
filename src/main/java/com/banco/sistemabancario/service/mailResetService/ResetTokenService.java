package com.banco.sistemabancario.service.mailResetService;

import com.banco.sistemabancario.dto.mailReset.ForgotRequest;
import com.banco.sistemabancario.dto.mailReset.ResetPasswordTokenDto;
import com.banco.sistemabancario.entity.Usuario;

public interface ResetTokenService {
    void almacenarTokenPassword(ForgotRequest request);
    void almacenarTokenRegister(String email, String datos, Usuario usuario);

    void resetPassword(ResetPasswordTokenDto pTokenDto);
    void activarUsuario(String token);
    void forgotUsernameUsuario(ForgotRequest request);
}
