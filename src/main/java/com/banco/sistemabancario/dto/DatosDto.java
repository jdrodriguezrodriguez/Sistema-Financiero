package com.banco.sistemabancario.dto;

import java.sql.Date;

import com.banco.sistemabancario.entity.enums.CuentaEnum;
import com.banco.sistemabancario.entity.enums.RoleEnum;

public class DatosDto {

    private String nombre, apellido, documento, correo, username, numCuenta;
    private CuentaEnum estado;
    private RoleEnum rol;
    private Date nacimiento;

    public DatosDto(){
    }

    public DatosDto(String nombre, String apellido, String documento, String correo, String username, RoleEnum rol,
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

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public RoleEnum getRol() {
        return rol;
    }
    public void setRol(RoleEnum rol) {
        this.rol = rol;
    }

    public String getNumCuenta() {
        return numCuenta;
    }
    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public CuentaEnum getEstado() {
        return estado;
    }
    public void setEstado(CuentaEnum estado) {
        this.estado = estado;
    }

    public Date getNacimiento() {
        return nacimiento;
    }
    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }  
}
