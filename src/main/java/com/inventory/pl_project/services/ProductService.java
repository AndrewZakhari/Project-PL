package com.inventory.pl_project.services;

import com.google.gson.reflect.TypeToken;
import com.inventory.pl_project.models.Product;
import com.inventory.pl_project.utils.FileManager;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    private static final String PRODUCTS_FILE = "products.json";
    private static final Type PRODUCT_LIST_TYPE = new TypeToken<List<Product>>(){}.getType();

    public void addProduct(Product product) {
        FileManager.appendToFile(PRODUCTS_FILE, product, PRODUCT_LIST_TYPE);
    }

    public void updateProduct(Product updatedProduct) {
        List<Product> products = getAllProducts();
        products = products.stream()
                .map(p -> p.getId().equals(updatedProduct.getId()) ? updatedProduct : p)
                .collect(Collectors.toList());
        FileManager.updateInFile(PRODUCTS_FILE, products);
    }

    public void deleteProduct(String productId) {
        FileManager.deleteFromFile(PRODUCTS_FILE, productId, PRODUCT_LIST_TYPE);
    }

    public List<Product> getAllProducts() {
        return FileManager.readFromFile(PRODUCTS_FILE, PRODUCT_LIST_TYPE);
    }

    public Product getProductById(String id) {
        return getAllProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Product> searchByName(String name) {
        return getAllProducts().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Product> searchByCategory(String categoryId) {
        return getAllProducts().stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public List<Product> searchByDateRange(LocalDate startDate, LocalDate endDate) {
        return getAllProducts().stream()
                .filter(p -> !p.getDateAdded().isBefore(startDate) && !p.getDateAdded().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Product> getProductsNearingExpiration(int daysThreshold) {
        LocalDate today = LocalDate.now();
        return getAllProducts().stream()
                .filter(p -> p.getExpirationDate() != null)
                .filter(p -> {
                    long daysUntilExpiration = java.time.temporal.ChronoUnit.DAYS.between(today, p.getExpirationDate());
                    return daysUntilExpiration >= 0 && daysUntilExpiration <= daysThreshold;
                })
                .collect(Collectors.toList());
    }

    public List<Product> getLowStockProducts(int threshold) {
        return getAllProducts().stream()
                .filter(p -> p.getQuantity() <= threshold)
                .collect(Collectors.toList());
    }
}
