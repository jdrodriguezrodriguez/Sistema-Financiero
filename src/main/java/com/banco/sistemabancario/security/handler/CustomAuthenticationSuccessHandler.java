package com.banco.sistemabancario.security.handler;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.banco.sistemabancario.serviceImpl.LoginFailureService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private LoginFailureService loginFailureService;

    public CustomAuthenticationSuccessHandler(LoginFailureService loginFailureService) {
        this.loginFailureService = loginFailureService;
    } 

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        response.setContentType("application/json");

        String username = authentication.getName();
        loginFailureService.limpiarUsuarioFailer(username);
    }
}
