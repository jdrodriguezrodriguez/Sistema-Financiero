package com.banco.sistemabancario.dto;

public class LoginUsuarioDto {
    
    private String username;
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
