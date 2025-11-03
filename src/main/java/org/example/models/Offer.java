package org.example.models;

public class Offer {

    private String name;
    private String email;
    private String id; // item id
    private double amount;

    public Offer() {
    }

    public Offer(double amount, String id, String email, String name) {
        this.amount = amount;
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
