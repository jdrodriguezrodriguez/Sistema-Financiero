package com.banco.sistemabancario.security.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.banco.sistemabancario.security.controller.CustomUserDetails;
import com.banco.sistemabancario.security.service.AuditorProvider;

@Component
public class CurrentUserProvider implements AuditorProvider{

    private static final Logger logger =  LoggerFactory.getLogger(CurrentUserProvider.class);
    // 0 == ACCION POR USUARIO
    @Override
    public Integer getCustomUserId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            logger.error("Authenticador vacio!");
            return 0;
        }

        if (auth.getPrincipal() instanceof CustomUserDetails user) {
            return user.getId();
        }

        logger.error("No se encontro algo o error de autenticador!");
        return 0;
    }
}
