package com.inventory.model;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private int orderId;
    private Client client;
    private List<Product> products;
    private LocalDate orderDate;
    private double totalAmount;

    // Getters and Setters
}
