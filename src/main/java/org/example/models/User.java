package org.example.models;

/**
 * Represents a user of the platform.
 * <p>
 * Stores basic user data such as ID and name.
 */
public class User {
    private String id;
    private String name;  // You can add more fields such as email later.

    /** Default constructor. */
    public User() {
    }

    /**
     * Constructs a {@link User} object with provided ID and name.
     *
     * @param id   unique identifier for the user
     * @param name user's display name
     */
    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
