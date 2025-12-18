package com.banco.sistemabancario.serviceImpl;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.banco.sistemabancario.controller.TransaccionController;
import com.banco.sistemabancario.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger =  LoggerFactory.getLogger(TransaccionController.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailBanco;

    @Value("${spring.mail.password}")
    private String passBanco;

    @Override
    public void enviarResetPassword(String email, String token) {

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(email);
            mensaje.setSubject("Recupera contraseña - BancoLess");
            mensaje.setText("Ultiliza el siguiente token para autenticar y cambiar tu contraseña: " + token);

            javaMailSender.send(mensaje);
        } catch (Exception e) {
            logger.error("Error con el metodo enviarResetPassword" + e);
        }
    }

    @Bean
    public JavaMailSender getJavaMailSender() {

        try {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

            mailSender.setUsername(emailBanco);
            mailSender.setPassword(passBanco);

            Properties props = mailSender.getJavaMailProperties();

            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            return mailSender;
        } catch (Exception e) {
            logger.error("Error con JavaMailSender:" + e);
        }

        return null;
    }
}
