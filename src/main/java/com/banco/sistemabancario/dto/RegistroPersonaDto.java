package com.banco.sistemabancario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroPersonaDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellid oes obligatorio")
    private String apellido;

    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    @NotBlank(message =  "La fecha de nacimiento es obligatoria")
    private String nacimiento;

    @Email(message = "Correo invalido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 4, message = "La contraseña debe tener cuatro caracteres")
    private String password;

    public RegistroPersonaDto(String nombre, String apellido, String documento, String nacimiento, String correo,
            String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.nacimiento = nacimiento;
        this.correo = correo;
        this.password = password;
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

    public String getNacimiento() {
        return nacimiento;
    }
    public void setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
