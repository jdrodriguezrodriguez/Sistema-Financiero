package com.banco.sistemabancario.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.dto.LoginUsuarioDto;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.serviceImpl.DatosDTOService;
import com.banco.sistemabancario.serviceImpl.UsuarioServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/sistema/usuarios")
public class UsuarioController {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private UsuarioServiceImpl usuarioService;
    private DatosDTOService datosDTOService;

    public UsuarioController(UsuarioServiceImpl usuarioService, DatosDTOService datosDTOService) {
        this.usuarioService = usuarioService;
        this.datosDTOService = datosDTOService;
    }

    //LISTAR
    @GetMapping
    public ResponseEntity<?> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }

    //CREAR
    @PostMapping("/autenticar")
    public ResponseEntity<?> login(@RequestBody LoginUsuarioDto datos, HttpSession session) {
        
        Usuario usuario = usuarioService.autenticar(datos);

        session.setAttribute("idPersona", usuario.getPersona().getIdPersona());
        
        return ResponseEntity.ok(Map.of("Mensaje", "Acceso correcto"));
    }

    //ACTUALIZAR
    @PutMapping("/usuarios/{idPersona}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable int idPersona ,@Valid @RequestBody ActualizarUsuarioDto datos, HttpSession session){
        
        /*Integer idPersona = (Integer) session.getAttribute("idPersona");
        if(idPersona == null){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesion no valida.");
        }*/

        try {
            usuarioService.actualizarDatosUsuario(datos, idPersona);
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

    /* 
    //REDIRIGIR AL LOGIN
    @GetMapping("/")
    public String redirigirAlLogin() {
        return "redirect:/login.html";
    }

    //REDIRIGIR AL LOGIN
    @GetMapping("/index")
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
    
    //DATOS DEL USUARIO EN LINEA
    @GetMapping("/api/datos")
    @ResponseBody
    public DatosDto obtenerDatos(HttpSession session){
        Integer idPersona = (Integer) session.getAttribute("idPersona");
        return datosDTOService.datosUsuario(idPersona);
    }*/
}
