package com.inventory.pl_project.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "your-email@gmail.com"; // Configure this
    private static final String PASSWORD = "your-app-password"; // Configure this

    public static void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendOrderNotification(String clientEmail, String orderId, double totalAmount) {
        String subject = "Order Confirmation - " + orderId;
        String body = "Dear Customer,\n\n" +
                "Your order " + orderId + " has been confirmed.\n" +
                "Total Amount: $" + totalAmount + "\n\n" +
                "Thank you for your purchase!\n\n" +
                "Best regards,\nInventory Management System";

        sendEmail(clientEmail, subject, body);
    }

    public static void sendExpirationNotification(String adminEmail, String productName, int daysUntilExpiration) {
        String subject = "Product Expiration Warning";
        String body = "Dear Admin,\n\n" +
                "The product '" + productName + "' will expire in " + daysUntilExpiration + " days.\n" +
                "Please take necessary action.\n\n" +
                "Best regards,\nInventory Management System";

        sendEmail(adminEmail, subject, body);
    }
}
