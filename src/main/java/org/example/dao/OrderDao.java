package org.example.dao;

import org.example.Database;
import org.example.models.Order;

import java.sql.*;
import java.util.*;

public class OrderDao {

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
