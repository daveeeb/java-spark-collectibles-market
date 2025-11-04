// src/main/java/org/example/models/Offer.java
package org.example.models;

public class Offer {
    private String id;
    private double amount;
    private String bidderName;

    public Offer() {}

    public Offer(String id, double amount, String bidderName) {
        this.id = id;
        this.amount = amount;
        this.bidderName = bidderName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getBidderName() { return bidderName; }
    public void setBidderName(String bidderName) { this.bidderName = bidderName; }
}