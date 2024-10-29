package com.ing_software.tp.service;

public interface EmailSenderService {

    void sendConfirmationEmail(String to, String subject, String body);

}
