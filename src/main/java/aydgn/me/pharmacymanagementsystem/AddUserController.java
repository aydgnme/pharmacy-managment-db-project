package aydgn.me.pharmacymanagementsystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class AddUserController {

    @FXML
    private ChoiceBox<String> userRoleChoiceBox;

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private TextField mobileNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField addressField;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Initialize choice box with user roles
        userRoleChoiceBox.getItems().addAll("Admin", "Pharmacist");
    }

    @FXML
    public void saveUser() {
        String userRole = userRoleChoiceBox.getValue();
        String name = nameField.getText();
        String dob = dobDatePicker.getValue().toString(); // Assuming date format is correct
        String mobileNumber = mobileNumberField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String address = addressField.getText();

        // Insert user into database
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO app_user_AM (user_role, name, dob, mobile_number, email, username, password, address) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userRole);
            statement.setString(2, name);
            statement.setString(3, dob);
            statement.setString(4, mobileNumber);
            statement.setString(5, email);
            statement.setString(6, username);
            statement.setString(7, password);
            statement.setString(8, address);
            statement.executeUpdate();
            System.out.println("User saved successfully");

            // Show success message
            showSuccessAlert("Success", "User successfully saved.");
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("dashboard.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
