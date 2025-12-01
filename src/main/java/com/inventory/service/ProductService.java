package com.inventory.service;

import com.inventory.model.Product;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProductService {

  private final FileService fileService = new FileService();
  private static final String FILE_NAME = "products.txt";

    public void addProduct(Product product) {
        FileService.appendToFile(FILE_NAME, product.toString());
    }

    public List<Product> getAllProducts() {
      List<String> lines = fileService.readAllLines(FILE_NAME);
      List<Product> products = new ArrayList<>();
      for(String line: lines) {
        if(!line.trim().isEmpty()){
          products.add(Product.fromCsv(line));
        }
      }
      return products;
    }

    public void updateProduct(String oldName, Product newProduct) {
        List<Product> products = getAllProducts();
        List<String> newLines = new ArrayList<>();

        for (Product p : products){
          if (p.getName().equals(oldName)){
            newLines.add(newProduct.toString());
          } else {
            newLines.add(p.toString());
          }
        }
        fileService.overwriteFile(FILE_NAME, newLines);
    }

    public void deleteProduct(String name) {
      List<Product> products = getAllProducts();
      List<String> newLines = new ArrayList<>();

      for (Product p: products) {
        if (!p.getName().equals(name)){
            newLines.add(p.toString());
        }
      }
      fileService.overwriteFile(FILE_NAME, newLines);
    }

    public List<Product> searchByName(String name) {
        return getAllProducts().stream().filter(p -> p.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    public List<Product> searchByDate(LocalDate date) {
       return getAllProducts().stream().filter(p -> p.getExpirationDate().equals(date)).collect(Collectors.toList());
    }

    public List<Product> searchByCategory(String category) {
      return getAllProducts().steam().filter(p -> p.getExpirationDate().equals(date)).collect(Collectors.toList());
    }

    public void getExpiringProducts() {
       LocalDate today = LocalDate.now();
       LocalDate warningDate = today.plusDays(7);

       return getAllProducts().stream().filter(p -> !p.getExpirationDate().isBefore(today) && p.getExpirationDate().isBefore(warningDate)).collect(Collectors.toList());
    }
}
