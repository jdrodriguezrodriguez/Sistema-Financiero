package com.banco.sistemabancario.Dto;

import java.math.BigDecimal;

import com.banco.sistemabancario.Entity.Cuenta;

public class TransferenciasDto {

    private Cuenta numeroCuenta;
    private String cuentaDestino, tipoTransaccion, descripcion, fecha;
    private BigDecimal monto;

    public TransferenciasDto(){
    }

    public TransferenciasDto(Cuenta numeroCuenta, String cuentaDestino, String tipoTransaccion, BigDecimal monto,
            String fecha, String descripcion) {
        this.numeroCuenta = numeroCuenta;
        this.cuentaDestino = cuentaDestino;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public Cuenta getNumeroCuenta() {
        return numeroCuenta;
    }
    public void setNumeroCuenta(Cuenta numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }
    public void setCuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }
    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public BigDecimal getMonto() {
        return monto;
    }
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
