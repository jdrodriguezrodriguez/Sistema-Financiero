package com.banco.sistemabancario.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.banco.sistemabancario.events.EmailDepositoEvent;
import com.banco.sistemabancario.events.EmailForgotUsername;
import com.banco.sistemabancario.events.EmailResetPassword;
import com.banco.sistemabancario.events.EmailTokenRegistro;
import com.banco.sistemabancario.events.EmailTransaccionEvent;
import com.banco.sistemabancario.service.mailResetService.EmailService;

@Component
public class EmailEventListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailEventListener.class);

    private EmailService emailService;

    public EmailEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void emailTransaccion(EmailTransaccionEvent transaccionEvent) {
        try {
            emailService.notificarTransaccion(
                    transaccionEvent.getTransaccion(),
                    transaccionEvent.getCuentaEmisor(),
                    transaccionEvent.getCuentaReceptor());
        } catch (Exception e) {
            logger.error("Error al tomar el evento para notificar transaccion al email", e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void emailDeposito(EmailDepositoEvent depositoEvent) {
        try {
            emailService.enviarInfoDeposito(
                    depositoEvent.getTransaccion(),
                    depositoEvent.getIdUser());
        } catch (Exception e) {
            logger.error("Error al tomar el evento para notificar deposito al email", e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void emailResetPassword(EmailResetPassword resetPassword) {
        try {
            emailService.enviarResetPassword(resetPassword.getCorreo(), resetPassword.getToken());
        } catch (Exception e) {
            logger.error("Error al tomar el evento para enviar el token para la contraseña", e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void emailTokenRegistro(EmailTokenRegistro emailRegistro) {
        try {
            emailService.enviarTokenRegistro(
                    emailRegistro.getNombreCompleto(),
                    emailRegistro.getEmail(),
                    emailRegistro.getToken());
        } catch (Exception e) {
            logger.error("Error al tomar el evento para enviar el token para la contraseña", e);
        }
    }

    @Async
    @EventListener
    public void emailForgotUsername(EmailForgotUsername emailUsername) {
        try {
            emailService.enviarUsername(emailUsername.getCorreo(), emailUsername.getUsername());
        } catch (Exception e) {
            logger.error("Error al tomar el evento para enviar el token para la contraseña", e);
        }
    }
}
