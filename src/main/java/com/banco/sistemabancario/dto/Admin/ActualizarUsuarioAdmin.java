package com.banco.sistemabancario.dto.Admin;

import java.sql.Date;
import com.banco.sistemabancario.entity.enums.RoleEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private RoleEnum rol;

    private Date nacimiento;

}
