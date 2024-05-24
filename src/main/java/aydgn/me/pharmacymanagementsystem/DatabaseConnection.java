// DatabaseConnection.java
package aydgn.me.pharmacymanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Connection URL
                String url = "jdbc:sqlite:identifier.sqlite";
                // Create Connection
                connection = DriverManager.getConnection(url);
                System.out.println("Database Connection Success");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return connection;
    }
}
