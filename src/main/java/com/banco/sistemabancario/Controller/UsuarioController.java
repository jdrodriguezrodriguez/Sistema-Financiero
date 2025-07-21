package com.banco.sistemabancario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.banco.sistemabancario.Dto.DatosDTO;
import com.banco.sistemabancario.Entity.Usuario;
import com.banco.sistemabancario.Service.DatosDTOService;
import com.banco.sistemabancario.Service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    DatosDTOService datosDTOService;
    
    @GetMapping("/")
    public String redirigirAlLogin() {
        return "redirect:/login.html";
    }
  
    //LIMPIAR SESION
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login.html?error";
    }

    //INICIAR SESION
    @PostMapping("/login")
    public String Login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        
        Usuario usuario = usuarioService.autenticar(username, password);

        if (usuario != null) {
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            return "redirect:/index.html";
        }else {
            System.out.println("Login fallido");
            return "redirect:/login.html?error";
        }
    }

    //DATOS DEL USUARIO EN LINEA
    @GetMapping("/api/datos")
    @ResponseBody
    public DatosDTO obtenerDatos(HttpSession session){
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        return datosDTOService.datosUsuario(idUsuario);
    }
}
