package com.banco.sistemabancario.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.banco.sistemabancario.dto.ActualizarPersonaDto;
import com.banco.sistemabancario.dto.RegistroPersonaDto;
import com.banco.sistemabancario.service.PersonaService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class PersonaController {
    
    private static final Logger logger =  LoggerFactory.getLogger(PersonaController.class);

    private PersonaService personaService;
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    //REGISTRAR UNA PERSONA
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroPersonaDto datos) {
       
        try{
            personaService.registrarPersona(datos);
            logger.info("El registro se realizo correctamente");
            return ResponseEntity.ok(Map.of("Mensaje", "Registro exitoso"));
            
        }catch(IllegalArgumentException e){
            logger.error("Error al registrar a la persona {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }       
    }

    //ACTUALIZAR UNA PERSONA EXISTENTE
    @PutMapping("/actualizarPersona")
    public ResponseEntity<?> actualizarDatosPersona(@Valid @RequestBody ActualizarPersonaDto actualizarPersonaDto,  HttpSession session){
      
        Integer idPersona = (Integer) session.getAttribute("idPersona");
        if (idPersona == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesion no valida");
        }

        try{
            personaService.actualizarPersona(actualizarPersonaDto, idPersona);
            logger.info("Los datos personales fueron actualizados correctamente");
            return ResponseEntity.ok(Map.of("Mensaje", "Actualizacion exitosa"));
            
        }catch(NoSuchElementException e){
            logger.error("Error en actualizar los datos personales:", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
