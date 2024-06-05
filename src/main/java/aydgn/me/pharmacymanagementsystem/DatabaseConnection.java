package aydgn.me.pharmacymanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String URL = "jdbc:oracle:thin:@172.20.1.39:1521:Ora09";
    private static final String USER = "hr";
    private static final String PASSWORD = "oracletest";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Set the timezone to UTC
            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);
            props.setProperty("oracle.jdbc.timezoneAsRegion", "false");
            props.setProperty("oracle.jdbc.defaultNChar", "true");
            props.setProperty("oracle.jdbc.thinLogonCapability", "true");

            // Set the JVM timezone to UTC
            System.setProperty("user.timezone", "UTC");

            connection = DriverManager.getConnection(URL, props);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}