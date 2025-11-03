package org.example.models;

public class Order {
    private String id;
    private String customerName;
    private String itemName;
    private int quantity;
    private double price;

    public Order() {
    //EMPTY BUILDER
    }

    public Order(String itemName, String id, String customerName, int quantity, double price) {
        this.itemName = itemName;
        this.customerName = customerName;
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
