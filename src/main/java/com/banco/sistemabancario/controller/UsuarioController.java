package com.banco.sistemabancario.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.banco.sistemabancario.dto.DatosDto;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.service.DatosDTOService;
import com.banco.sistemabancario.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private UsuarioService usuarioService;
    private DatosDTOService datosDTOService;

    public UsuarioController(UsuarioService usuarioService, DatosDTOService datosDTOService) {
        this.usuarioService = usuarioService;
        this.datosDTOService = datosDTOService;
    }
    
    //REDIRIGIR AL LOGIN
    @GetMapping("/")
    public String redirigirAlLogin() {
        return "redirect:/login.html";
    }
  
    //LIMPIAR SESION
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login.html";
    }

    //INICIAR SESION
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        
        Usuario usuario = usuarioService.autenticar(username, password);

        if (usuario != null) {
            session.setAttribute("idPersona", usuario.getPersona().getIdPersona());
            return "redirect:/index.html";
        }else {
            logger.error("Error al iniciar sesion");
            return "redirect:/login.html?error";
        }
    }

    //DATOS DEL USUARIO EN LINEA
    @GetMapping("/api/datos")
    @ResponseBody
    public DatosDto obtenerDatos(HttpSession session){
        Integer idPersona = (Integer) session.getAttribute("idPersona");
        return datosDTOService.datosUsuario(idPersona);
    }

    //ACTUALIZAR USUARIO
    @PostMapping("/actualizarUsuario")
    public String actualizarDatosUsuario(@RequestParam String username, @RequestParam String password, HttpSession session){
        Integer idPersona = (Integer) session.getAttribute("idPersona");

        try {
            usuarioService.actualizarUsuario(username, password, idPersona);
            logger.info("Usuario actualizado correctamente");
            return "redirect:/index.html";
        } catch (NoSuchElementException e) {
            logger.error("Error al actualizar al usuario", e);
            return "redirect:/update.html?error=notfound";
        } catch (IllegalArgumentException e) {
            logger.error("Validacion fallida", e);
            return "redirect:/update.html?error=username";
        }
    }
}
