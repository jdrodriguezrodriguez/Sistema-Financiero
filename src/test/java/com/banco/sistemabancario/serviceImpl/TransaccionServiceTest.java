package com.banco.sistemabancario.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.banco.sistemabancario.dto.TransferirDineroDto;
import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Transaccion;
import com.banco.sistemabancario.events.EmailDepositoEvent;
import com.banco.sistemabancario.events.EmailTransaccionEvent;
import com.banco.sistemabancario.exception.ValorInvalidoException;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.repository.TransaccionRepository;

@ExtendWith(MockitoExtension.class)
public class TransaccionServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private CuentaServiceImpl cuentaServiceImpl;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private TransaccionServiceImpl transaccionServiceImpl;

    @Test
    public void transferenciaCorrecta() {
        TransferirDineroDto datos = new TransferirDineroDto(
                "12345",
                "5000",
                null);

        Cuenta cuentaEnvio = new Cuenta();
        cuentaEnvio.setNum_cuenta("54321");
        cuentaEnvio.setSaldo(BigDecimal.valueOf(7500));

        Cuenta cuentaRecibo = new Cuenta();
        cuentaRecibo.setNum_cuenta("12345");
        cuentaRecibo.setSaldo(BigDecimal.valueOf(0));

        when(cuentaServiceImpl.buscarCuentaPorIdUser(1)).thenReturn(cuentaEnvio);
        when(cuentaServiceImpl.buscarCuentaPorNumeroCuenta(datos.getCuentaDestino())).thenReturn(cuentaRecibo);

        transaccionServiceImpl.transferir(1, datos);

        ArgumentCaptor<List<Transaccion>> captor = ArgumentCaptor.forClass(List.class);
        verify(transaccionRepository).saveAll(captor.capture());

        List<Transaccion> transaccionsTest = captor.getValue();

        assertEquals(0, transaccionsTest.get(1).getMonto().compareTo(BigDecimal.valueOf(5000)));
        verify(applicationEventPublisher).publishEvent(any(EmailTransaccionEvent.class));
    }

    @Test
    public void LanzarExcepcion_DepositoValorInvalido() {
        ValorInvalidoException exception = assertThrows(ValorInvalidoException.class, () -> {
            transaccionServiceImpl.depositar(1, "1900");
        });

        assertEquals("El depósito debe ser de al menos $2.000.", exception.getMessage());
        verify(transaccionRepository, times(0)).save(any());
    }

    @Test
    public void depositoCorrecto() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNum_cuenta("123456");
        cuenta.setSaldo(BigDecimal.valueOf(0));

        when(cuentaServiceImpl.buscarCuentaPorIdUser(1)).thenReturn(cuenta);

        transaccionServiceImpl.depositar(1, "5000");

        ArgumentCaptor<Transaccion> captor = ArgumentCaptor.forClass(Transaccion.class);
        verify(transaccionRepository).save(captor.capture());

        Transaccion transaccionTest = captor.getValue();

        assertEquals(transaccionTest.getMonto(), BigDecimal.valueOf(5000));
        verify(applicationEventPublisher).publishEvent(any(EmailDepositoEvent.class));
    }

    @Test
    public void consultar() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNum_cuenta("123456");
        cuenta.setSaldo(BigDecimal.valueOf(5000));

        when(cuentaServiceImpl.buscarCuentaPorIdUser(1)).thenReturn(cuenta);

        BigDecimal resultado = transaccionServiceImpl.consultar(1);

        assertEquals(BigDecimal.valueOf(5000), resultado);
    }
}