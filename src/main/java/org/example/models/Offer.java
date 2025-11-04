package org.example.models;

public class Offer {
    private String id; // item id
    private double amount;
    private String buyer;

    public Offer() {}

    public Offer(String id, double amount, String buyer) {
        this.id = id;
        this.amount = amount;
        this.buyer = buyer;
    }

    // getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getBuyer() { return buyer; }
    public void setBuyer(String buyer) { this.buyer = buyer; }
}
