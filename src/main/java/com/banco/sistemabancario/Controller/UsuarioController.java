package com.banco.sistemabancario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.sistemabancario.Service.UsuarioService;

@Controller
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/")
    public String redirigirAlLogin() {
        return "redirect:/login.html";
    }
  
    @PostMapping("/index")
    public String Login(@RequestParam String username, @RequestParam String password) {
        if (usuarioService.Login(username, password)) {
            return "redirect:/index.html";
        } else {
            System.out.println("Login fallido");
            return "redirect:/login.html?error";
        }
    }
}
