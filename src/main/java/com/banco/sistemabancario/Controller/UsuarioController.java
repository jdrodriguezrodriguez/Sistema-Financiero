package com.banco.sistemabancario.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.sistemabancario.Entity.Usuario;

@Controller
public class UsuarioController {
    
    Usuario usuario = new Usuario("juan", "1234");
    
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

        System.out.println("Registro exitoso");
            return "redirect:/login.html";
    }
}
