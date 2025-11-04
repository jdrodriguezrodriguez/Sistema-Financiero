package com.banco.sistemabancario.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banco.sistemabancario.dto.TransferirDineroDto;
import com.banco.sistemabancario.security.controller.CustomUserDetails;
import com.banco.sistemabancario.serviceImpl.TransaccionServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/sistema/transaccion")
public class TransaccionController {

    private static final Logger logger =  LoggerFactory.getLogger(TransaccionController.class);

    private TransaccionServiceImpl transaccionService;
    public TransaccionController(TransaccionServiceImpl transaccionService) {
        this.transaccionService = transaccionService;
    }

    //TRANSFERIR
    @PostMapping("/transferir/{idPersona}")
    public ResponseEntity<?> transferirDinero(@PathVariable int idPersona,@Valid @RequestBody TransferirDineroDto datos, HttpSession session) {

        try {
            transaccionService.transferir(idPersona, datos);

            return ResponseEntity.ok(Map.of("Mensaje", "Transaccion realizada con exito."));

        } catch (NoSuchElementException e) {
            logger.error("Error al transferir el dinero", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DEPOSITAR
    @PostMapping("/depositar")
    public ResponseEntity<?> depositarDinero(@RequestBody String valor, @AuthenticationPrincipal CustomUserDetails user){
        try {
            transaccionService.depositar(user.getId(), valor);
            return ResponseEntity.ok(Map.of("Mensaje", "Se ha depositado correctamente."));
        } catch (NoSuchElementException e) {
            logger.error("Error al depositar el dinero", e);
            return ResponseEntity.badRequest().body("Error al depositar el dinero: " + e);
        }
    }

    //CONSULTAR HISTORIAL
    @GetMapping("/historial")
    public ResponseEntity<?> consultarTransacciones(@AuthenticationPrincipal CustomUserDetails user) {
        try {
            return ResponseEntity.ok(transaccionService.transacciones(user.getId()));
            
        } catch (NoSuchElementException e) {
            logger.error("Error al consultar el historial de transacciones", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    //CONSULTAR DINERO
    @GetMapping("/saldo")
    public ResponseEntity<?> consultarDinero(@AuthenticationPrincipal CustomUserDetails user) {
        try {

            BigDecimal saldo = transaccionService.consultar(user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("saldo", saldo);

            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            logger.error("Error al consultar el dinero actual del usuario", e); 
            return ResponseEntity.badRequest().body("Error" + e);
        }
    }
}