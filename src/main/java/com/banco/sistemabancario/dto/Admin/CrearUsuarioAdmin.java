package com.banco.sistemabancario.dto.Admin;

import java.time.LocalDate;

public class CrearUsuarioAdmin {
    
    private Long usuarioId;
    private String username;
    private String email;
    private String rol;

    private Long personaId;
    private String nombre;
    private String apellido;
    private String documento;
    private LocalDate nacimiento;

    private Boolean estado;
    private String numeroCuenta;
}
