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
                // Load Oracle JDBC Driver
                Class.forName("oracle.jdbc.driver.OracleDriver");

                // Connection URL
                String url = "jdbc:oracle:thin:@172.20.1.39:1521:Ora09";

                // Username and Password
                String username = "hr";
                String password = "oracletest";

                // Create Connection
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database Connection Success");
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("Class Not Found Exception: " + e.getMessage());
            }
        }
        return connection;
    }
}
