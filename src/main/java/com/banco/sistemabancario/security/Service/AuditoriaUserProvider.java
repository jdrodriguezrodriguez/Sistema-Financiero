package com.banco.sistemabancario.security.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.banco.sistemabancario.security.controller.CustomUserDetails;

@Component
public class AuditoriaUserProvider {

    // 0 == ACCION POR USUARIO
    public Integer getCustomUserId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return 0;
        }

        if (auth.getPrincipal() instanceof CustomUserDetails user) {
            return user.getId();
        }

        return 0;
    }
}
