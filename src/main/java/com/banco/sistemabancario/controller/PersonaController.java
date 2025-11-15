package com.banco.sistemabancario.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.security.controller.CustomUserDetails;
import com.banco.sistemabancario.service.CuentaService;
import com.banco.sistemabancario.service.PersonaService;
import com.banco.sistemabancario.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/sistema/personas")
public class PersonaController {
    
    private static final Logger logger =  LoggerFactory.getLogger(PersonaController.class);

    private PersonaService personaService;
    private UsuarioService usuarioService;
    private CuentaService cuentaService;

    public PersonaController(PersonaService personaService, 
                            UsuarioService usuarioService,
                            CuentaService cuentaService) {
        this.personaService = personaService;
        this.usuarioService = usuarioService;
        this.cuentaService = cuentaService;
    }

    //CONSULTAS
    @GetMapping("/{idPersona}")
    public ResponseEntity<?> buscarPersonaPorId(@PathVariable int idPersona) {
        Persona persona = personaService.obtenerPersonaPorId(idPersona);
        return ResponseEntity.ok(persona);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarPersonas() {
        return ResponseEntity.ok(personaService.obtenerPersonas());
    }
    
    //CREAR
    @PostMapping("/registrar")
    public ResponseEntity<?> registroPersona(@Valid @RequestBody RegistroPersonaDto datos) {
       
        try{
            Persona persona = personaService.registrarPersona(datos);
            Usuario usuario = usuarioService.registrarUsuario(datos.getNombre(), datos.getApellido(), datos.getPassword(), persona);
            cuentaService.registrarCuenta(usuario);

            logger.info("El registro se realizo correctamente");
            return ResponseEntity.ok(Map.of("Mensaje", "Registro exitoso"));
            
        }catch(IllegalArgumentException e){
            logger.error("Error al registrar a la persona {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }       
    }

    //ACTUALIZAR
    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarPersona(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody ActualizarPersonaDto actualizarPersonaDto){  
        try{
            Persona persona = usuarioService.obtenerPersonaPorUsuarioId(user.getId());
            personaService.actualizarDatosPersona(actualizarPersonaDto, persona.getIdPersona());
            
            logger.info("Los datos personales fueron actualizados correctamente");
            return ResponseEntity.ok(Map.of("Mensaje", "Actualizacion exitosa"));
            
        }catch(NoSuchElementException e){
            logger.error("Error en actualizar los datos personales:", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //ELIMINAR
    @DeleteMapping("/{idPersona}")
    public ResponseEntity<?> eliminarPersona(@PathVariable int idPersona){

        personaService.eliminarPersona(idPersona);
        return ResponseEntity.ok(Map.of("Mensaje","Persona eliminada"));
    }
}
