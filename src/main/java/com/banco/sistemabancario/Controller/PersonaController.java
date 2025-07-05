package com.banco.sistemabancario.Controller;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Service.PersonaService;

@Controller
public class PersonaController {
    
    @Autowired
    private PersonaService personaService;
    
    @GetMapping("/")
    public String redirigirAlLogin() {
        return "redirect:/login.html";
    }
  
    @PostMapping("/index")
    public String iniciar(@RequestParam String user, @RequestParam String pass) {
        if (personaService.Login(user, pass)) {
            System.out.println("Login exitoso");
            return "redirect:/index.html";
        } else {
            System.out.println("Login fallido");
            return "redirect:/login.html?error";
        }
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String documento, @RequestParam String nacimiento, @RequestParam String correo) {
        if (nombre.isEmpty() && documento.isEmpty() && documento.isEmpty() && nacimiento.isEmpty() && correo.isEmpty()) {
            System.out.println("Registro fallido");
            return "redirect:/registro.html?error";
        }

        LocalDate fechanacimiento = LocalDate.parse(nacimiento);

        Persona persona = new Persona();

        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setDocumento(documento);
        persona.setNacimiento(Date.valueOf(fechanacimiento));
        persona.setCorreo(correo);

        personaService.registrar(persona);

        System.out.println("Registro exitoso");
        return "redirect:/login.html";
    }
}
