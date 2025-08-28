package com.banco.sistemabancario.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(MethodArgumentNotValidException ex){
        Map<String, String> errores  = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(UsuarioNoRegistrado.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoRegistrado(UsuarioNoRegistrado ex){
        Map<String, String> errores = new HashMap<>();

        errores.put("error", "Usuario no registrado");
        errores.put("detalle", ex.getMessage());

        return ResponseEntity.status(404).body(errores);
    }

    @ExceptionHandler(CuentaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleCuentaNoencontradaException(CuentaNoEncontradaException ex){
        Map<String, String> errores = new HashMap<>();

        errores.put("error", "Cuenta no existe");
        errores.put("detalle", ex.getMessage());

        return ResponseEntity.status(404).body(errores);
    }
}
