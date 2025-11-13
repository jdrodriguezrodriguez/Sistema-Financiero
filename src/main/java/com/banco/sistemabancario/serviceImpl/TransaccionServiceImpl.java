
package com.banco.sistemabancario.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.TransferirDineroDto;
import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Transaccion;
import com.banco.sistemabancario.entity.enums.CuentaEnum;
import com.banco.sistemabancario.exception.CuentaDeshabilitadaException;
import com.banco.sistemabancario.exception.CuentaNoEncontradaException;
import com.banco.sistemabancario.exception.SaldoInsuficienteException;
import com.banco.sistemabancario.exception.ValorInvalidoException;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.repository.TransaccionRepository;
import com.banco.sistemabancario.service.TransaccionService;
import com.banco.sistemabancario.util.TransaccionUtils;

@Service
public class TransaccionServiceImpl implements TransaccionService{

    private static final BigDecimal MONTO_MINIMO = new BigDecimal("2000.00");

    private TransaccionRepository transaccionRepository;
    private CuentaRepository cuentaRepository;
    private CuentaServiceImpl cuentaService;
    private TransaccionUtils transaccionUtils;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, CuentaRepository cuentaRepository, CuentaServiceImpl cuentaService) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
        this.cuentaService = cuentaService;
    }

    //TRANSFERIR
    @Override
    @Transactional
    public Transaccion transferir(int idUser, TransferirDineroDto datos){

        
        try {
            BigDecimal monto = new BigDecimal(datos.getValor().trim());
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValorInvalidoException("El valor debe ser mayor $0.");
            }

            Cuenta cuentaEntrada = cuentaService.buscarCuenta(idUser);
            Cuenta cuentaSalida = cuentaRepository.findById(datos.getCuentaDestino())
                    .orElseThrow(() -> new CuentaNoEncontradaException("No se encontro a la cuenta con el ID: " + datos.getCuentaDestino()));

            if (!cuentaSalida.getEstado().equals(CuentaEnum.ACTIVA)) {
                throw new CuentaDeshabilitadaException("La cuenta destino se encuentra deshabilitada.");
            }

            if (cuentaEntrada.getSaldo().compareTo(monto) < 0) {                        //0 ==, 1 >, -1 <
                throw new SaldoInsuficienteException("Saldo insuficiente para realizar la transaccion.");
            }

            cuentaEntrada.setSaldo(cuentaEntrada.getSaldo().subtract(monto));
            cuentaSalida.setSaldo(cuentaSalida.getSaldo().add(monto));

           Transaccion historialEntrada = transaccionUtils.crearTransaccion(
                                        cuentaEntrada, 
                                        datos.getCuentaDestino(), 
                                        "TRANSFERENCIA", 
                                        monto.negate(), 
                                        datos.getDescripcion()
                                        );
           Transaccion historialSalida = transaccionUtils.crearTransaccion(
                                        cuentaSalida, 
                                        datos.getCuentaDestino(), 
                                        "TRANSFERENCIA", 
                                        monto, 
                                        datos.getDescripcion());
            
            cuentaRepository.save(cuentaEntrada);
            cuentaRepository.save(cuentaSalida);
            transaccionRepository.save(historialEntrada);
            
            return transaccionRepository.save(historialSalida);
        } catch (NumberFormatException e) {
            throw new ValorInvalidoException("Formato de valor inválido:" + e.getMessage());
        }
    }

    //TRANSFERENCIAS
    @Override
    public List<Transaccion> transacciones(int idPersona){
        Cuenta cuenta = cuentaService.buscarCuenta(idPersona);
        return transaccionRepository.findByCuenta(cuenta);
    }

    //DEPOSITAR
    @Transactional
    @Override
    public Transaccion depositar(int idUser, String valor){
        try {

            BigDecimal monto = new BigDecimal(valor.trim());

            if (monto.compareTo(MONTO_MINIMO) <= 0) {
                throw new ValorInvalidoException("El valor a depositar debe ser mayor o igual a $2.000");
            }

            Cuenta cuenta = cuentaService.buscarCuenta(idUser);
            cuenta.setSaldo(cuenta.getSaldo().add(monto));
            cuentaRepository.save(cuenta);

            Transaccion transaccion = new Transaccion(cuenta, cuenta.getNum_cuenta(), "DEPOSITO", monto, transaccionUtils.generarFechaActual(), "Deposito de $" + valor);

            return transaccionRepository.save(transaccion);
        } catch (NumberFormatException e) {
            throw new ValorInvalidoException("Formato de valor inválido" + e.getMessage());
        }
    }

    //CONSULTAR
    public BigDecimal consultar(int idUser){
        Cuenta cuenta = cuentaService.buscarCuenta(idUser);
        return cuenta.getSaldo();
    }
}