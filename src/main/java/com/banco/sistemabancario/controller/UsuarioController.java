package com.banco.sistemabancario.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.dto.DatosDto;
import com.banco.sistemabancario.dto.LoginUsuarioDto;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.service.DatosDTOService;
import com.banco.sistemabancario.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;


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

    //REDIRIGIR AL LOGIN
    @GetMapping("/index")
    @PreAuthorize("hashAuthority('CREATE') or hasAuthority('READ')")  
    public String regirigirIndex() {
        return "redirect:/index.html";
    }

    //REDIRIGIR AL ADMIN
    @GetMapping("/admin")
    public String regirigirAdmin() {
        return "redirect:/admin.html";
    }
  
    //LIMPIAR SESION
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login.html";
    }

    //INICIAR SESION
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUsuarioDto datos, HttpSession session) {
        
        Usuario usuario = usuarioService.autenticar(datos);

        session.setAttribute("idPersona", usuario.getPersona().getIdPersona());
        
        return ResponseEntity.ok(Map.of("Mensaje", "Acceso correcto"));
    }

    //DATOS DEL USUARIO EN LINEA
    @GetMapping("/api/datos")
    @ResponseBody
    public DatosDto obtenerDatos(HttpSession session){
        Integer idPersona = (Integer) session.getAttribute("idPersona");
        return datosDTOService.datosUsuario(idPersona);
    }

    //ACTUALIZAR USUARIO
    @PutMapping("/actualizarUsuario")
    public ResponseEntity<?> actualizarDatosUsuario(@Valid @RequestBody ActualizarUsuarioDto datos, HttpSession session){
        
        Integer idPersona = (Integer) session.getAttribute("idPersona");
        if(idPersona == null){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesion no valida.");
        }

        try {
            usuarioService.actualizarUsuario(datos, idPersona);
            logger.info("Usuario actualizado correctamente");
            return ResponseEntity.ok(Map.of("Mensaje", "Actualizacion exitosa."));

        } catch (NoSuchElementException e) {
            logger.error("Error al actualizar al usuario", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Validacion fallida", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
