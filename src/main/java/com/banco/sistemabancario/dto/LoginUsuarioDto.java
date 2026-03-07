package com.banco.sistemabancario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginUsuarioDto {

    @Schema(description = "Username del usuario", example = "juanrr23")
    private String username;

    @Schema(description = "Contraseña del usuario")
    private String password;
    
    public LoginUsuarioDto(String username, String password){
        this.username = username;
        this.password = password;
    }

	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}   
}
