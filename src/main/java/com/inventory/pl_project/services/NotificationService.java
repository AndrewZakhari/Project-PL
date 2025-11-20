package com.inventory.pl_project.services;

import com.inventory.pl_project.models.Product;
import com.inventory.pl_project.utils.EmailService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationService {
    private static final int EXPIRATION_WARNING_DAYS = 7;
    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final String ADMIN_EMAIL = "admin@inventory.com"; // Configure this

    private ProductService productService;
    private ScheduledExecutorService scheduler;

    public NotificationService() {
        this.productService = new ProductService();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startDailyChecks() {
        // Run checks every 24 hours
        scheduler.scheduleAtFixedRate(this::checkExpirations, 0, 24, TimeUnit.HOURS);
    }

    public void stopDailyChecks() {
        scheduler.shutdown();
    }

    private void checkExpirations() {
        LocalDate today = LocalDate.now();
        List<Product> products = productService.getAllProducts();

        for (Product product : products) {
            if (product.getExpirationDate() != null) {
                long daysUntilExpiration = ChronoUnit.DAYS.between(today, product.getExpirationDate());

                if (daysUntilExpiration >= 0 && daysUntilExpiration <= EXPIRATION_WARNING_DAYS) {
                    sendExpirationWarning(product, (int) daysUntilExpiration);
                }
            }

            if (product.getQuantity() <= LOW_STOCK_THRESHOLD) {
                sendLowStockWarning(product);
            }
        }
    }

    private void sendExpirationWarning(Product product, int daysUntilExpiration) {
        String subject = "Product Expiration Warning - " + product.getName();
        String body = "ALERT: Product Expiration Warning\n\n" +
                "Product: " + product.getName() + "\n" +
                "ID: " + product.getId() + "\n" +
                "Days until expiration: " + daysUntilExpiration + "\n" +
                "Expiration Date: " + product.getExpirationDate() + "\n" +
                "Current Quantity: " + product.getQuantity() + "\n\n" +
                "Please take necessary action.\n\n" +
                "Inventory Management System";

        EmailService.sendEmail(ADMIN_EMAIL, subject, body);
        System.out.println("Expiration warning sent for: " + product.getName());
    }

    private void sendLowStockWarning(Product product) {
        String subject = "Low Stock Alert - " + product.getName();
        String body = "ALERT: Low Stock Warning\n\n" +
                "Product: " + product.getName() + "\n" +
                "ID: " + product.getId() + "\n" +
                "Current Quantity: " + product.getQuantity() + "\n" +
                "Threshold: " + LOW_STOCK_THRESHOLD + "\n\n" +
                "Please restock this product.\n\n" +
                "Inventory Management System";

        EmailService.sendEmail(ADMIN_EMAIL, subject, body);
        System.out.println("Low stock warning sent for: " + product.getName());
    }

    public void checkAndNotify() {
        checkExpirations();
    }
}
