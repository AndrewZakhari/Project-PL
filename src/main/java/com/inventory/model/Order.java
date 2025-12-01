package com.inventory.model;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private String orderId;
    private String clientEmail;
    private String productName;
    private LocalDate orderDate;
    private int quantity;

    public Order(String orderId, String clientEmail,String productName, LocalDate orderDate, int quantity){
      this.orderId = orderId;
      this.clientEmail = clientEmail;
      this.productName = productName;
      this.orderDate = orderDate;
      this.quantity = quantity;
    }

    public String getOrderId() {return this.orderId;}
    public String getClientEmail() { return this.clientEmail;}
    public String getProductName() {return this.productName;} 
    public LocalDate getOrderDate() { return this.orderDate; }
    public int getQuantity() { return this.quantity; }

    @Override
    public String toString() {
      return orderId + "," + clientEmail + "," + productName + "," + orderDate + "," + quantity;
    }

    public static Order fromCsv(String csvLine) {
      String[] parts = csvLine.split(",");
      return new Order(
          parts[0],
          parts[1],
          parts[2],
          LocalDate.parse(parts[3]),
          Integer.parseInt(parts[4])
          );
    }
}
