package com.banco.sistemabancario.dto.Admin;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearUsuarioAdmin {
    
    private String nombre, apellido, documento, correo, username, rol, fechaNacimiento;

    private String contraseña;

    private String permisos; //ROL SECURITY
}
