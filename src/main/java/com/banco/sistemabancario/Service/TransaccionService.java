
package com.banco.sistemabancario.Service;

import java.math.BigDecimal;

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
    public Transaccion depositar(int idPersona, BigDecimal valor, String descripcion){
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow( () -> new NoSuchElementException("No se encontro a la persona con el ID: " + idPersona));

        Usuario usuario = usuarioRepository.findByPersona(persona);

        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);

        return transaccion = new Transaccion(idPersona, cuenta.getNum_cuenta(), cuenta.getNum_cuenta, "DEPOSITO", valor, null, descripcion);
    }

}