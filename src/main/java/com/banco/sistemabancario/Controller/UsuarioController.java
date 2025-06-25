package com.banco.sistemabancario.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

  
    @@GetMapping("/login")
    public String login(Usuario usuario) {
        return "login";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre, String contraseña, Usuario Usuario){

    }


}
