package com.inventory.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.inventory.model.Client;
import com.inventory.model.Order;

public class ClientService {

    private static final String CLIENT_FILE = "data/clients.txt";
    private static final String ORDER_FILE = "data/orders.txt";

    private void ensureDataFileExists(String filePath) throws IOException {
        Path p = Paths.get(filePath);
        Path parent = p.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(p)) {
            Files.createFile(p);
        }
    }

    public void registerClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }

        try {
            ensureDataFileExists(CLIENT_FILE);
            try (FileWriter writer = new FileWriter(CLIENT_FILE, true)) {
                writer.write(client.toString() + System.lineSeparator());
            }
            System.out.println("Client registered successfully!");
        } catch (IOException e) {
            System.out.println("Error saving client: " + e.getMessage());
        }
    }

    public void editClient(Client updatedClient) {
        if (updatedClient == null) {
            throw new IllegalArgumentException("updatedClient cannot be null");
        }

        List<String> lines = new ArrayList<>();

        try {
            ensureDataFileExists(CLIENT_FILE);
            List<String> allLines = Files.readAllLines(Paths.get(CLIENT_FILE));

            for (String line : allLines) {
                String[] data = line.split(",");

                // defensive: check that ID exists in expected position
                String existingId = data.length > 4 ? data[4] : "";
                String updatedId = "";
                try {
                    updatedId = updatedClient.getclientId() != null ? updatedClient.getclientId() : "";
                } catch (Exception e) {
                    updatedId = "";
                }

                if (!existingId.isEmpty() && existingId.equals(updatedId)) {
                    lines.add(updatedClient.toString());
                } else {
                    lines.add(line);
                }
            }

            Files.write(Paths.get(CLIENT_FILE), lines);
            System.out.println("Client updated successfully!");

        } catch (IOException e) {
            System.out.println("Error editing client: " + e.getMessage());
        }
    }

    public void generateInvoice(Order order, Client client) {
        if (order == null || client == null) {
            throw new IllegalArgumentException("Order and Client are required");
        }

        String fileName;
        try {
            // try common getter first, fall back to toString-based name
            int orderId = -1;
            try {
                orderId = order.getOrderId();
            } catch (Exception e) {
                // ignore, fallback
            }
            fileName = "invoice_" + (orderId >= 0 ? orderId : Math.abs(order.hashCode())) + ".txt";

            ensureDataFileExists(fileName); // ensure path (no parent usually)
        } catch (IOException e) {
            System.out.println("Error preparing invoice file: " + e.getMessage());
            return;
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("===== INVOICE =====\n");

            // Client info (uses existing getters as in current model)
            try {
                writer.write("Client: " + client.getname() + "\n");
            } catch (Exception e) {
                writer.write("Client: " + client.toString() + "\n");
            }
            try { writer.write("Email: " + client.getemail() + "\n"); } catch (Exception ignored) {}
            try { writer.write("Address: " + client.getaddress() + "\n"); } catch (Exception ignored) {}
            try { writer.write("Phone: " + client.getphoneNumber() + "\n\n"); } catch (Exception ignored) {}

            // Order info - try getters, otherwise write toString()
            try {
                writer.write("Order ID: " + order.getOrderId() + "\n");
            } catch (Exception e) {
                writer.write("Order: " + order.toString() + "\n");
            }

            // Attempt common order fields if getters exist
            try { writer.write("Product: " + order.getProductName() + "\n"); } catch (Exception ignored) {}
            try { writer.write("Quantity: " + order.getQuantity() + "\n"); } catch (Exception ignored) {}
            try { writer.write("Price: " + order.getPrice() + "\n"); } catch (Exception ignored) {}
            try { writer.write("Date: " + order.getOrderDate() + "\n"); } catch (Exception ignored) {}

            writer.write("===================\n");
            System.out.println("Invoice generated: " + fileName);
        } catch (IOException e) {
            System.out.println("Error generating invoice: " + e.getMessage());
        }
    }

    public void createOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        try {
            ensureDataFileExists(ORDER_FILE);
            try (FileWriter writer = new FileWriter(ORDER_FILE, true)) {
                writer.write(order.toString() + System.lineSeparator());
            }
            System.out.println("Order created successfully!");
        } catch (IOException e) {
            System.out.println("Error saving order: " + e.getMessage());
        }
    }

    public void generateOrderReport() {
        try {
            ensureDataFileExists(ORDER_FILE);
            List<String> orders = Files.readAllLines(Paths.get(ORDER_FILE));

            String fileName = "order_report.txt";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("===== ORDER REPORT =====\n\n");

                for (String line : orders) {
                    String[] data = line.split(",");

                    String orderId = data.length > 0 ? data[0] : "";
                    String clientId = data.length > 1 ? data[1] : "";
                    String product = data.length > 2 ? data[2] : "";
                    String quantity = data.length > 3 ? data[3] : "";
                    String price = data.length > 4 ? data[4] : "";
                    String date = data.length > 5 ? data[5] : "";

                    writer.write("Order ID: " + orderId + "\n");
                    writer.write("Client ID: " + clientId + "\n");
                    writer.write("Product: " + product + "\n");
                    writer.write("Quantity: " + quantity + "\n");
                    writer.write("Price: " + price + "\n");
                    writer.write("Date: " + date + "\n");
                    writer.write("------------------------\n");
                }
            }

            System.out.println("Order report generated: " + fileName);

        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
}


