package aydgn.me.pharmacymanagementsystem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;


public class AddMedicamentController {

    @FXML
    private TextField medicamentNameField;

    @FXML
    private TextField companyField;

    @FXML
    private TextField pricePerUnitField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextArea descriptionTextarea;

    @FXML
    private DatePicker expiryDate;

    @FXML
    public void initialize() {
    }

    @FXML
    public void saveMedicament() {
        String medicamentName = medicamentNameField.getText();
        String company = companyField.getText();
        String pricePerUnit = pricePerUnitField.getText();
        String quantity = quantityField.getText();
        String description = descriptionTextarea.getText();
        String expiryDateString = expiryDate.getValue().toString();

        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO medicine (name, description, manufacturer, expiry_date, price ,quantity) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, medicamentName);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, company);
            preparedStatement.setString(4, expiryDateString);
            preparedStatement.setString(5, pricePerUnit);
            preparedStatement.setString(6, quantity);
            preparedStatement.executeUpdate();
            System.out.println("Medicament added successfully");

            showSuccessAlert("Success", "Medicament added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlerts("Error", "An error occurred while adding medicament");
        }
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlerts(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboardPharmacy.fxml")); // Assuming dashboard.fxml is in the same directory as this class
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
