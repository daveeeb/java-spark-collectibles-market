package org.example.models;

/**
 * Represents an item in the collectibles market.
 * <p>
 * Used for displaying and handling collectible item data such as name, description, and price.
 */
public class Item {
    private String id;
    private String name;
    private String description;
    private String price;

    /**
     * Constructs an {@link Item} object.
     *
     * @param id          unique identifier of the item
     * @param name        name of the collectible item
     * @param description textual description of the item
     * @param price       price string (formatted as currency)
     */
    public Item(String id, String name, String description, String price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}
