package com.inventory.pl_project.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private String clientId;
    private List<OrderItem> items;
    private double totalAmount;
    private LocalDateTime orderDate;
    private String status; // PENDING, COMPLETED, CANCELLED

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(String id, String clientId) {
        this.id = id;
        this.clientId = clientId;
        this.items = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static class OrderItem {
        private String productId;
        private int quantity;
        private double pricePerUnit;
        private double subtotal;

        public OrderItem() {}

        public OrderItem(String productId, int quantity, double pricePerUnit) {
            this.productId = productId;
            this.quantity = quantity;
            this.pricePerUnit = pricePerUnit;
            this.subtotal = quantity * pricePerUnit;
        }

        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getPricePerUnit() { return pricePerUnit; }
        public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }

        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    }
}
