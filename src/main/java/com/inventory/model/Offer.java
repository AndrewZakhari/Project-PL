package com.inventory.model;

import java.time.LocalDate;
import com.inventory.model.Product;

public class Offer {
    private Product product;
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;

    //constructor
    public Offer(Product product,double discountPercentage,LocalDate startDate,LocalDate EndDate){
        this.product = product;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    //getters
    public Product getproduct(){
        return product;
    }
    public double discountPercentage() {
        return discountPercentage;
    }
    public LocalDate getstartDate() {
        return startDate;
    }
    public LocalDate getEndDate(){
        return endDate;
    }
    //setters
    public void setProduct(Product product) {
        this.product = product;
    }
    public void setDiscountPercentage(double discountPercentage) {
        this.product = product;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }
    // toString method for saving in file
    @Override
    public String toString(){
        return product.getName() + "," + discountPercentage + "," + startDate + "," + endDate;

    }





    // Getters and Setters
}
