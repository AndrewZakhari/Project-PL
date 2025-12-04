package com.inventory.model;

public class Client {
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private String clientId;
    private String registrationDate;

    public Client(String name, String email, String address, String phoneNumber, String clientId, String registrationDate) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.clientId = clientId;
        this.registrationDate = registrationDate;
    }

   public String getname() {
       return this.name;}
    public void setname(String name) {
        this.name = name;
    }
    public String getemail() {
        return this.email;}
     public void setemail(String email) {
         this.email = email;
     }
     public String getaddress() {
         return this.address;}
      public void setaddress(String address) {
          this.address = address;
      }
      public String getphoneNumber() {
          return this.phoneNumber;}
       public void setphoneNumber(String phoneNumber) {
           this.phoneNumber = phoneNumber;
       }
       public String getclientId() {
           return this.clientId;}
        
        public String getregistrationDate() {
            return this.registrationDate;}
         public void setregistrationDate(String registrationDate) {
             this.registrationDate = registrationDate;
         }


        }
