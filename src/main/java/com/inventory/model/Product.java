package com.inventory.model;

import java.time.LocalDate;

public class Product {
      public class Product {
    private int id;
    private String name;
    private String category;
    private String supplier;
    private String productionDate;
    private String expirationDate;
    private int quantity;
    private double price;

    public Product(int id, String name, String category, String supplier,
                   String productionDate, String expirationDate,
                   int quantity, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.supplier = supplier;
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

    public String getProductionDate() { return productionDate; }
    public void setProductionDate(String productionDate) { this.productionDate = productionDate; }

    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
} 
}
