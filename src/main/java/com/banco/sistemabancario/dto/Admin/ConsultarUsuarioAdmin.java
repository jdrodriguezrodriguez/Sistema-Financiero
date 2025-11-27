package com.banco.sistemabancario.dto.Admin;

import java.sql.Date;

import com.banco.sistemabancario.entity.enums.CuentaEnum;
import com.banco.sistemabancario.entity.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarUsuarioAdmin {

    private String nombre, apellido, documento, correo, username, numCuenta;
    private CuentaEnum estado;
    private RoleEnum rol;
    private Date nacimiento;

    public ConsultarUsuarioAdmin(String nombre, String apellido, String documento, String correo, String username, RoleEnum rol,
            String numCuenta, CuentaEnum estado, Date nacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.correo = correo;
        this.username = username;
        this.rol = rol;
        this.numCuenta = numCuenta;
        this.estado = estado;
        this.nacimiento = nacimiento;
    }
}
