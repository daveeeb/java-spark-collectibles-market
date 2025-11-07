package org.example.dao;

import org.example.Database;
import org.example.models.Order;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) for performing CRUD operations on the "orders" table.
 * <p>
 * Provides methods to retrieve and create orders from the database.
 */
public class OrderDao {

    /**
     * Retrieves all orders from the database.
     *
     * @return a list of {@link Order} objects representing all existing orders
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {

            while (rs.next()) {
                Order o = new Order(
                        rs.getString("id"),
                        rs.getString("customer_name"),
                        rs.getString("item"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                orders.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Inserts a new order record into the database.
     *
     * @param order the {@link Order} object containing order details to be saved
     */
    public void createOrder(Order order) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO orders (id, customer_name, item, quantity, price) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, order.getId());
            stmt.setString(2, order.getCustomerName());
            stmt.setString(3, order.getItemName());
            stmt.setInt(4, order.getQuantity());
            stmt.setDouble(5, order.getPrice());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
