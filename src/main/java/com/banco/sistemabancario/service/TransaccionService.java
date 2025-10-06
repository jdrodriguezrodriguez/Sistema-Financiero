package com.banco.sistemabancario.service;

import java.math.BigDecimal;
import java.util.List;

import com.banco.sistemabancario.dto.TransferirDineroDto;
import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Transaccion;

public interface TransaccionService {
    Transaccion transferir(int idPersona, TransferirDineroDto datos);
    List<Transaccion> transacciones(int idPersona);
    Transaccion depositar(int idPersona, String valor);

    BigDecimal consultar(int idPersona);
    Transaccion crearTransaccion(Cuenta cuenta, String cuentaDestino, String tipo, BigDecimal monto, String descripcion);
}
