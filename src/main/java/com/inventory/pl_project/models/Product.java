package com.inventory.pl_project.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private double price;
    private int quantity;
    private LocalDate expirationDate;
    private LocalDate dateAdded;
    private String supplierId;
    private List<Offer> offers;

    public Product() {
        this.offers = new ArrayList<>();
    }

    public Product(String id, String name, String description, String categoryId,
                   double price, int quantity, LocalDate expirationDate, String supplierId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.dateAdded = LocalDate.now();
        this.supplierId = supplierId;
        this.offers = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public LocalDate getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDate dateAdded) { this.dateAdded = dateAdded; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public List<Offer> getOffers() { return offers; }
    public void setOffers(List<Offer> offers) { this.offers = offers; }
}