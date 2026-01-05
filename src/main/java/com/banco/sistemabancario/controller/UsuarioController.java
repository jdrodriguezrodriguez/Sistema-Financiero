package com.banco.sistemabancario.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.dto.MailReset.ForgotRequest;
import com.banco.sistemabancario.dto.MailReset.ResetPasswordTokenDto;
import com.banco.sistemabancario.security.controller.CustomUserDetails;
import com.banco.sistemabancario.service.MailResetService.ResetTokenService;
import com.banco.sistemabancario.serviceImpl.DatosDTOServiceImpl;
import com.banco.sistemabancario.serviceImpl.UsuarioServiceImpl;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/sistema/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private UsuarioServiceImpl usuarioService;
    private DatosDTOServiceImpl datosDTOService;
    private ResetTokenService resetTokenService;

    public UsuarioController(UsuarioServiceImpl usuarioService, DatosDTOServiceImpl datosDTOService,
            ResetTokenService resetTokenService) {
        this.usuarioService = usuarioService;
        this.datosDTOService = datosDTOService;
        this.resetTokenService = resetTokenService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> CurrentUser(@AuthenticationPrincipal CustomUserDetails user) {

        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("idUsuario", user.getId());
        userInfo.put("username", user.getUsername());

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/profile/datos")
    public ResponseEntity<?> datosSesionAutenticada(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(datosDTOService.datosUsuario(user.getId()));
    }

    @GetMapping("/{idPersona}")
    public ResponseEntity<?> getMethodName(@PathVariable int idPersona) {
        return usuarioService.obtenerUsuarioPorPersonaId(idPersona)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarUsuario(@AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ActualizarUsuarioDto datos) {

        try {
            usuarioService.actualizarDatosUsuario(datos, user.getId());
            logger.info("Usuario actualizado correctamente");
            return ResponseEntity.ok(Map.of("Mensaje", "Actualizacion exitosa. Por favor, vuelve a iniciar sesión"));

        } catch (NoSuchElementException e) {
            logger.error("Error al actualizar al usuario", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Validacion fallida", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgotUsername")
    public ResponseEntity<?> forgotUsernameUsuario(@RequestBody ForgotRequest request) {
        resetTokenService.forgotUsernameUsuario(request);

        return ResponseEntity.ok(Map.of("Mensaje", "Si el correo es valido, se envio el token al correo."));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> TokenPasswordUsuario(@RequestBody ForgotRequest request) {
        resetTokenService.almacenarTokenPassword(request);

        return ResponseEntity.ok(Map.of("Mensaje", "Si el correo es valido, se envio el token al correo."));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPasswordUsuario(@RequestBody ResetPasswordTokenDto rTokenDto) {
        resetTokenService.resetPassword(rTokenDto);

        return ResponseEntity.ok(Map.of("Mensaje", "Token aceptado."));
    }
}
