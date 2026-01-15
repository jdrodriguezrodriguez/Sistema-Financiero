package com.banco.sistemabancario.service.Auditoria;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.banco.sistemabancario.entity.Events.AuditoriaEvents;
import com.banco.sistemabancario.entity.enums.AuditoriaActionEnums;
import com.banco.sistemabancario.repository.AuditoriaRepository.AuditLogRepository;
import com.banco.sistemabancario.security.controller.CustomUserDetails;

public class AuditoriaEventosService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void log(AuditoriaActionEnums actionEnums,
            int targetId,
            Map<String, Object> cambios) {

        try {

            AuditoriaEvents auditoriaEvents = new AuditoriaEvents();

            auditoriaEvents.setAccion(actionEnums);
            auditoriaEvents.setPerformedBy(getCustomUserId());
            auditoriaEvents.setTargetId(targetId);
            auditoriaEvents.setCambios(
                    cambios == null || cambios.isEmpty()
                            ? null
                            : cambios);

            auditLogRepository.save(auditoriaEvents);

        } catch (Exception e) {
            System.err.println("Error marcando cambio en el auditorio: " + e.getMessage());
        }
    }

    // 0 == ACCION POR USUARIO
    private Integer getCustomUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
                return 0;
            }

            CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

            return user.getId();
        } catch (Exception e) {
            System.err.println("Error al manejar el id autenticado" + e);
        }

        return null;
    }
}
