package com.banco.sistemabancario.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.security.controller.CustomUserDetails;
import com.banco.sistemabancario.serviceImpl.DatosDTOServiceImpl;
import com.banco.sistemabancario.serviceImpl.UsuarioServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/sistema/usuarios")
public class UsuarioController {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private UsuarioServiceImpl usuarioService;
    private DatosDTOServiceImpl datosDTOService;

    public UsuarioController(UsuarioServiceImpl usuarioService, DatosDTOServiceImpl datosDTOService) {
        this.usuarioService = usuarioService;
        this.datosDTOService = datosDTOService;
    }

    //SESION AUTENTICADA
    @GetMapping("/profile")
    public ResponseEntity<?> CurrentUser(@AuthenticationPrincipal CustomUserDetails user) {

        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());

        return ResponseEntity.ok(userInfo);
    }
    

    //CONSULTAS
    @GetMapping("/{idPersona}")
    public ResponseEntity<?> getMethodName(@PathVariable int idPersona) {
        return usuarioService.obtenerUsuarioPorId(idPersona)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<?> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }

    //ACTUALIZAR
    @PutMapping("/{idPersona}")
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
    //AUTENTICAR
    @PostMapping("/autenticar")
    public ResponseEntity<?> login(@RequestBody LoginUsuarioDto datos, HttpSession session) {
        
        Usuario usuario = usuarioService.autenticar(datos);

        session.setAttribute("idPersona", usuario.getPersona().getIdPersona());
        
        return ResponseEntity.ok(Map.of("Mensaje", "Acceso correcto"));
    }

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
