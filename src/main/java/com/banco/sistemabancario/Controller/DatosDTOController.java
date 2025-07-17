package com.banco.sistemabancario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.banco.sistemabancario.Dto.DatosDTO;
import com.banco.sistemabancario.Service.DatosDTOService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DatosDTOController {

    @Autowired
    DatosDTOService datosDTOService;

    @GetMapping("/api/datos")
    @ResponseBody
    public DatosDTO obtenerDatos(HttpSession session){
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        return datosDTOService.datosUsuario(idUsuario);
    }
}