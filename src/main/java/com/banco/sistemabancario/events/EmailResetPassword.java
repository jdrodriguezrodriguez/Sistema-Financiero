package com.banco.sistemabancario.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailResetPassword {
    
    private String correo;
    private String token;
}
