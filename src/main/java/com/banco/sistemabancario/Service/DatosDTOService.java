package com.banco.sistemabancario.Service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Dto.DatosDTO;
import com.banco.sistemabancario.Entity.Cuenta;
import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Repository.CuentaRepository;
import com.banco.sistemabancario.Repository.PersonaRepository;
import com.banco.sistemabancario.Repository.UsuarioRepository;

@Service
public class DatosDTOService {
    
    @Autowired
    PersonaRepository personaRepository;

    @Autowired 
    UsuarioRepository usuarioRepository;

    @Autowired 
    CuentaRepository cuentaRepository;

    //BUSCAR DATOS DEL USUARIO
    public DatosDTO datosUsuario(int idPersona){

        Persona persona = personaRepository.findById(idPersona).orElseThrow(() -> new NoSuchElementException("No se encontro a la persona con el ID: " + idPersona));
        Usuario usuario = usuarioRepository.findByPersona(persona);
        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);

        return new DatosDTO(persona.getNombre(), persona.getApellido(), persona.getDocumento(),
                            persona.getCorreo(), usuario.getUsername(), usuario.getRol(), 
                            cuenta.getNum_cuenta(), cuenta.getEstado(), persona.getNacimiento());  
    }
}
