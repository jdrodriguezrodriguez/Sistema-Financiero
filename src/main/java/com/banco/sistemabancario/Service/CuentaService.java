package com.banco.sistemabancario.Service;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Entity.Cuenta;
import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Repository.CuentaRepository;

@Service
public class CuentaService {

    private static final String ESTADO_CUENTA = "ACTIVA";
    private final Random random = new Random();

    @Autowired
    CuentaRepository cuentaRepository;

    //REGISTRAR CUENTA
    public Cuenta registrarCuenta(Usuario usuario){

        Cuenta cuenta = new Cuenta();

        cuenta.setNum_cuenta(generarNumeroCuenta());
        cuenta.setUsuario(usuario);
        cuenta.setEstado(ESTADO_CUENTA);
        cuenta.setSaldo((new BigDecimal("0.00")));

        return cuentaRepository.save(cuenta);
    }

    //GENERAR NUMERO DE CUENTA
    public String generarNumeroCuenta(){

        String numeroCuenta;

        do{
            int numero = 1000000000 + random.nextInt(900000000);
            numeroCuenta = String.valueOf(numero);
        }while(cuentaRepository.existsByNumCuenta(numeroCuenta));

        return numeroCuenta;
    }
}
