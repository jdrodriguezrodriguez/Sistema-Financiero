
package com.banco.sistemabancario.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Transaccion;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.TransaccionRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;

@Service
public class TransaccionService {

    private static final BigDecimal MONTO_MINIMO = new BigDecimal("2000.00");

    private TransaccionRepository transaccionRepository;
    private PersonaRepository personaRepository;
    private UsuarioRepository usuarioRepository;
    private CuentaRepository cuentaRepository;

    public TransaccionService(TransaccionRepository transaccionRepository, PersonaRepository personaRepository,
            UsuarioRepository usuarioRepository, CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
    }

    //TRANSFERIR
    public Transaccion transferir(int idPersona, String cuentaDestino, String valor, String descripcion){

        
        try {
            BigDecimal monto = new BigDecimal(valor.trim());
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El valor debe ser mayor $0.");
            }

            Cuenta cuentaEntrada = buscarCuenta(idPersona);
            Cuenta cuentaSalida = cuentaRepository.findById(cuentaDestino)
                    .orElseThrow(() -> new NoSuchElementException("No se encontro a la cuenta con el ID: " + cuentaDestino));

            if (!cuentaSalida.getEstado().equals("ACTIVA")) {
                throw new IllegalArgumentException("La cuenta destino se encuentra deshabilitada.");
            }

            if (cuentaEntrada.getSaldo().compareTo(monto) < 0) {                        //0 ==, 1 >, -1 <
                throw new IllegalArgumentException("Saldo insuficiente para realizar la transaccion.");
            }

            cuentaEntrada.setSaldo(cuentaEntrada.getSaldo().subtract(monto));
            cuentaSalida.setSaldo(cuentaSalida.getSaldo().add(monto));

           Transaccion historialEntrada = crearTransaccion(cuentaEntrada, cuentaDestino, "TRANSFERENCIA", monto.negate(), descripcion);
           Transaccion historialSalida = crearTransaccion(cuentaSalida, cuentaDestino, "TRANSFERENCIA", monto, descripcion);
            
            cuentaRepository.save(cuentaEntrada);
            cuentaRepository.save(cuentaSalida);
            transaccionRepository.save(historialEntrada);
            
            return transaccionRepository.save(historialSalida);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de valor inválido:" + e.getMessage());
        }
    }

    //TRANSFERENCIAS
    public List<Transaccion> transacciones(int idPersona){
        Cuenta cuenta = buscarCuenta(idPersona);
        return transaccionRepository.findByCuenta(cuenta);
    }

    //DEPOSITAR
    public Transaccion depositar(int idPersona, String valor){
        try {

            BigDecimal monto = new BigDecimal(valor.trim());

            if (monto.compareTo(MONTO_MINIMO) <= 0) {
                throw new IllegalArgumentException("El valor a depositar debe ser mayor o igual a $2.000");
            }

            Cuenta cuenta = buscarCuenta(idPersona);
            cuenta.setSaldo(cuenta.getSaldo().add(monto));
            cuentaRepository.save(cuenta);

            Transaccion transaccion = new Transaccion(cuenta, cuenta.getNum_cuenta(), "DEPOSITO", monto, generarFechaActual(), "Deposito de $" + valor);

            return transaccionRepository.save(transaccion);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de valor inválido" + e.getMessage());
        }
    }

    //CONSULTAR
    public BigDecimal consultar(int idPersona){
        Cuenta cuenta = buscarCuenta(idPersona);
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

    //BUSCAR CUENTA
    public Cuenta buscarCuenta(int idPersona){
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow( () -> new NoSuchElementException("No se encontro a la persona con el ID: " + idPersona));

        Usuario usuario = usuarioRepository.findByPersona(persona);
        if (usuario == null) {
            throw new NoSuchElementException("No se encontró el usuario para la persona con ID: " + idPersona);
        }

        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);
        if (cuenta == null) {
            throw new NoSuchElementException("No se encontró la cuenta para el usuario con ID: " + usuario.getIdUsuario());
        }
        
        return cuenta;
    }
}