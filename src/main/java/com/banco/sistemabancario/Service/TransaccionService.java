
package com.banco.sistemabancario.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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