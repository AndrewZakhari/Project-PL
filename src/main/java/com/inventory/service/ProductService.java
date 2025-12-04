package com.inventory.service;

import com.inventory.model.Product;
import java.time.LocalDate;
import java.util.List;

public class ProductService {
  // add
  public void addProduct(Product p) {
        products.add(p);
        System.out.println("Product added successfully!");
    }

    // Show
    public void showProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        for (Product p : products) System.out.println(p);
    }

    // Search
    public Product searchProduct(int id) {
        for (Product p : products) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }

    // Delete
    public void deleteProduct(int id) {
        Product p = searchProduct(id);
        if (p != null) {
            products.remove(p);
            System.out.println("Deleted successfully!");
        } else {
            System.out.println("Product not found.");
        }
    }

    // Update
    public void updateProduct(int id) {
        Product p = searchProduct(id);
        if (p == null) {
            System.out.println("Product not found.");
            return;
        }

        Scanner input = new Scanner(System.in);

        System.out.print("New name: ");
        p.setName(input.nextLine());

        System.out.print("New price: ");
        p.setPrice(input.nextDouble());

        System.out.print("New quantity: ");
        p.setQuantity(input.nextInt());
        input.nextLine();

        System.out.print("New production date: ");
        p.setProductionDate(input.nextLine());

        System.out.print("New expiration date: ");
        p.setExpirationDate(input.nextLine());

        System.out.print("New category: ");
        p.setCategory(input.nextLine());

        System.out.println("Product updated successfully!");
    }

    // Search by name
    public void searchByName(String name) {
        boolean found = false;
        for (Product p : products) {
            if (p.toString().toLowerCase().contains(name.toLowerCase())) {
                System.out.println(p);
                found = true;
            }
        }
        if (!found) System.out.println("No product with that name.");
    }

    // Notify near-expiration
    public void notifyExpiration(String todayDate) {
        System.out.println("Products near expiration:");
        for (Product p : products) {
            if (p.getExpirationDate().compareTo(todayDate) < 0) {
                System.out.println(p + "is about to be expired");
            }
        }
    }
    // Search by category
    public List<Product> searchByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                result.add(p);
            }
        }
        return result;
    }

    // Search by expiration date
    public List<Product> searchByExpiration(String date) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getExpirationDate().equals(date)) {
                result.add(p);
            }
        }
        return result;
    }
}











}  


