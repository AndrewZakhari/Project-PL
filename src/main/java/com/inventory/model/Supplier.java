package com.inventory.model;

public class Supplier {
    private String name;
    private String contactInfo;

    public Supplier(String name, String contactInfo){
      this.name = name;
      this.contactInfo = contactInfo;
    } 

    public String getName() { return this.name; }
    public String getContactInfo() { return this.contactInfo; }

    @Override
    public String toString() { return name + "," + contactInfo; }

    public static Supplier fromCsv(String line) {
      String[] parts = line.split(",");
      return new Supplier(parts[0], parts.length > 1 ? parts[1] : "");
    }
}
