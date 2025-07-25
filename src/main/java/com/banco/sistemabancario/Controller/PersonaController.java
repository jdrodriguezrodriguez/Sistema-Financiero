package com.banco.sistemabancario.Controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.sistemabancario.Dto.RegistroPersonaDTO;
import com.banco.sistemabancario.Service.PersonaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PersonaController {
    
    @Autowired
    private PersonaService personaService;

    //REGISTRAR
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

    //ACTUALIZAR
    @PostMapping("/actualizarPersona")
    public String actualizarDatosPersona(@RequestParam String nombre,
                                        @RequestParam String apellido,
                                        @RequestParam String correo,
                                        @RequestParam String nacimiento,  HttpSession session){
      
        Integer idPersona = (Integer) session.getAttribute("idPersona");

        try{
            personaService.actualizarPersona(nombre, apellido, correo, nacimiento, idPersona);
            System.out.println("Datos personales actualizados.");
            return "redirect:/index.html";
            
        }catch(NoSuchElementException e){
            System.out.println("Error en actualizar los datos personales: " + e.getMessage());
            return "redirect:/update.html?error=";
        }
    }
}
