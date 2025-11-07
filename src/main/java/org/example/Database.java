package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Manages the connection to the PostgreSQL database.
 * <p>
 * Loads connection properties from the "db.properties" file and
 * provides a singleton-style connection instance.
 */
public class Database {
    private static Connection connection;

    /**
     * Establishes (or retrieves) a database connection using configuration properties.
     *
     * @return a valid {@link Connection} object to the database
     * @throws SQLException if a database access error occurs
     * @throws IOException  if the properties file cannot be found or loaded
     */
    public static Connection getConnection() throws SQLException, IOException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            try (InputStream input = Database.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (input == null) {
                    throw new IOException("IT DOES NOT FIND IN THE db.properties FILE");
                }
                props.load(input);
            }

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            connection = DriverManager.getConnection(url, user, password);
            System.out.println(" SUCCESFULLY CONECTION PostgreSQL");
        }
        return connection;
    }
}
