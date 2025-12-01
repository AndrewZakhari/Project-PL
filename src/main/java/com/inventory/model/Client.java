package com.inventory.model;

public class Client {
    private String name;
    private String email;
    private String address;

    public Client(String name, String email, String address) {
      this.name = name;
      this.email = email;
      this.address = address;
    } 

    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getAddress() { return this.address; }


    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
      return name + "," + email + "," + address;
    }

    public static Client fromCsv(String csvLine) {
      String[] parts = csvLine.split(",");
      if (parts.length < 3) return null;
      return new Client(parts[0], parts[1], parts[2]);
    }
}
