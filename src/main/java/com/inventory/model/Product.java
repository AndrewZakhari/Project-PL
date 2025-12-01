package com.inventory.model;

import java.time.LocalDate;

public class Product {
    private String name;
    private String category;
    private LocalDate expirationDate;
    private int quantity;
    private double price;

   public Product(String name, String category, LocalDate expirationDate, int quantity, double price) {
    this.name = name;
    this.category = category;
    this.expirationDate = expirationDate;
    this.quantity = quantity;
    this.price = price;
   }

   public String getName(){return name;}
   public String getCategory(){return category;}
   public LocalDate getExpirationDate(){return expirationDate;}
   public int getQuantity() {return quantity;}
   public double getPrice(){return price;}

   public void setName( String name ){ this.name = name; }
   public void setCategory ( String category ) { this.category = category; }
   public void setExpirationDate( LocalDate expirationDate ) { this.expirationDate = expirationDate; }
   public void setQuantity( int quantity ) { this.quantity = quantity; }
   public void setPrice ( double price ) {this.price = price;}

  @Override 
  public String toString() {
    return name + "," + category + "," + expirationDate + "," + quantity + "," + price;
  }

  public static Product fromCsv(String csvLine) {
    String[] parts = csvLine.split(",");
    return new Product(
        parts[0],
        parts[1],
        LocalDate.parse(parts[2]),
        Integer.parseInt(parts[3]),
        Double.parseDouble(parts[4])
        );
  }
}
