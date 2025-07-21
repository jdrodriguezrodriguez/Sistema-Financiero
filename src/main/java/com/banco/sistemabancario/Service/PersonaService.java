package com.banco.sistemabancario.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.Dto.RegistroPersonaDTO;
import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Repository.PersonaRepository;

@Service
public class PersonaService {
    
    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CuentaService cuentaService;

    //ACTUALIZAR PERSONA
    public Persona actualizarPersona(String nombre, String apellido, String correo, String nacimiento, int idUsuario){

        Persona persona = personaRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("No se encontro a la persona con el ID: " + idUsuario));

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

        if (!UsuarioService.ValidarContraseña(datos.getPassword())) {
            throw new IllegalArgumentException("La contraseña debe tener exactamente cuatro digitos.");
        }
    }
}


/*
//REGISTRAR PERSONA
    public Persona registrarPersona(String nombre, String apellido, String documento, String nacimiento, String correo, String password){
    
        if (documentoYaRegistrado(documento)) {
            System.out.println("El documento "+ documento +" ya fue registrado.");
            return null;
        }

        if (correoYaRegistrado(correo)) {
            System.out.println("El correo "+ correo +" ya fue registrado.");
            return null;
        }

        if (!camposRegistroValidos(nombre, apellido, documento, nacimiento, correo, password)) {
            return null;
        }

        Persona persona = new Persona();
        LocalDate fechanacimiento = LocalDate.parse(nacimiento);

        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setDocumento(documento);
        persona.setNacimiento(Date.valueOf(fechanacimiento));
        persona.setCorreo(correo);
      
        Persona personaRegistro = personaRepository.save(persona);
        if (personaRegistro == null) {
            System.out.println("No se logro registrar a la persona");
            return null;
        }

        Usuario usuarioRegistro = usuarioService.registrarUsuario(nombre, apellido, password, persona);
        if (usuarioRegistro == null) {
            System.out.println("No se logro registrar el usuario");
            return null;
        }

        cuentaService.registrarCuenta(usuarioRegistro);

        return personaRegistro;
    }
 */