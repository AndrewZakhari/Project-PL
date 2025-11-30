package com.inventory.service;

import com.inventory.model.Product;
import java.time.LocalDate;
import java.util.List;

public class ProductService {

    public void addProduct(Product product) {
        // File Handling: Add product to file
    }

    public void updateProduct(Product product) {
        // File Handling: Update product in file
    }

    public void deleteProduct(String productName) {
        // File Handling: Delete product from file
    }

    public List<Product> searchByName(String name) {
        // File Handling: Search by name
        return null;
    }

    public List<Product> searchByDate(LocalDate date) {
        // File Handling: Search by date
        return null;
    }

    public List<Product> searchByCategory(String category) {
        // File Handling: Search by category
        return null;
    }

    public void checkExpiration() {
        // Check expiration dates and trigger notifications
    }
}
