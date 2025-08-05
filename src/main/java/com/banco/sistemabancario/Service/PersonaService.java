package com.banco.sistemabancario.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Dto.RegistroPersonaDTO;
import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Repository.PersonaRepository;

@Service
public class PersonaService {
    
    private PersonaRepository personaRepository;
    private UsuarioService usuarioService;
    private CuentaService cuentaService;

    public PersonaService(PersonaRepository personaRepository, UsuarioService usuarioService, CuentaService cuentaService) {
        this.personaRepository = personaRepository;
        this.usuarioService = usuarioService;
        this.cuentaService = cuentaService;
    }

    //ACTUALIZAR PERSONA
    public Persona actualizarPersona(String nombre, String apellido, String correo, String nacimiento, int idPersona){

        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new NoSuchElementException("No se encontro a la persona con el ID: " + idPersona));
                
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setCorreo(correo);
        persona.setNacimiento(Date.valueOf(LocalDate.parse(nacimiento)));

        return personaRepository.save(persona);
    }

    //REGISTRAR PERSONA
    public Persona registrarPersona(RegistroPersonaDTO datos){
    
        validarDatosRegistro(datos);
        Persona persona = convertirAObjeto(datos);
      
        Persona personaRegistro = personaRepository.save(persona);
        Usuario usuarioRegistro = usuarioService.registrarUsuario(datos.getNombre(), datos.getApellido(), datos.getPassword(), persona);
        cuentaService.registrarCuenta(usuarioRegistro);

        return personaRegistro;
    }

    //DTO A PERSONA
    public Persona convertirAObjeto(RegistroPersonaDTO datos){
        Persona persona = new Persona();
        persona.setNombre(datos.getNombre());
        persona.setApellido(datos.getApellido());
        persona.setDocumento(datos.getDocumento());
        persona.setNacimiento(Date.valueOf(LocalDate.parse(datos.getNacimiento())));
        persona.setCorreo(datos.getCorreo());
        return persona;
    }

    //VALIDAR REGISTRO DEL DOCUMENTO
    public boolean documentoYaRegistrado(String documento){
        return personaRepository.existsByDocumento(documento);
    }

    //VALIDAR REGISTRO DEL CORREO
    public boolean correoYaRegistrado(String correo){
        return personaRepository.existsByCorreo(correo);
    }

    //VALIDAR CAMPOS REGISTRO
    public static boolean camposRegistroValidos(RegistroPersonaDTO datos){
        if (datos.getNombre().isEmpty() && datos.getApellido().isEmpty() && datos.getDocumento().isEmpty() && datos.getNacimiento().isEmpty() && datos.getCorreo().isEmpty() && datos.getPassword().isEmpty()) {
            System.out.println("Complete los campos vacios");
            return false;
        }
        return true;
    }

    //VALIDAR DATOS REGISTRO
    public void validarDatosRegistro(RegistroPersonaDTO datos){
        if (documentoYaRegistrado(datos.getDocumento())) {
            throw new IllegalArgumentException("Ya existe una persona registrada con el documento: " + datos.getDocumento());
        }

        if (correoYaRegistrado(datos.getCorreo())) {
            throw new IllegalArgumentException("Ya existe una persona registrada con el correo electronico: " + datos.getCorreo());
        }

        if (!camposRegistroValidos(datos)) {
            throw new IllegalArgumentException("Hay campos obligatorios vacios.");
        }

        if (!UsuarioService.validarPassword(datos.getPassword())) {
            throw new IllegalArgumentException("La contraseña debe tener exactamente cuatro digitos.");
        }
    }
}