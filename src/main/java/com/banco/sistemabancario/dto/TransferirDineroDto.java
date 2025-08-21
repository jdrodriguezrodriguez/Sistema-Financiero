package com.banco.sistemabancario.dto;

import jakarta.validation.constraints.NotBlank;

public class TransferirDineroDto {

    @NotBlank(message = "La cuenta destino es obligatoria.")
    private String cuentaDestino;

    @NotBlank(message = "El valor es obligatorio.")
    private String valor;
    
    private String descripcion;

	public TransferirDineroDto(String cuentaDestino, String valor, String descripcion) {
		this.cuentaDestino = cuentaDestino;
        this.valor = valor;
		this.descripcion = descripcion;
	}

    public String getCuentaDestino() {
		return cuentaDestino;
	}
	public String getValor() {
		return valor;
	}
	public String getDescripcion() {
		return descripcion;
	}
}
