package com.inventory.service;

import org.junit.jupiter.api.Test;

public class EmailServiceTest {

    @Test
    public void testSendEmail() {
        EmailService emailService = new EmailService();
        // Sending an email to yourself to test
        emailService.sendEmail("andrewzakhari5@gmail.com", "Test Email from Inventory System", "This is a test email to verify the EmailService is working correctly.");
    }
}
