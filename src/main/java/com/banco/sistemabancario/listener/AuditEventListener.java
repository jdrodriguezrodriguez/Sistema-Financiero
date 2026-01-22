package com.banco.sistemabancario.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.banco.sistemabancario.entity.Events.AuditoriaEvent;
import com.banco.sistemabancario.service.Auditoria.AuditoriaEventosService;

@Component
public class AuditEventListener {

    private static final Logger logger =  LoggerFactory.getLogger(AuditoriaEventosService.class);

    private AuditoriaEventosService auditoriaEventosService;

    public AuditEventListener(AuditoriaEventosService auditoriaEventosService){
        this.auditoriaEventosService = auditoriaEventosService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void auditorioCambios(AuditoriaEvent aEvents){

        try {
            auditoriaEventosService.log(
            aEvents.getAccion(), 
            aEvents.getPerformedBy(),
            aEvents.getTargetId(), 
            aEvents.getCambios());
        } catch (Exception e) {
            logger.error("Error al tomar el evento", e);
        }
    }
}
