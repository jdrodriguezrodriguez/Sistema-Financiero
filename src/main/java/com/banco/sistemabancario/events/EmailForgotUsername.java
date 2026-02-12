package com.banco.sistemabancario.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailForgotUsername {
    
    private String correo;
    private String username;
}
