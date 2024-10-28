package com.ing_software.tp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    @Value("${spring.mail.username}")
    private String senderEmail;

    private JavaMailSender mailSender;
    public EmailSenderServiceImpl(JavaMailSender mailSender) {
         this.mailSender = mailSender;
     }
    public void sendConfirmationEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
