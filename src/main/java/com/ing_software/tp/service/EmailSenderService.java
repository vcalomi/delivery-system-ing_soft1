package com.ing_software.tp.service;

import com.ing_software.tp.model.Order;

public interface EmailSenderService {

    void sendConfirmationEmail(String to, String subject, String body);
    String buildOrderConfirmationEmail(Order order);
}
