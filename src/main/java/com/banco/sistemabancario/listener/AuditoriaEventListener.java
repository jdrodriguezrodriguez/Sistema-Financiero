package com.banco.sistemabancario.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.banco.sistemabancario.events.AdminAuditoriaEvent;
import com.banco.sistemabancario.service.auditoria.AuditoriaEventosService;

@Component
public class AuditoriaEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaEventListener.class);

    private AuditoriaEventosService auditoriaEventosService;

    public AuditoriaEventListener(AuditoriaEventosService auditoriaEventosService) {
        this.auditoriaEventosService = auditoriaEventosService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void auditorioCambiosAdmin(AdminAuditoriaEvent aEvents) {

        try {
            auditoriaEventosService.log(
                    aEvents.getAccion(),
                    aEvents.getPerformedBy(),
                    aEvents.getTargetId(),
                    aEvents.getCambios());
        } catch (Exception e) {
            logger.error("Error al tomar el evento para auditoria administrativa ", e);
        }
    }
}
