package com.banco.sistemabancario.Controller;

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
        if (user.equals(usuario.getNombre()) && pass.equals(usuario.getContraseña())) {
            System.out.println("Login exitoso");
            return "redirect:/index.html";
        } else {
            System.out.println("Login fallido");
            return "redirect:/login.html";
        }
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam String nombre, @RequestParam String documento, @RequestParam String correo, @RequestParam String celular, @RequestParam String contraseña) {
        if (nombre.isEmpty() && documento.isEmpty() && correo.isEmpty() && celular.isEmpty() && contraseña.isEmpty()) {
            System.out.println("Registro fallido");
            return "redirect:/registro.html";
        }

        int xdocumento = Integer.parseInt(documento);
        int xcelular = Integer.parseInt(celular);

        System.out.println("Registro exitoso");
            Persona usuario2 = new Persona(nombre, xdocumento, correo, xcelular, contraseña);

            System.out.println(usuario2.getNombre() + " " + usuario2.getCorreo() + " " + usuario2.getDocumento());
            return "redirect:/login.html";
    }
}
