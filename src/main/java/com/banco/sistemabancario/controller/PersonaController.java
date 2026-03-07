package com.banco.sistemabancario.controller;

import java.util.Map;

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
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.security.controller.CustomUserDetails;
import com.banco.sistemabancario.service.PersonaService;
import com.banco.sistemabancario.service.UsuarioApplicationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Personas", description = "Operaciones relacionadas con la gestion de personas registradas en el sistema.")
@RestController
@RequestMapping("/api/sistema/personas")
public class PersonaController {

    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class);

    private PersonaService personaService;
    private UsuarioApplicationService applicationService;

    public PersonaController(PersonaService personaService,
            UsuarioApplicationService applicationService) {
        this.personaService = personaService;
        this.applicationService = applicationService;
    }

    @GetMapping("/{idPersona}")
    public ResponseEntity<?> buscarPersonaPorId(@PathVariable int idPersona) {
        Persona persona = personaService.obtenerPersonaPorId(idPersona);
        return ResponseEntity.ok(persona);
    }

    // PRUEBA
    @GetMapping("/numeroCuenta/{numCuenta}")
    public ResponseEntity<?> buscarNumeroCuenta(@PathVariable String numCuenta) {
        Persona persona = personaService.obtenerPersonaPorNumeroCuenta(numCuenta);
        return ResponseEntity.ok(persona);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarPersonas() {
        return ResponseEntity.ok(personaService.obtenerPersonas());
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registroPersona(@Valid @RequestBody RegistroPersonaDto datos) {
        applicationService.registrarPersona(datos);

        logger.info("El registro se realizo correctamente");
        return ResponseEntity.ok(Map.of("Mensaje", "Registro exitoso"));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarPersona(@AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ActualizarPersonaDto actualizarPersonaDto) {

        applicationService.actualizarPersona(user.getId(), actualizarPersonaDto);

        logger.info("Los datos personales fueron actualizados correctamente");
        return ResponseEntity.ok(Map.of("Mensaje", "Actualizacion exitosa"));
    }

    @DeleteMapping("/{idPersona}")
    public ResponseEntity<?> eliminarPersona(@PathVariable int idPersona) {

        personaService.eliminarPersona(idPersona);
        return ResponseEntity.ok(Map.of("Mensaje", "Persona eliminada"));
    }
}
