package com.banco.sistemabancario.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.sistemabancario.dto.RegistroPersonaDTO;
import com.banco.sistemabancario.service.PersonaService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PersonaController {
    
    private static final Logger logger =  LoggerFactory.getLogger(PersonaController.class);

    private PersonaService personaService;
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    //REGISTRAR
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroPersonaDTO datos) {
       
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
    @PostMapping("/actualizarPersona")
    public String actualizarDatosPersona(@RequestParam String nombre,
                                        @RequestParam String apellido,
                                        @RequestParam String correo,
                                        @RequestParam String nacimiento,  HttpSession session){
      
        Integer idPersona = (Integer) session.getAttribute("idPersona");

        try{
            personaService.actualizarPersona(nombre, apellido, correo, nacimiento, idPersona);
            logger.info("Los datos personales fueron actualizados correctamente");
            return "redirect:/index.html";
            
        }catch(NoSuchElementException e){
            logger.error("Error en actualizar los datos personales:", e);
            return "redirect:/update.html?error=";
        }
    }
}
