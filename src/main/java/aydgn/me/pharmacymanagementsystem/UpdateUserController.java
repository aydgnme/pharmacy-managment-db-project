package aydgn.me.pharmacymanagementsystem;

import aydgn.me.pharmacymanagementsystem.DatabaseConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateUserController {

    @FXML
    private ChoiceBox<String> userRoleChoiceBox;

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private TextField mobileNumberField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private Button searchButton;

    @FXML
    private Button updateButton;

    @FXML
    public void initialize() {
        // Initialize choice box with user roles
        userRoleChoiceBox.getItems().addAll("Admin", "Pharmacist");
    }

    @FXML
    public void updateUser() {
        String userRole = userRoleChoiceBox.getValue();
        String name = nameField.getText();
        String dob = (dobDatePicker.getValue() != null) ? dobDatePicker.getValue().toString() : null;
        String mobileNumber = mobileNumberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String username = usernameField.getText();

        // Check if username is provided
        if (username == null || username.isEmpty()) {
            showErrorAlert("Error", "Username must be provided to update user.");
            return;
        }

        // Build the SQL update query dynamically based on non-empty fields
        StringBuilder sqlBuilder = new StringBuilder("UPDATE app_user SET ");
        boolean firstField = true;

        if (userRole != null && !userRole.isEmpty()) {
            sqlBuilder.append("user_role = ?");
            firstField = false;
        }
        if (name != null && !name.isEmpty()) {
            if (!firstField) sqlBuilder.append(", ");
            sqlBuilder.append("name = ?");
            firstField = false;
        }
        if (dob != null && !dob.isEmpty()) {
            if (!firstField) sqlBuilder.append(", ");
            sqlBuilder.append("dob = ?");
            firstField = false;
        }
        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            if (!firstField) sqlBuilder.append(", ");
            sqlBuilder.append("mobile_number = ?");
            firstField = false;
        }
        if (email != null && !email.isEmpty()) {
            if (!firstField) sqlBuilder.append(", ");
            sqlBuilder.append("email = ?");
            firstField = false;
        }
        if (address != null && !address.isEmpty()) {
            if (!firstField) sqlBuilder.append(", ");
            sqlBuilder.append("address = ?");
        }

        sqlBuilder.append(" WHERE username = ?");

        // Update user in the database
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());

            int paramIndex = 1;

            if (userRole != null && !userRole.isEmpty()) {
                statement.setString(paramIndex++, userRole);
            }
            if (name != null && !name.isEmpty()) {
                statement.setString(paramIndex++, name);
            }
            if (dob != null && !dob.isEmpty()) {
                statement.setString(paramIndex++, dob);
            }
            if (mobileNumber != null && !mobileNumber.isEmpty()) {
                statement.setString(paramIndex++, mobileNumber);
            }
            if (email != null && !email.isEmpty()) {
                statement.setString(paramIndex++, email);
            }
            if (address != null && !address.isEmpty()) {
                statement.setString(paramIndex++, address);
            }

            statement.setString(paramIndex, username);

            statement.executeUpdate();
            System.out.println("User updated successfully");

            // Show success message
            showSuccessAlert("Success", "User successfully updated.");
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void checkUsername() {
        // Check if username exists in database
        int checkUsernameExist = 0;
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM app_user WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, usernameField.getText());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                checkUsernameExist = resultSet.getInt(1);
                System.out.println("Username exists: " + checkUsernameExist);
            }
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
        }
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
