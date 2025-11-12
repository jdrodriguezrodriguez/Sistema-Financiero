
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

@Service
public class TransaccionServiceImpl implements TransaccionService{

    private static final BigDecimal MONTO_MINIMO = new BigDecimal("2000.00");

    private TransaccionRepository transaccionRepository;
    private CuentaRepository cuentaRepository;
    private CuentaServiceImpl cuentaService;

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

           Transaccion historialEntrada = crearTransaccion(cuentaEntrada, datos.getCuentaDestino(), "TRANSFERENCIA", monto.negate(), datos.getDescripcion());
           Transaccion historialSalida = crearTransaccion(cuentaSalida, datos.getCuentaDestino(), "TRANSFERENCIA", monto, datos.getDescripcion());
            
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
    public Transaccion depositar(int idUser, String valor){
        try {

            BigDecimal monto = new BigDecimal(valor.trim());

            if (monto.compareTo(MONTO_MINIMO) <= 0) {
                throw new ValorInvalidoException("El valor a depositar debe ser mayor o igual a $2.000");
            }

            Cuenta cuenta = cuentaService.buscarCuenta(idUser);
            cuenta.setSaldo(cuenta.getSaldo().add(monto));
            cuentaRepository.save(cuenta);

            Transaccion transaccion = new Transaccion(cuenta, cuenta.getNum_cuenta(), "DEPOSITO", monto, generarFechaActual(), "Deposito de $" + valor);

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

    //CREAR TRANSACCION
    public Transaccion crearTransaccion(Cuenta cuenta, String cuentaDestino, String tipo, BigDecimal monto, String descripcion){
        Transaccion t = new Transaccion();
        t.setCuenta(cuenta);
        t.setCuenta_destino(cuentaDestino);
        t.setTipo(tipo);
        t.setFecha(generarFechaActual());
        t.setMonto(monto);
        t.setDescripcion(descripcion);
        return t;
    }

    //GENERAR FECHA ACTUAL
    public static String generarFechaActual(){
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ahora.format(formato);
    }
}