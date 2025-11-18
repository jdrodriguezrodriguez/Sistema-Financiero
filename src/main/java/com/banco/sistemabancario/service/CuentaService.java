package com.banco.sistemabancario.service;

import java.math.BigDecimal;

import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Usuario;

public interface CuentaService {
    Cuenta registrarCuenta(Usuario usuario);
    String generarNumeroCuenta();
    Cuenta buscarCuentaPorIdUser(int idPersona);
    Cuenta buscarCuentaPorNumeroCuenta(String idCuenta);
    void aumentarSaldo(Cuenta depositoCuenta, BigDecimal monto);
    void descontarSaldo(Cuenta retiroCuenta, BigDecimal monto);
    void validarValoresTransferencia(Cuenta cuentaEntrada, Cuenta cuentaSalida, BigDecimal monto);
}
