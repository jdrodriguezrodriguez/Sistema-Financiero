package com.banco.sistemabancario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/")
    public String redirigirAlLogin() {
        return "redirect:/login.html";
    }
  
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login.html?error";
    }

    @GetMapping("/api/usuario")
    @ResponseBody
    public Usuario obtenerUsuario(HttpSession session){
        return (Usuario) session.getAttribute("usuario");
    }

    @PostMapping("/login")
    public String Login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        
        Usuario usuario = usuarioService.autenticar(username, password);

        if (usuario != null) {
            session.setAttribute("usuario", usuario);
            return "redirect:/index.html";
        }else {
            System.out.println("Login fallido");
            return "redirect:/login.html?error";
        }
    }
}
