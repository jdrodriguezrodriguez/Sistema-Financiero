package com.banco.sistemabancario.events;

import com.banco.sistemabancario.entity.Transaccion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailDepositoEvent {
    
    private Transaccion transaccion;
    private int idUser;
}
