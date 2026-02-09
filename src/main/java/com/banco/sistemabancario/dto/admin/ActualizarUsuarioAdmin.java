package com.banco.sistemabancario.dto.admin;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioAdmin {

    private String nombre, apellido, documentoActual, documentoNuevo, email, username;

    private String rol;

    private Date nacimiento;

}
