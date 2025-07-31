package com.banco.sistemabancario.Controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.banco.sistemabancario.Service.TransaccionService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;


    //DEPOSITAR DINERO
    @PostMapping("/transaccion/depositar")
    public String depositarDinero(@RequestParam String valor, HttpSession session){

        try {

            Integer idPersona = (Integer) session.getAttribute("idPersona");
            transaccionService.depositar(idPersona, valor);

            return "redirect:/index.html";

        } catch (NoSuchElementException e) {
            System.out.print("Error al depositar: " + e);
            return "redirect:/index.html?error";
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
            System.out.println("Error al consultar: " + e);  
            return null; 
        }
    }
    
}