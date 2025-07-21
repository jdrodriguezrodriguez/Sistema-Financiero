package com.banco.sistemabancario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.sistemabancario.Dto.RegistroPersonaDTO;
import com.banco.sistemabancario.Service.PersonaService;

@Controller
public class PersonaController {
    
    @Autowired
    private PersonaService personaService;

    @PostMapping("/registro")
    public String registrar(@RequestParam String nombre, 
                            @RequestParam String apellido, 
                            @RequestParam String documento, 
                            @RequestParam String nacimiento, 
                            @RequestParam String correo, 
                            @RequestParam String password) {
       
        RegistroPersonaDTO datos = new RegistroPersonaDTO(nombre, apellido, documento, nacimiento, correo, password);
        
        try{

            if (personaService.registrarPersona(datos) != null) {
            System.out.println("Registro exitoso");
            return "redirect:/login.html";
            }else{
                return "redirect:/login.html?error=";
            }
            
        }catch(IllegalArgumentException e){
            System.out.println("Error en registro: " + e.getMessage());
            return "redirect:/login.html?error=" + e.getMessage();
        }        
    }
}
