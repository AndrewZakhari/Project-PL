package com.inventory.service;

import io.github.cdimascio.dotenv.Dotenv;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private final String username = "andrewzakhari5@gmail.com";
    private String password;

    public EmailService() {
        try {
            Dotenv dotenv = Dotenv.load();
            this.password = dotenv.get("APP_PASSWORD");
        } catch (Exception e) {
            System.err.println("Could not load .env file. Ensure it exists and contains APP_PASSWORD.");
            // Fallback or handle error appropriately
            this.password = System.getenv("APP_PASSWORD");
        }
    }

    public void sendEmail(String to, String subject, String body) {
        if (password == null || password.isEmpty()) {
            System.err.println("Error: APP_PASSWORD is not set.");
            return;
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully to: " + to);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
