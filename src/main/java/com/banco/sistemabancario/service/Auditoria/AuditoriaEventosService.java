package com.banco.sistemabancario.service.Auditoria;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.Events.AuditoriaEntity;
import com.banco.sistemabancario.entity.enums.AuditoriaActionEnums;
import com.banco.sistemabancario.repository.AuditoriaRepository.AuditLogRepository;
import com.banco.sistemabancario.security.Service.AuditoriaUserProvider;

@Service
public class AuditoriaEventosService {

    private static final Logger logger =  LoggerFactory.getLogger(AuditoriaEventosService.class);

    private final AuditLogRepository auditLogRepository;
    private AuditoriaUserProvider userProvider;

    public AuditoriaEventosService(AuditLogRepository auditLogRepository, AuditoriaUserProvider userProvider) {
        this.auditLogRepository = auditLogRepository;
        this.userProvider = userProvider;
    }

    public void log(AuditoriaActionEnums actionEnums,
            int targetId,
            Map<String, Object> cambios) {

        try {
            AuditoriaEntity auditoriaEvents = new AuditoriaEntity();

            auditoriaEvents.setAccion(actionEnums);
            auditoriaEvents.setPerformedBy(userProvider.getCustomUserId());
            auditoriaEvents.setTargetId(targetId);
            auditoriaEvents.setCambios(
                    cambios == null || cambios.isEmpty()
                            ? null
                            : cambios);
            
            auditLogRepository.save(auditoriaEvents);
            
            logger.info("Se registro un cambio por roles administrativos");

        } catch (Exception e) {
            logger.error("Error marcando cambio en auditoría", e);
        }
    }
}
