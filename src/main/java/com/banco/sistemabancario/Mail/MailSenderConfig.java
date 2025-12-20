package com.banco.sistemabancario.Mail;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.banco.sistemabancario.controller.TransaccionController;

@Configuration
public class MailSenderConfig {

    private static final Logger logger =  LoggerFactory.getLogger(TransaccionController.class);

    @Value("${spring.mail.username}")
    private String emailBanco;

    @Value("${spring.mail.password}")
    private String passBanco;
   
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
