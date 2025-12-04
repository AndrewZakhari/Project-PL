package com.inventory.service;
import java.io.FileWriter;
import java.io.IOException;
import com.inventory.model.Client;
import com.inventory.model.Order;

public class ClientService {

    private static final String FILE_PATH = "clients.txt";

    public void registerClient(Client client) {

        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }

        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(client.toString() + System.lineSeparator());
            System.out.println("Client registered successfully!");
        } catch (IOException e) {
            System.out.println("Error saving client: " + e.getMessage());
        }
    }

        
    public void editClient(Client client) {
     
    }

    public void createPurchaseOrder(Order order) {
        // File handling: Add order
    }

    public void generateInvoice(Order order) {
        // Generate invoice
    }

    public void generateOrderReport(Client client) {
        // Analyze orders for client
    }

    public void sendOrderNotification(Order order) {
        // Sending emails using java
    }
}

