package com.banco.sistemabancario.serviceImpl;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.service.CuentaService;

@Service
public class CuentaServiceImpl implements CuentaService{

    private static final String ESTADO_CUENTA = "ACTIVA";
    private final Random random = new Random();

    private CuentaRepository cuentaRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    // REGISTRAR CUENTA
    @Override
    public Cuenta registrarCuenta(Usuario usuario) {

        Cuenta cuenta = new Cuenta();

        cuenta.setNum_cuenta(generarNumeroCuenta());
        cuenta.setUsuario(usuario);
        cuenta.setEstado(ESTADO_CUENTA);
        cuenta.setSaldo((new BigDecimal("0.00")));

        return cuentaRepository.save(cuenta);
    }

    // GENERAR NUMERO DE CUENTA
    @Override
    public String generarNumeroCuenta(){

        String numeroCuenta;

        do {
            int numero = 1000000000 + random.nextInt(900000000);
            numeroCuenta = String.valueOf(numero);
        } while (cuentaRepository.existsByNumCuenta(numeroCuenta));

        return numeroCuenta;
    }

    // BUSCAR CUENTA
    @Override
    public Cuenta buscarCuenta(int idUser) {
        Cuenta cuenta = cuentaRepository.findByUsuario_IdUsuario(idUser);
        return cuenta;
    }
}
