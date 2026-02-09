package com.banco.sistemabancario.service.auditoria;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.enums.AuditoriaActionEnums;
import com.banco.sistemabancario.entity.events.AuditoriaEntity;
import com.banco.sistemabancario.repository.auditoriaRepository.AuditLogRepository;

@Service
public class AuditoriaEventosService {

    private static final Logger logger =  LoggerFactory.getLogger(AuditoriaEventosService.class);

    private final AuditLogRepository auditLogRepository;
    

    public AuditoriaEventosService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(AuditoriaActionEnums actionEnums,
            Integer performedBy,    
            Integer targetId,
            Map<String, Object> cambios) {

        try {
            AuditoriaEntity auditoriaEvents = new AuditoriaEntity();

            auditoriaEvents.setAccion(actionEnums);
            auditoriaEvents.setPerformedBy(performedBy);
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
