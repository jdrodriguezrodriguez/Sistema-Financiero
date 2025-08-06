
package com.banco.sistemabancario.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

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
        
        BigDecimal monto = new BigDecimal(valor.trim());

        Cuenta cuentaEntrada = buscarCuenta(idPersona);
        Cuenta cuentaSalida = cuentaRepository.findById(cuentaDestino)
                .orElseThrow(() -> new NoSuchElementException("No se encontro a la cuenta con el ID: " + cuentaDestino));

        cuentaEntrada.setSaldo(cuentaEntrada.getSaldo().subtract(monto));
        cuentaSalida.setSaldo(cuentaSalida.getSaldo().add(monto));

        Transaccion historialEntrada = new Transaccion();
        historialEntrada.setCuenta(cuentaEntrada);
        historialEntrada.setCuenta_destino(cuentaDestino);
        historialEntrada.setTipo("TRANSFERENCIA");
        historialEntrada.setMonto(monto.negate());
        historialEntrada.setFecha(generarFechaActual());
        historialEntrada.setDescripcion(descripcion);

        Transaccion historialSalida = new Transaccion();
        historialSalida.setCuenta(cuentaEntrada);
        historialSalida.setCuenta_destino(cuentaDestino);
        historialSalida.setTipo("TRANSFERENCIA");
        historialSalida.setMonto(monto);
        historialSalida.setFecha(generarFechaActual());
        historialSalida.setDescripcion(descripcion);
        
        cuentaRepository.save(cuentaEntrada);
        cuentaRepository.save(cuentaSalida);
        transaccionRepository.save(historialEntrada);
        
        return transaccionRepository.save(historialSalida);
    }

    //TRANSFERENCIAS
    public List<Transaccion> transacciones(int idPersona){
        Cuenta cuenta = buscarCuenta(idPersona);
        return transaccionRepository.findByCuenta(cuenta);
    }

    //DEPOSITAR
    public Transaccion depositar(int idPersona, String valor){

        BigDecimal monto = new BigDecimal(valor);

        Cuenta cuenta = buscarCuenta(idPersona);
        
        cuenta.setSaldo(cuenta.getSaldo().add(monto));
        cuentaRepository.save(cuenta);

        Transaccion transaccion = new Transaccion(cuenta, cuenta.getNum_cuenta(), "DEPOSITO", monto, generarFechaActual(), "Deposito de $" + valor);

        return transaccionRepository.save(transaccion);
    }

    //CONSULTAR
    public BigDecimal consultar(int idPersona){
        Cuenta cuenta = buscarCuenta(idPersona);
        return cuenta.getSaldo();
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