package com.ing_software.tp.service;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.model.OrderProduct;
import com.ing_software.tp.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    @Value("${spring.mail.username}")
    private String senderEmail;

    private final JavaMailSender mailSender;

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

    public String buildOrderConfirmationEmail(Order order) {
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("¡Gracias por tu compra!\n\n")
                .append("Detalles de la orden:\n")
                .append("Número de Orden: ").append(order.getId()).append("\n\n");

        emailBody.append("Productos:\n");

        for (OrderProduct item : order.getProducts()) {
            emailBody.append("- ").append(item.getProduct_name())
                    .append(" (Cantidad: ").append(item.getQuantity())
                    .append(")\n");
        }
                emailBody.append("Tu orden ha sido confirmada y se está preparando para su envío.\n")
                        .append("Recorda que tenes 24hs para cancelar el pedido.\n")
                .append("¡Gracias por comprar con nosotros!");

        return emailBody.toString();
    }
}