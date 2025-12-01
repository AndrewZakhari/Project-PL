package com.inventory.service;

import com.inventory.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AdminService {
  private final FileService fileService = new FileService();
  private final ProductService productService = new ProductService();
  private final ClientService clientService = new ClientService();

  private static final String CATEGORIES_FILE = "categories.txt";
  private static final String SUPPLIERS_FILE = "suppliers.txt";
  private static final String OFFERS_FILE = "offers.txt";

    public void addCategory(Category category) {
      fileService.appendToFile(CATEGORIES_FILE, category.toString());
    }

    public List<Category> getAllCategories() {
      return fileService.readAllLines(CATEGORIES_FILE).stream().map(Category::new).collect(Collectors.toList());
    }

    public void updateCategory(Category category) {
        
    }

    public void deleteCategory(String categoryName) {
        // File handling
    }

    public void addSupplier(Supplier supplier) {
        fileService.appendToFile(SUPPLIERS_FILE, supplier.toString());
    }

    public List<Supplier> getAllSuppliers() {
      return fileService.readAllLines(SUPPLIERS_FILE).stream().map(Supplier::fromCsv).collect(Collectors.toList());
    }

    public void updateSupplier(Supplier supplier) {
        // File handling
    }

    public void deleteSupplier(String supplierName) {
        // File handling
    }

    public String generateProductReport() {
      List<Product> products = productService.getAllProducts();
      StringBuilder report = new StringBuilder("--- Product Inventory Report ---\n");
      for (Product p: products) {
        report.append(String.format("Name: %s | Qty: %d | Price: %.2f | Exp: %s\n"), p.getName(), p.getQuantity(), p.getPrice(), p.getExpirationDate());
      }
      return report.toString();
    }

    public void generateCategoryReport() {
        // Generate statistics for categories
    }

    public void addOffer(Offer offer) {
        fileService.appendToFile(OFFERS_FILE, offer.toString()); 
    }

    public String generateProfitReport() {
      List<Order> orders = clientService.getAllOrders();
      List<Product> products = productService.getAllProducts();

      double totalRevenue = 0;
      StringBuilder report = new StringBuilder("--- Sales & Profit Report ---\n");
      for (Order order: orders) {
        double price = products.stream().filter(p -> p.getName().equals(order.getProductName())).map(Product::getPrice).findFirst().orElse(0.0);
        double orderTotal = price * order.getQuantity();
        totalRevenue += orderTotal;

        report.append(String.format("Order ID: %s | Product: %s | Qty: %d | Total: $%.2f\n", order.getOrderId(), order.getProductName(), order.getQuantity(), orderTotal));

      }

      report.append("\n---------------\n");
      report.append(String.format("TOTAL REVENUE $%.2f", totalRevenue));

      return report.toString();
    }
}
