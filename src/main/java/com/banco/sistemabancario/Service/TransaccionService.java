
package com.banco.sistemabancario.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Entity.Cuenta;
import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Entity.Transaccion;
import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Repository.CuentaRepository;
import com.banco.sistemabancario.Repository.PersonaRepository;
import com.banco.sistemabancario.Repository.TransaccionRepository;
import com.banco.sistemabancario.Repository.UsuarioRepository;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    //TRANSFERIR
    public Transaccion transferir(int idPersona, String cuentaDestino, String valor, String descripcion){
        
        BigDecimal monto = new BigDecimal(valor.trim());

        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new NoSuchElementException("No se encontro a la persona con el ID: " + idPersona));
        Usuario usuario = usuarioRepository.findByPersona(persona);
        Cuenta cuentaEntrada = cuentaRepository.findByUsuario(usuario);
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
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new NoSuchElementException("No se encontro a la persona con el ID: " + idPersona));
        Usuario usuario = usuarioRepository.findByPersona(persona);
        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);
        List<Transaccion> transaccion = transaccionRepository.findByCuenta(cuenta);

        return transaccion;
    }

    //DEPOSITAR
    public Transaccion depositar(int idPersona, String valor){

        BigDecimal monto = new BigDecimal(valor);

        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow( () -> new NoSuchElementException("No se encontro a la persona con el ID: " + idPersona));
        Usuario usuario = usuarioRepository.findByPersona(persona);
        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);
        
        cuenta.setSaldo(cuenta.getSaldo().add(monto));
        cuentaRepository.save(cuenta);

        Transaccion transaccion = new Transaccion(cuenta, cuenta.getNum_cuenta(), "DEPOSITO", monto, generarFechaActual(), "Deposito de $" + valor);

        return transaccionRepository.save(transaccion);
    }

    //CONSULTAR
    public BigDecimal consultar(int idPersona){
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow( () -> new NoSuchElementException("No se encontro a la persona con el ID: " + idPersona));
        Usuario usuario = usuarioRepository.findByPersona(persona);
        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);

        return cuenta.getSaldo();
    }

    //GENERAR FECHA ACTUAL
    public static String generarFechaActual(){
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ahora.format(formato);
    }

}