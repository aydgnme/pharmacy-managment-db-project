package aydgn.me.pharmacymanagementsystem;

import aydgn.me.pharmacymanagementsystem.model.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends Application {

    private Connection connection;
    private User currentUser;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Pharmacy Management System");
        stage.setScene(scene);

        connection = DatabaseConnection.getConnection();

        Button loginButton = (Button) scene.lookup("#loginButton");
        TextField usernameField = (TextField) scene.lookup("#usernameField");
        PasswordField passwordField = (PasswordField) scene.lookup("#passwordField");

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            redirectToDashboard(stage, username, password, scene);
        });

        stage.show();
    }

    private User authenticateUser(String username, String password) {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM app_user_AM WHERE username = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                String userRole = resultSet.getString("user_role");
                String name = resultSet.getString("name");
                String dob = resultSet.getString("dob"); // Use getString for dob
                String mobileNumber = resultSet.getString("mobile_number");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");

                // Create a new User object with the retrieved data
                User user = new User(userID, userRole, name, dob, mobileNumber, email, username, password, address);
                return user; // Return the user object upon successful authentication
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if authentication fails
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void redirectToDashboard(Stage stage, String username, String password, Scene scene) {
        User currentUser = authenticateUser(username, password);
        if (currentUser != null) {
            this.currentUser = currentUser;
            if (currentUser.getUserRole().equals("Admin")) {
                redirectAdminDashboard(stage, scene);
            } else if (currentUser.getUserRole().equals("Pharmacist")) {
                redirectPharmacyDashboard(stage, scene);
            } else {
                System.out.println("Error: Invalid user role!");
            }
        } else {
            showAlert("Invalid username or password!");
        }
    }

    private void redirectAdminDashboard(Stage stage, Scene scene) {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("dashboard.fxml"));
        try {
            Scene dashboardScene = new Scene(fxmlLoader.load());
            stage.setScene(dashboardScene);

            // Get the controller and set the user object
            DashboardController controller = fxmlLoader.getController();
            controller.setCurrentUser(currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectPharmacyDashboard(Stage stage, Scene scene) {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("dashboardPharmacy.fxml"));
        try {
            Scene dashboardScene = new Scene(fxmlLoader.load());
            stage.setScene(dashboardScene);

            // Get the controller and set the user object
            DashboardPharmacyController controller = fxmlLoader.getController();
            controller.setCurrentUser(currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User getCurrentUser() {
        return currentUser;
    }
}
