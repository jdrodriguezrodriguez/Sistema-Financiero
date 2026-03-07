package com.banco.sistemabancario.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banco.sistemabancario.dto.admin.ActualizarEstadoAdmin;
import com.banco.sistemabancario.dto.admin.ActualizarUsuarioAdmin;
import com.banco.sistemabancario.dto.admin.CrearUsuarioAdmin;
import com.banco.sistemabancario.service.admin.AdminService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Administracion", description = "Operaciones administrativas para la gestion de usuarios del sistema.")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/sistema/admin")
public class AdminController {

    private AdminService adminService;
    AdminController(AdminService adminService){
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

    @PostMapping("/usuario/crear")
    public ResponseEntity<?> adminCrearUsuario(@RequestBody CrearUsuarioAdmin datos) {
        adminService.adminCrearUsuario(datos);
        return ResponseEntity.ok(Map.of("Mensaje", "Se creo la persona correctamente."));
    }

    @PutMapping("/usuario/estado")
    public ResponseEntity<?> adminActualizarEstado(@RequestBody ActualizarEstadoAdmin datos) {
        adminService.adminActualizarEstado(datos);
        return ResponseEntity.ok(Map.of("Mensaje", "Se actualizo el estado del usuario."));
    }

    @DeleteMapping("/usuario/eliminar")
    public ResponseEntity<?> adminEliminarUsuario(@RequestParam String documento) {
        adminService.adminEliminarUsuario(documento);
        return ResponseEntity.ok(Map.of("Mensaje", "Se elimino a la persona."));
    }
}
