package aydgn.me.pharmacymanagementsystem;

import aydgn.me.pharmacymanagementsystem.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import java.sql.ResultSet;



public class EditProfileController {

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField mobileNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private Button updateButton;

    @FXML
    private Button backButton;

    private User currentUser;



    public void setUser(User user) {
        this.currentUser = user;
        initializeFields(); // Alanları doldur
    }

    public void checkUsername(String username) {
        // Check if username exists in database
        int checkUsernameExist = 0;
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM app_user WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                checkUsernameExist = resultSet.getInt(1);
                System.out.println("Username exists: " + checkUsernameExist);
            }
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
        }
    }



    private void initializeFields() {
        if (currentUser != null) {
            // Set username label
            usernameLabel.setText(currentUser.getUsername());
            // Set other fields
            nameField.setText(currentUser.getName());
            mobileNumberField.setText(currentUser.getMobileNumber());
            emailField.setText(currentUser.getEmail());
            addressField.setText(currentUser.getAddress());
        }
    }

    @FXML
    public void updateProfile() {
        if (currentUser == null) {
            showErrorAlert("Error", "User profile is not available.");
            return;
        }

        // Kullanıcının güncellenecek bilgilerini al
        String newName = nameField.getText();
        String newMobileNumber = mobileNumberField.getText();
        String newEmail = emailField.getText();
        String newAddress = addressField.getText();

        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "UPDATE users SET name=?, mobile_number=?, email=?, address=? WHERE user_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newMobileNumber);
            preparedStatement.setString(3, newEmail);
            preparedStatement.setString(4, newAddress);
            preparedStatement.setInt(5, currentUser.getUserID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                showSuccessAlert("Success", "Profile updated successfully!");
                // Profil güncellendikten sonra kullanıcıyı güncelle
                currentUser.setName(newName);
                currentUser.setMobileNumber(newMobileNumber);
                currentUser.setEmail(newEmail);
                currentUser.setAddress(newAddress);
            } else {
                showErrorAlert("Error", "Failed to update profile.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error", "An error occurred while updating the profile!");
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

    @FXML
    public void backButton(ActionEvent actionEvent) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
