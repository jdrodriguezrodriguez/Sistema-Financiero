package com.banco.sistemabancario.Controller;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banco.sistemabancario.Entity.Persona;
import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Service.PersonaService;
import com.banco.sistemabancario.Service.UsuarioService;

@Controller
public class PersonaController {
    
    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
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
    public String registrar(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String documento, @RequestParam String nacimiento, @RequestParam String correo, @RequestParam String password) {
        if (nombre.isEmpty() && documento.isEmpty() && documento.isEmpty() && nacimiento.isEmpty() && correo.isEmpty() && password.isEmpty()) {
            System.out.println("Registro fallido");
            return "redirect:/registro.html?error";
        }

        String username = UsuarioService.GenerarUsername(nombre, apellido);
        LocalDate fechanacimiento = LocalDate.parse(nacimiento);

        Persona persona = new Persona();
        Usuario usuario = new Usuario();
        
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setDocumento(documento);
        persona.setNacimiento(Date.valueOf(fechanacimiento));
        persona.setCorreo(correo);

        personaService.registrar(persona);

        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setPersona(persona);
        if (usuario.getRol() == null) {
            usuario.setRol("CLIENTE");
        }

        usuarioService.registrar(usuario);

        

        System.out.println("Registro exitoso");
        return "redirect:/login.html";
    }
}
