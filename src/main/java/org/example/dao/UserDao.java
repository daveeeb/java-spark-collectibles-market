package org.example.dao;

import org.example.Database;
import org.example.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing users in the database.
 * <p>
 * Provides CRUD operations and helper methods for user records.
 */
public class UserDao {

    /**
     * Retrieves all users from the database.
     *
     * @return a list of {@link User} objects representing all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                users.add(new User(rs.getString("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Retrieves a single user by ID.
     *
     * @param id the user's unique identifier
     * @return a {@link User} object if found, otherwise null
     */
    public User getUserById(String id) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("id"), rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user the {@link User} object to add
     */
    public void addUser(User user) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)")) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a user's name in the database.
     *
     * @param user the {@link User} object containing updated data
     */
    public void updateUser(User user) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET name = ? WHERE id = ?")) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a user record from the database by ID.
     *
     * @param id the ID of the user to delete
     */
    public void deleteUser(String id) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a user exists in the database by ID.
     *
     * @param id the user's ID to check
     * @return true if the user exists, false otherwise
     */
    public boolean existsUser(String id) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM users WHERE id = ?")) {
            stmt.setString(1, id);
            return stmt.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
