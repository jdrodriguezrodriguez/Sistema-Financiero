package com.banco.sistemabancario.service;

import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Usuario;

public interface CuentaService {
    Cuenta registrarCuenta(Usuario usuario);
    String generarNumeroCuenta();
    Cuenta buscarCuenta(int idPersona);
}
