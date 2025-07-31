package com.banco.sistemabancario.Controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.sistemabancario.Service.TransaccionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;


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
}