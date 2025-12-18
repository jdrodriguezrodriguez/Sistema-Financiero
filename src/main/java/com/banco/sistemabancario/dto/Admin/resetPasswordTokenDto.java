package com.banco.sistemabancario.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class resetPasswordTokenDto {
    private String token;
    private String password;
}
