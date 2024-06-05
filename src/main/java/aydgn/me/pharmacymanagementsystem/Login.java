package aydgn.me.pharmacymanagementsystem;

import aydgn.me.pharmacymanagementsystem.model.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import java.sql.CallableStatement;
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
        if (connection == null) {
            showAlert("Failed to connect to the database. Please check your connection settings.");
            return;
        } else {
            System.out.println("Database connection established.");
        }

        Button loginButton = (Button) scene.lookup("#loginButton");
        TextField usernameField = (TextField) scene.lookup("#usernameField");
        PasswordField passwordField = (PasswordField) scene.lookup("#passwordField");

        if (loginButton == null || usernameField == null || passwordField == null) {
            System.out.println("UI elements not found. Please check your FXML file.");
            return;
        }

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            System.out.println("Login button clicked with username: " + username);
            System.out.println("Password: " + password);

            Task<String> loginTask = new Task<>() {
                @Override
                protected String call() {
                    return authenticateUser(username, password);
                }
            };

            loginTask.setOnSucceeded(e -> {
                String userRole = loginTask.getValue();
                System.out.println("User role obtained: " + userRole);
                if (!"Invalid".equals(userRole)) {
                    if ("Admin".equals(userRole)) {
                        redirectAdminDashboard(stage);
                    } else if ("Pharmacist".equals(userRole)) {
                        redirectPharmacyDashboard(stage);
                    } else {
                        showAlert("Invalid user role!");
                    }
                } else {
                    showAlert("Invalid username or password!");
                }
            });

            loginTask.setOnFailed(e -> {
                Platform.runLater(() -> {
                    showAlert("Login failed. Please try again.");
                    loginTask.getException().printStackTrace();
                });
            });

            new Thread(loginTask).start();
        });

        stage.show();
    }


    private String authenticateUser(String username, String password) {
        String query = "{ ? = call login_user_AM(?, ?) }";
        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
            callableStatement.setString(2, username);
            callableStatement.setString(3, password);

            System.out.println("Executing login_user_AM function with username: " + username + " and password: " + password);

            callableStatement.execute();

            String result = callableStatement.getString(1);
            System.out.println("Result from login_user_AM function: " + result);

            return result;
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while authenticating user: " + e.getMessage());
            e.printStackTrace();
            return "Invalid";
        }
    }



    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void redirectAdminDashboard(Stage stage) {
        System.out.println("Redirecting to Admin Dashboard");
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

    private void redirectPharmacyDashboard(Stage stage) {
        System.out.println("Redirecting to Pharmacy Dashboard");
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

}
