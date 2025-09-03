package com.banco.sistemabancario.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    //Exepcion para @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity <Map<String,String>> ExceptionHandler(MethodArgumentNotValidException ex){

        Map<String, String> errores  = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        
        /*List<String> errores = ex.getBindingResult().getFieldErrors().stream().map(error ->  error.getDefaultMessage()).toList();*/

        return ResponseEntity.badRequest().body(errores);
    }

    //Exepcion clase UsuarioNoRegistrado
    @ExceptionHandler(UsuarioNoRegistrado.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoRegistrado(UsuarioNoRegistrado ex){
        Map<String, String> errores = new HashMap<>();

        errores.put("error", "Usuario no registrado");
        errores.put("detalle", ex.getMessage());

        return ResponseEntity.status(404).body(errores);
    }

    //Exepcion clase CuentaNoencontrada
    @ExceptionHandler(CuentaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleCuentaNoencontradaException(CuentaNoEncontradaException ex){
        Map<String, String> errores = new HashMap<>();

        errores.put("error", "Cuenta no existe");
        errores.put("detalle", ex.getMessage());

        return ResponseEntity.status(404).body(errores);
    }

    //Exepcion claseIllegalArgument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> hanldeIllegalArgumentException(IllegalArgumentException ex){
        Map<String, String> errores = new HashMap<>();

        errores.put("error", "Argumento o valor inapropiado.");
        errores.put("detalle", ex.getMessage());

        return ResponseEntity.badRequest().body(errores);
    }

    //Exepcion clase SaldoInsuficiente
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Map<String, String>> hanldeSaldoInsuficienteException(SaldoInsuficienteException ex){
        Map<String, String> errores = new HashMap<>();

        errores.put("error", "Saldo insuficiente");
        errores.put("detalle", ex.getMessage());

        return ResponseEntity.badRequest().body(errores);
    }

    //Documento ya registrado
    @ExceptionHandler(DocumentoYaRegistradoException.class)
    public ResponseEntity<Map<String, String>> handleDocumentoYaRegistrado(DocumentoYaRegistradoException ex) {
        Map<String, String> errores = new HashMap<>();

        errores.put("error", "Documento ya registrado");
        errores.put("detalle", ex.getMessage());

        return ResponseEntity.badRequest().body(errores);
    }

    //Correo ya registrado
    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ResponseEntity<Map<String, String>> handleCorreoYaRegistrado(CorreoYaRegistradoException ex) {
        Map<String, String> errores = new HashMap<>();

        errores.put("error", "Correo ya registrado");
        errores.put("detalle", ex.getMessage());

        return ResponseEntity.badRequest().body(errores);
    }

    //Password invalida
    @ExceptionHandler(PasswordInvalidaException.class)
    public ResponseEntity<Map<String, String>> handlePasswordInvalida(PasswordInvalidaException ex) {
        Map<String, String> errores = new HashMap<>();

        errores.put("error", "Contraseña inválida");
        errores.put("detalle", ex.getMessage());
        
        return ResponseEntity.badRequest().body(errores);
    }

}
