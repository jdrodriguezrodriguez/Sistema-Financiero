package com.banco.sistemabancario.serviceImpl;

import org.springframework.stereotype.Service;

import com.banco.sistemabancario.dto.DatosDto;
import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.exception.UsuarioNoencontradoException;
import com.banco.sistemabancario.repository.CuentaRepository;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.service.DatosDTOService;

@Service
public class DatosDTOServiceImpl implements DatosDTOService{
    
    private PersonaRepository personaRepository;
    private UsuarioRepository usuarioRepository;
    private CuentaRepository cuentaRepository;

    public DatosDTOServiceImpl(PersonaRepository personaRepository, UsuarioRepository usuarioRepository, CuentaRepository cuentaRepository) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
    }

    //BUSCAR DATOS DEL USUARIO
    @Override
    public DatosDto datosUsuario(int idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new UsuarioNoencontradoException("No se encontro el usuario con ID: " + idUsuario));
        Persona persona = personaRepository.findByUsuario(usuario);
        Cuenta cuenta = cuentaRepository.findByUsuario(usuario);

        return new DatosDto(persona.getNombre(), persona.getApellido(), persona.getDocumento(),
                            persona.getCorreo(), usuario.getUsername(), usuario.getRol(), 
                            cuenta.getNum_cuenta(), cuenta.getEstado(), persona.getNacimiento());  
    }
}
