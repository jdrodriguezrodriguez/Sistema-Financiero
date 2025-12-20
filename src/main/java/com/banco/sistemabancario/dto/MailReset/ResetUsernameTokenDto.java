package com.banco.sistemabancario.dto.MailReset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetUsernameTokenDto {
    private String token;
    private String newUsername;
}
