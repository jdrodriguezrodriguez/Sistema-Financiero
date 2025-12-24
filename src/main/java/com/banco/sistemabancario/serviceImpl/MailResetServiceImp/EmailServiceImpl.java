package com.banco.sistemabancario.serviceImpl.MailResetServiceImp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.service.MailResetService.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger =  LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void enviarResetPassword(String email, String token) {

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(email);
            mensaje.setSubject("Recuperar contraseña - BancoLess");
            mensaje.setText("Ultiliza el siguiente token para autenticar y cambiar tu contraseña: " + token);

            javaMailSender.send(mensaje);

            logger.info("Inicio reset de password por correo: {}", email);

        } catch (Exception e) {
            logger.error("Error con el metodo enviarResetPassword" + e);
        }
    }

    @Override
    public void enviarResetUsername(String email, String token) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(email);
            mensaje.setSubject("Recuperar usuario - BancoLess");
            mensaje.setText("Ultiliza el siguiente token para autenticar y cambiar tu usuario: " + token);

            javaMailSender.send(mensaje);

            logger.info("Inicio reset de username por correo: {}", email);
        } catch (Exception e) {
            logger.error("Error con el metodo enviarResetUsername" + e);
        }
    }
}
