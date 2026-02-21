
package com.banco.sistemabancario.serviceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.TransferirDineroDto;
import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Transaccion;
import com.banco.sistemabancario.events.EmailDepositoEvent;
import com.banco.sistemabancario.events.EmailTransaccionEvent;
import com.banco.sistemabancario.exception.ValorInvalidoException;
import com.banco.sistemabancario.repository.TransaccionRepository;
import com.banco.sistemabancario.service.CuentaService;
import com.banco.sistemabancario.service.TransaccionService;
import com.banco.sistemabancario.util.TransaccionUtils;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private static final BigDecimal MONTO_MINIMO = new BigDecimal("2000.00");

    private TransaccionRepository transaccionRepository;
    private CuentaService cuentaService;
    private TransaccionUtils transaccionUtils;

    private ApplicationEventPublisher applicationEventPublisher;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository,
            CuentaService cuentaService, ApplicationEventPublisher applicationEventPublisher) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaService = cuentaService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public Transaccion transferir(int idUser, TransferirDineroDto datos) {

        BigDecimal monto;

        try {
            monto = new BigDecimal(datos.getValor().trim());
        } catch (NumberFormatException e) {
            throw new ValorInvalidoException("Formato de valor inválido: " + e.getMessage());
        }

        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("El valor debe ser mayor a $0.");
        }

        Cuenta cuentaEntrada = cuentaService.buscarCuentaPorIdUser(idUser);
        Cuenta cuentaSalida = cuentaService.buscarCuentaPorNumeroCuenta(datos.getCuentaDestino());

        cuentaService.validarValoresTransferencia(cuentaEntrada, cuentaSalida, monto);

        cuentaService.descontarSaldo(cuentaEntrada, monto);
        cuentaService.aumentarSaldo(cuentaSalida, monto);

        Transaccion historialEnvio = transaccionUtils.crearTransaccion(
                cuentaEntrada,
                datos.getCuentaDestino(),
                "TRANSFERENCIA",
                monto.negate(),
                datos.getDescripcion());

        Transaccion historialRecibo = transaccionUtils.crearTransaccion(
                cuentaSalida,
                datos.getCuentaDestino(),
                "TRANSFERENCIA",
                monto,
                datos.getDescripcion());

        transaccionRepository.saveAll(List.of(historialEnvio, historialRecibo));

        applicationEventPublisher.publishEvent(
            new EmailTransaccionEvent(
                historialEnvio, 
                historialEnvio.getCuenta().getNum_cuenta(), 
                historialEnvio.getCuenta_destino())
        );

        return historialEnvio;
    }

    @Override
    public List<Transaccion> transacciones(int idUser) {
        Cuenta cuenta = cuentaService.buscarCuentaPorIdUser(idUser);
        return transaccionRepository.findByCuenta(cuenta);
    }

    @Transactional
    @Override
    public Transaccion depositar(int idUser, String valor) {

        BigDecimal monto;

        try {
            monto = new BigDecimal(valor.trim());
        } catch (NumberFormatException e) {
            throw new ValorInvalidoException("Formato inválido: " + e.getMessage());
        }

        if (monto.compareTo(MONTO_MINIMO) < 0) {
            throw new ValorInvalidoException("El depósito debe ser de al menos $2.000.");
        }

        Cuenta cuenta = cuentaService.buscarCuentaPorIdUser(idUser);

        cuentaService.aumentarSaldo(cuenta, monto);

        Transaccion transaccion = new Transaccion(
                cuenta,
                cuenta.getNum_cuenta(),
                "DEPOSITO",
                monto,
                transaccionUtils.generarFechaActual(),
                "Deposito de $" + monto);

        applicationEventPublisher.publishEvent(
            new EmailDepositoEvent(
                transaccion, idUser
            )
        );

        return transaccionRepository.save(transaccion);
    }

    public BigDecimal consultar(int idUser) {
        Cuenta cuenta = cuentaService.buscarCuentaPorIdUser(idUser);
        return cuenta.getSaldo();
    }
}