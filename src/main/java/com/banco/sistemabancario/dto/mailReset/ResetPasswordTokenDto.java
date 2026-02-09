package com.banco.sistemabancario.dto.mailReset;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordTokenDto {
    private String token;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    @NotBlank(message = "Necesita confirmar la contraseña.")
    private String newPassword;
}
