package com.inventory.model;

import java.time.LocalDate;

public class Offer {
    private String productName;
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;

    public Offer(String productName, double discountPercentage, LocalDate startDate, LocalDate endDate){
      this.productName = productName;
      this.discountPercentage = discountPercentage;
      this.startDate = startDate;
      this.endDate = endDate;
    }


    @Override
    public String toString() {
      return productName + "," + discountPercentage + "," + startDate + "," + endDate;
    }
}
