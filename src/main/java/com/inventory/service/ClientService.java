package com.inventory.service;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

        

    public void editClient(Client updatedClient) {

    List<String> lines = new ArrayList<>();

    try {
        List<String> allLines = Files.readAllLines(Paths.get(FILE_PATH));

        for (String line : allLines) {
            String[] data = line.split(",");

            if (data[4].equals(updatedClient.getclientId())) {
                // Replace with updated client
                lines.add(updatedClient.toString());
            } else {
                lines.add(line);
            }
        }

        Files.write(Paths.get(FILE_PATH), lines);
        System.out.println("Client updated successfully!");

    } catch (IOException e) {
        System.out.println("Error editing client: " + e.getMessage());
    }
}


    public class OrderService {

    private static final String ORDER_FILE = "orders.txt";

    public void createOrder(Order order) {
        try (FileWriter writer = new FileWriter(ORDER_FILE, true)) {
            writer.write(order.toString() + System.lineSeparator());
            System.out.println("Order created successfully!");
        } catch (IOException e) {
            System.out.println("Error saving order: " + e.getMessage());
        }
    }
}

    public void generateInvoice(Order order, Client client) {
    String fileName = "invoice_" + order.orderId + ".txt";

    try (FileWriter writer = new FileWriter(fileName)) {
        writer.write("===== INVOICE =====\n");
        writer.write("Client: " + client.getname() + "\n");
        writer.write("Email: " + client.getemail() + "\n");
        writer.write("Address: " + client.getaddress() + "\n");
        writer.write("Phone: " + client.getphoneNumber() + "\n\n");

        writer.write("Order ID: " + order.orderId + "\n");
        writer.write("Product: " + order.productName + "\n");
        writer.write("Quantity: " + order.quantity + "\n");
        writer.write("Price per unit: " + order.price + "\n");
        writer.write("Total: " + (order.price * order.quantity) + "\n");
        writer.write("Date: " + order.date + "\n");
        writer.write("===================\n");

        System.out.println("Invoice generated: " + fileName);

    } catch (IOException e) {
        System.out.println("Error generating invoice: " + e.getMessage());
    }
}

        public void generateOrderReport() {
    try {
        List<String> orders = Files.readAllLines(Paths.get(ORDER_FILE));

        String fileName = "order_report.txt";
        FileWriter writer = new FileWriter(fileName);

        writer.write("===== ORDER REPORT =====\n\n");

        for (String line : orders) {
            String[] data = line.split(",");

            writer.write("Order ID: " + data[0] + "\n");
            writer.write("Client ID: " + data[1] + "\n");
            writer.write("Product: " + data[2] + "\n");
            writer.write("Quantity: " + data[3] + "\n");
            writer.write("Price: " + data[4] + "\n");
            writer.write("Date: " + data[5] + "\n");
            writer.write("------------------------\n");
        }

        writer.close();
        System.out.println("Order report generated: " + fileName);

    } catch (IOException e) {
        System.out.println("Error generating report: " + e.getMessage());
    }

   

    }
}


