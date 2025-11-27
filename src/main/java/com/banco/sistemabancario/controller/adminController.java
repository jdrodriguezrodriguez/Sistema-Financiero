package com.banco.sistemabancario.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banco.sistemabancario.dto.Admin.ActualizarUsuarioAdmin;
import com.banco.sistemabancario.service.Admin.AdminService;

@RestController
@RequestMapping("/api/sistema/admin")
public class adminController {

    private AdminService adminService;
    adminController(AdminService adminService){
        this.adminService = adminService;
    }
    
    @GetMapping("/usuario/datos")
    public ResponseEntity<?> adminDatosUsuario(@RequestParam String documento){
        return ResponseEntity.ok(adminService.adminBuscarUsuario(documento));
    }

    @PutMapping("/usuario/actualizar")
    public ResponseEntity<?> adminActualizarUsuario(@RequestBody ActualizarUsuarioAdmin datos){
        adminService.adminActualizarUsuario(datos);
        return ResponseEntity.ok(Map.of("Mensaje", "Se actualizo el usuario correctamente."));
    }
}
