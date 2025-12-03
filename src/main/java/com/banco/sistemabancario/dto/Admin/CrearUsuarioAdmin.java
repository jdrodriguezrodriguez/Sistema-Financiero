package com.banco.sistemabancario.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearUsuarioAdmin {
    
    private String nombre, apellido, documento, correo, username, rol, nacimiento;

    private String password;

    private String permisos; //ROL SECURITY
}
