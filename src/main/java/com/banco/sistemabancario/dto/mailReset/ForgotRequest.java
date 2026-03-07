package com.banco.sistemabancario.dto.mailReset;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotRequest {

    @Email
    @NotBlank(message = "El correo es obligatorio.")
    @Schema(description = "Correo del usuario", example = "juan@gmail.com")
    String email;
}
