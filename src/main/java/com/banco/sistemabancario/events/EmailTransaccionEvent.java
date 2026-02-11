package com.banco.sistemabancario.events;

import com.banco.sistemabancario.entity.Transaccion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailTransaccionEvent {
    
    private Transaccion transaccion;
    private String cuentaEmisor;
    private String cuentaReceptor;
}
