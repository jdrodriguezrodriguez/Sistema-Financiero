package com.banco.sistemabancario.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class TestRolesController {
    
    @GetMapping("/accessAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accesAdmin() {
        return "Rol de admin";
    }
    
    @GetMapping("/accessEmpleado")
    public String accesEmpleado() {
        return "Rol de empleado";
    }

    @GetMapping("/accessCliente")
    public String accesCliente() {
        return "Rol de cliente";
    }
}
