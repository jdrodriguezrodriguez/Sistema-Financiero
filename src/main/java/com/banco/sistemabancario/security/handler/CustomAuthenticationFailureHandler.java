package com.banco.sistemabancario.security.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    final ObjectMapper mapper = new ObjectMapper();
    LocalDateTime fechDateTime = LocalDateTime.now();
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        
        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", fechDateTime.format(formato));
        body.put("path", request.getRequestURI());
        body.put("status", 401);

        if (exception instanceof BadCredentialsException) {
             body.put("error", "User/Password incorrectos");
        }else if(exception instanceof LockedException){
            body.put("error", "Usuario bloqueado");
        }else if(exception instanceof DisabledException){
            body.put("error", "Usuario deshabiliado");
        }else if(exception instanceof CredentialsExpiredException){
            body.put("error", "Las credenciales han expirado");
        }else{
            body.put("error", "Datos incorrectos");
        }

        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
