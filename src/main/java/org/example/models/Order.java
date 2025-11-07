package org.example.models;

/**
 * Represents a purchase order made by a customer.
 * <p>
 * Contains item details, customer info, quantity, and total price.
 */
public class Order {
    private String id;
    private String customerName;
    private String itemName;
    private int quantity;
    private double price;

    /** Empty constructor (required for serialization). */
    public Order() {
        // EMPTY BUILDER
    }

    /**
     * Constructs an {@link Order} with full details.
     *
     * @param itemName      name of the item being purchased
     * @param id            unique identifier of the order
     * @param customerName  name of the customer who placed the order
     * @param quantity      quantity of items ordered
     * @param price         total price of the order
     */
    public Order(String itemName, String id, String customerName, int quantity, double price) {
        this.itemName = itemName;
        this.customerName = customerName;
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
