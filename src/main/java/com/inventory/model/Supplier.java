package com.inventory.model;
//getters and setters
public class Supplier {
    private String name;
    private String contactInfo;
    //constructor
    public Supplier(String name,String contactInfo){
        this.name=name;
        this.contactInfo=contactInfo;
    }
    //getter for name
    public String getName(){
        return name;
    }
    //setter for name
    public void setName(String name){
        this.name=name;
    }
    //getter for contactinfo
    public String getContactInfo(){
        return contactInfo;
    }
    //setter for contact info
    public void setContactInfo(String contactInfo){
        this.contactInfo=contactInfo;
    }
    //to string for saving in file
    @Override
    public String toString(){
        return name + "," + contactInfo;
    }

    public static Supplier fromCsv(String line){
        String[] parts=line.split(",");
        return new Supplier(parts[0], parts.length > 1 ? parts[1] : "");
    }




}
