package com.banco.sistemabancario.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.banco.sistemabancario.entity.Transaccion;
import com.banco.sistemabancario.service.TransaccionService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class TransaccionController {

    private static final Logger logger =  LoggerFactory.getLogger(TransaccionController.class);

    private TransaccionService transaccionService;
    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    //TRANSFERIR DINERO
    @PostMapping("/transaccion/transferir")
    public String transferirDinero(@RequestParam String valor, @RequestParam String cuentaDestino, @RequestParam String descripcion, HttpSession session) {
        try {
           
            Integer idPersona = (Integer) session.getAttribute("idPersona");
            transaccionService.transferir(idPersona, cuentaDestino, valor, descripcion);

            return "redirect:/index.html";

        } catch (NoSuchElementException e) {
            logger.error("Error al transferir el dinero", e);
            return "redirect:/index.html?error";
        }
    }

    //DEPOSITAR DINERO
    @PostMapping("/transaccion/depositar")
    public String depositarDinero(@RequestParam String valor, HttpSession session){

        try {
            
            Integer idPersona = (Integer) session.getAttribute("idPersona");
            transaccionService.depositar(idPersona, valor);

            return "redirect:/index.html";

        } catch (NoSuchElementException e) {
            logger.error("Error al depositar el dinero", e);
            return "redirect:/index.html?error";
        }
    }

    //CONSULTAR HISTORIAL
    @GetMapping("/transaccion/historial")
    @ResponseBody
    public List<Transaccion> consultarTransacciones(HttpSession session) {

        try {
            Integer idPersona = (Integer) session.getAttribute("idPersona");
            return transaccionService.transacciones(idPersona);
            
        } catch (NoSuchElementException e) {
            logger.error("Error al consultar el historial de transacciones", e);
            return Collections.emptyList(); 
        }
    }
    
    //CONSULTAR DINERO
    @GetMapping("/transaccion/consultar")
    @ResponseBody
    public Map<String, Object> consultarDinero(HttpSession session) {

        try {
            Integer idPersona = (Integer) session.getAttribute("idPersona");
            BigDecimal saldo = transaccionService.consultar(idPersona);

            Map<String, Object> reponse = new HashMap<>();
            reponse.put("saldo", saldo);

            return reponse;

        } catch (NoSuchElementException e) {
            logger.error("Error al consultar el dinero actual del usuario", e); 
            return Collections.emptyMap();
        }
    }
}