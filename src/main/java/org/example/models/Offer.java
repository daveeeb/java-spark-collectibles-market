package org.example.models;

/**
 * Represents an offer made by a user for a specific item.
 * <p>
 * Used for bid operations and real-time price updates.
 */
public class Offer {
    private String id;
    private double amount;
    private String bidderName;

    /** Default constructor (required for Gson serialization). */
    public Offer() {}

    /**
     * Constructs an {@link Offer} object with provided data.
     *
     * @param id          ID of the item being offered on
     * @param amount      offered amount
     * @param bidderName  name of the bidder
     */
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
