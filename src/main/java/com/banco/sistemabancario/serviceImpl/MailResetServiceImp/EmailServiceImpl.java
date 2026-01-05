package com.banco.sistemabancario.serviceImpl.MailResetServiceImp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Transaccion;
import com.banco.sistemabancario.service.PersonaService;
import com.banco.sistemabancario.service.UsuarioService;
import com.banco.sistemabancario.service.MailResetService.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PersonaService personaService;

    @Override
    public void enviarResetPassword(String email, String token) {

        try {
            String link = frontendUrl + "/resetPassword.html?token=" + token;
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(email);
            mensaje.setSubject("Recuperar contraseña - BancoLess");
            mensaje.setText("Ingresa al siguiente enlace para continuar con el proceso de cambio de contraseña:\n\n"
                    + link + "\n\nEste enlace tiene una validez limitada.");

            javaMailSender.send(mensaje);

            logger.info("Inicio reset de password por correo: {}", email);

        } catch (Exception e) {
            logger.error("Error con el metodo enviarResetPassword" + e);
        }
    }

    @Override
    public void enviarUsername(String email, String username) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(email);
            mensaje.setSubject("Recuperar usuario - BancoLess");
            mensaje.setText("El nombre de usuario registrado en BancoLess es: \n\n " + username);

            javaMailSender.send(mensaje);

            logger.info("Inicio envio de username por correo: {}", email);
        } catch (Exception e) {
            logger.error("Error con el metodo enviarResetUsername" + e);
        }
    }

    @Override
    public void notificarTransaccion(Transaccion transaccion, String cuentaEmisor, String cuentaReceptor) {
        String emisorMsj = "Estimado cliente,\n\n" +
                "Le confirmamos que su transferencia fue procesada exitosamente.\n\n" +
                "Detalle de la transaccion:\n\n" +
                "- Monto transferido: $" + transaccion.getMonto().abs() + "\n" +
                "- Cuenta destino: " + transaccion.getCuenta_destino() + "\n" +
                "- Fecha de envio: " + transaccion.getFecha() + "\n\n" +
                "Gracias por confiar en BancoLess.";

        String receptorMsj = "Estimado cliente,\n\n" +
                "Le informamos que ha recibido una transferencia en su cuenta.\n\n" +
                "Detalle de la transacción:\n\n" +
                "- Monto recibido: $" + transaccion.getMonto().abs() + "\n" +
                "- Cuenta origen: " + transaccion.getCuenta().getNum_cuenta() + "\n" +
                "- Fecha de recepción: " + transaccion.getFecha() + "\n\n" +
                "Gracias por confiar en BancoLess.";

        Persona personaEmisor = personaService.obtenerPersonaPorNumeroCuenta(cuentaEmisor);
        Persona personaReceptor = personaService.obtenerPersonaPorNumeroCuenta(cuentaReceptor);

        if (personaEmisor == null || personaEmisor.getCorreo() == null ||
                personaReceptor == null || personaReceptor.getCorreo() == null) {
            logger.warn("No se pudo notificar la transacción. Emisor: {}, Receptor: {}",
                    cuentaEmisor, cuentaReceptor);
            return;
        }

        enviarInfoTransaccion(personaEmisor.getCorreo(), emisorMsj);
        enviarInfoTransaccion(personaReceptor.getCorreo(), receptorMsj);
    }

    @Override
    public void enviarInfoTransaccion(String correo, String mensaje) {

        SimpleMailMessage mail = new SimpleMailMessage();

        try {
            mail.setTo(correo);
            mail.setSubject("Registro de transaccion - BancoLess");
            mail.setText(mensaje);

            javaMailSender.send(mail);

            logger.info("Correo enviado correctamente a {}", correo);
        } catch (Exception e) {
            logger.error("Error en el método enviarInfoTransaccionEnvio", e);
        }
    }

    @Override
    public void enviarInfoDeposito(Transaccion transaccion, int idUser) {
        Persona persona = usuarioService.obtenerPersonaPorUsuarioId(idUser);
        SimpleMailMessage mensaje = new SimpleMailMessage();

        try {
            mensaje.setTo(persona.getCorreo());
            mensaje.setSubject("Registro de deposito - BancoLess");
            mensaje.setText("Estimado cliente,\n\n" +
                "Le confirmamos que su deposito fue realizado exitosamente.\n\n" +
                "Detalle del deposito:\n" +
                "-Monto transferido: $" + transaccion.getMonto().abs() + "\n" +
                "-Fecha de envio: " + transaccion.getFecha() + "\n\n" +
                "Gracias por confiar en BancoLess.");

            javaMailSender.send(mensaje);

            logger.info("Inicio envio datos de deposito al correo: {}", persona.getCorreo());
        } catch (Exception e) {
            logger.error("Error con el metodo enviarResetPassword" + e);
        }
    }

}
