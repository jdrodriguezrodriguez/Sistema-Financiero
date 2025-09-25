package com.banco.sistemabancario.dto;

import jakarta.validation.constraints.NotBlank;

public class ActualizarUsuarioDto {
    
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String username;
    
    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;
    
    public ActualizarUsuarioDto(String username, String password){
        this.password = password;
        this.username = username;
    }

	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}
