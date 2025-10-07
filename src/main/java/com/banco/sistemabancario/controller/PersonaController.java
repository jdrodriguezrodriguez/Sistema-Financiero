package com.banco.sistemabancario.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.serviceImpl.PersonaServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/sistema/personas")
public class PersonaController {
    
    private static final Logger logger =  LoggerFactory.getLogger(PersonaController.class);

    private PersonaServiceImpl personaService;
    public PersonaController(PersonaServiceImpl personaService) {
        this.personaService = personaService;
    }

    @GetMapping("/{idPersona}")
    public ResponseEntity<?> buscarPersonaPorId(@PathVariable int idPersona) {
        return personaService.obtenerPersonaPorId(idPersona)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @GetMapping
    public ResponseEntity<?> listarPersonas() {
        return ResponseEntity.ok(personaService.obtenerPersonas());
    }
    

    //CREAR
    @PostMapping
    public ResponseEntity<?> registroPersona(@Valid @RequestBody RegistroPersonaDto datos) {
       
        try{
            personaService.registrarPersona(datos);
            logger.info("El registro se realizo correctamente");
            return ResponseEntity.ok(Map.of("Mensaje", "Registro exitoso"));
            
        }catch(IllegalArgumentException e){
            logger.error("Error al registrar a la persona {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }       
    }

    //ACTUALIZAR
    @PutMapping("/{idPersona}")
    public ResponseEntity<?> actualizarPersona(@PathVariable int idPersona, @Valid @RequestBody ActualizarPersonaDto actualizarPersonaDto,  HttpSession session){
      
        /*Integer idPersona = (Integer) session.getAttribute("idPersona");
        if (idPersona == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesion no valida");
        }*/

        try{
            personaService.actualizarDatosPersona(actualizarPersonaDto, idPersona);
            logger.info("Los datos personales fueron actualizados correctamente");
            return ResponseEntity.ok(Map.of("Mensaje", "Actualizacion exitosa"));
            
        }catch(NoSuchElementException e){
            logger.error("Error en actualizar los datos personales:", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
