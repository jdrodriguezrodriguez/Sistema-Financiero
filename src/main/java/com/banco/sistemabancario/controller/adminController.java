package com.banco.sistemabancario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banco.sistemabancario.service.DatosDTOService;

@RestController
@RequestMapping("/api/sistema/admin")
public class adminController {

    private DatosDTOService datosDTOService;
    adminController(DatosDTOService datosDTOService){
        this.datosDTOService = datosDTOService;
    }
    
    @GetMapping("/userdatos")
    public ResponseEntity<?> datosAdminUsuarios(@RequestParam String documento){
        return ResponseEntity.ok(datosDTOService.adminBuscarUsuario(documento));
    }
}
