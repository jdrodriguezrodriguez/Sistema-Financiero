package com.banco.sistemabancario.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.CuentaNoEncontradaException;
import com.banco.sistemabancario.exception.PersonaNoEncontradaException;
import com.banco.sistemabancario.exception.UsuarioNoencontradoException;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;

@Service
public class CuentaService {

    private static final String ESTADO_CUENTA = "ACTIVA";
    private final Random random = new Random();

    private PersonaRepository personaRepository;
    private UsuarioRepository usuarioRepository;
    private CuentaRepository cuentaRepository;

    public CuentaService(PersonaRepository personaRepository, UsuarioRepository usuarioRepository,
            CuentaRepository cuentaRepository) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
    }

    // REGISTRAR CUENTA
    public Cuenta registrarCuenta(Usuario usuario) {

        Cuenta cuenta = new Cuenta();

        cuenta.setNum_cuenta(generarNumeroCuenta());
        cuenta.setUsuario(usuario);
        cuenta.setEstado(ESTADO_CUENTA);
        cuenta.setSaldo((new BigDecimal("0.00")));

        return cuentaRepository.save(cuenta);
    }

    // GENERAR NUMERO DE CUENTA
    public String generarNumeroCuenta() {

        String numeroCuenta;

        do {
            int numero = 1000000000 + random.nextInt(900000000);
            numeroCuenta = String.valueOf(numero);
        } while (cuentaRepository.existsByNumCuenta(numeroCuenta));

        return numeroCuenta;
    }

    // BUSCAR CUENTA
    public Cuenta buscarCuenta(int idPersona) {

        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new PersonaNoEncontradaException("No se encontro a la persona con el ID: " + idPersona));

        Usuario usuario = usuarioRepository.findByPersona(persona);
        if (usuario == null) {
            throw new UsuarioNoencontradoException("No se encontró el usuario con el ID de la persona: " + idPersona);
        }

        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("No se encontró la cuenta para el usuario con ID: " + usuario.getIdUsuario());
        }

        return cuenta;
    }
}
