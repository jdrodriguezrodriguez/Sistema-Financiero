package com.banco.sistemabancario.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Transaccion;

public class TransaccionUtils {
    
    private TransaccionUtils(){}

    public static String generarFechaActual(){
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ahora.format(formato);
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
}
