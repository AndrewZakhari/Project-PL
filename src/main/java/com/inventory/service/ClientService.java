package com.inventory.service;

import com.inventory.model.Client;
import com.inventory.model.Order;
import com.inventory.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    
    private final FileService fileService = new FileService();
    private final EmailService emailService = new EmailService();
    private final ProductService productService = new ProductService();

    private static final String CLIENTS_FILE = "clients.txt";
    private static final String ORDERS_FILE = "orders.txt";


    public void registerClient(Client client) {
        fileService.appendToFile(CLIENTS_FILE, client.toString());
    }

    public List<Client> getAllClients(){
      List<String> lines = fileService.readAllLines(CLIENTS_FILE);
      List<Client> clients = new ArrayList<>();
      for (String line: lines) {
        if (!line.trim().isEmpty()){
          clients.add(Client.fromCsv(line));
        }
      }
      return clients;
    }

    public List<Order> getAllOrders() {
      List<String> lines = fileService.readAllLines(ORDERS_FILE);
      List<Order> orders = new ArrayList<>();
      for (String line: lines) {
        if (!line.trim().isEmpty()){
          orders.add(Order.fromCsv(line));
        }
      }
      return orders;
    }

    public void editClient( String oldEmail, Client newClient) {
        List<Client> clients = getAllClients();
        List<String> newLines = new ArrayList<>();

        for(Client c : clients) {
          if(c.getEmail().equals(oldEmail)){
            newLines.add(newClient.toString());
          } else {
            newLines.add(c.toString());
          }
        }
        fileService.overwriteFile(CLIENTS_FILE, newLines);
    }

    public void createPurchaseOrder(Order order) {
       fileService.appendToFile(ORDERS_FILE, order.toString());
       generateInvoiceAndNotify(order);
    }

    public void generateInvoiceAndNotify(Order order) {
      List<Product> products = productService.searchByName(order.getProductName());

      if(products.isEmpty()){
        System.out.println("Product not found for invoice: " + order.getProductName());
        return;
      }

      Product product = products.get(0);
      double totalCost = product.getPrice() * order.getQuantity();

      String subject = "Invoice for Order #" + order.getOrderId();
      String body = "Dear Client, \n\n" + "Thank you for your purchase. \n" + "Product: " + product.getName() + "\n" + "Total Cost : $" + totalCost
        + "\n\n" + "Best Regards, \nInventory System";

      emailService.sendEmail(order.getClientEmail(), subject, body);
    }

    public String generateOrderReport(Client client) {
       List<Order> orders = getAllOrders();
       StringBuilder report = new StringBuilder("---" + " Order Report for " + client.getName() + "---" + "\n");
       for ( Order order: orders) {
         if (order.getClientEmail().equals(client.getEmail())){
           report.append(order.toString()).append("\n");
         }
       }

       return report.toString();
    }

    
}