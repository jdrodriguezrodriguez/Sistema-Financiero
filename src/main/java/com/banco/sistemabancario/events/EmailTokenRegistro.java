package com.banco.sistemabancario.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailTokenRegistro {
    
    private String nombreCompleto;
    private String email;
    private String token;
}
