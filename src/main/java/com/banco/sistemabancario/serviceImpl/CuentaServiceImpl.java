package com.banco.sistemabancario.serviceImpl;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.enums.CuentaEnum;
import com.banco.sistemabancario.exception.CuentaDeshabilitadaException;
import com.banco.sistemabancario.exception.CuentaNoEncontradaException;
import com.banco.sistemabancario.exception.SaldoInsuficienteException;
import com.banco.sistemabancario.exception.ValorInvalidoException;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.service.CuentaService;

@Service
public class CuentaServiceImpl implements CuentaService{

    private final Random random = new Random();
    private CuentaRepository cuentaRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional
    @Override
    public Cuenta registrarCuenta(Usuario usuario) {

        Cuenta cuenta = new Cuenta();

        cuenta.setNum_cuenta(generarNumeroCuenta());
        cuenta.setUsuario(usuario);
        cuenta.setEstado(CuentaEnum.ACTIVA);
        cuenta.setSaldo((new BigDecimal("0.00")));

        return cuentaRepository.save(cuenta);
    }

    @Override
    public String generarNumeroCuenta(){

        String numeroCuenta;

        do {
            int numero = 1000000000 + random.nextInt(900000000);
            numeroCuenta = String.valueOf(numero);
        } while (cuentaRepository.existsByNumCuenta(numeroCuenta));

        return numeroCuenta;
    }

    @Override
    public Cuenta buscarCuentaPorIdUser(int idUser) {
        Cuenta cuenta = cuentaRepository.findByUsuario_IdUsuario(idUser);
        return cuenta;
    }

    @Override
    public Cuenta buscarCuentaPorNumeroCuenta(String idCuenta){
        return cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new CuentaNoEncontradaException(
                        "No se encontro a la cuenta con el ID: " + idCuenta));
    }

    @Override
    public void aumentarSaldo(Cuenta depositoCuenta, BigDecimal monto){
        depositoCuenta.setSaldo(depositoCuenta.getSaldo().add(monto));
    }

    @Override
    public void descontarSaldo(Cuenta retiroCuenta, BigDecimal monto){
        retiroCuenta.setSaldo(retiroCuenta.getSaldo().subtract(monto));
    }
    

    @Override
    public void validarValoresTransferencia(Cuenta cuentaEntrada, Cuenta cuentaSalida, BigDecimal monto){
        
        if (!cuentaSalida.getEstado().equals(CuentaEnum.ACTIVA)) {
            throw new CuentaDeshabilitadaException("La cuenta destino se encuentra deshabilitada.");
        }

        if (cuentaEntrada.getSaldo().compareTo(monto) < 0) { // 0 ==, 1 >, -1 <
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar la transaccion.");
        }

        if (cuentaEntrada.getNum_cuenta().equals(cuentaSalida.getNum_cuenta())) {
            throw new ValorInvalidoException("No puedes transferirte dinero a tu propia cuenta.");
        }
    }
}
