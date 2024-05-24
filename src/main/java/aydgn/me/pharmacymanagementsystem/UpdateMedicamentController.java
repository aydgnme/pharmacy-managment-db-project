package aydgn.me.pharmacymanagementsystem;

import aydgn.me.pharmacymanagementsystem.model.Medicament;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateMedicamentController {

    @FXML
    private TextField medicamentIDField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField companyField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField addQuantityField;
    @FXML
    private TextField priceField;

    private Connection connection;

    public void initialize() {
        connectToDatabase();
    }

    private void connectToDatabase() {
        connection = DatabaseConnection.getConnection();
    }

    @FXML
    private void checkMedicamentID(ActionEvent event) {
        String medicamentID = medicamentIDField.getText();
        if (medicamentID.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter Medicament ID");
            return;
        }

        String sql = "SELECT * FROM medicament WHERE medicament_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, medicamentID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nameField.setText(resultSet.getString("name"));
                companyField.setText(resultSet.getString("company_name"));
                quantityField.setText(resultSet.getString("quantity"));
                priceField.setText(resultSet.getString("price_per_unit"));
            } else {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Medicament ID not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateMedicament(ActionEvent event) {
        String medicamentID = medicamentIDField.getText();
        String name = nameField.getText();
        String company = companyField.getText();
        String quantity = quantityField.getText();
        String addQuantity = addQuantityField.getText();
        String price = priceField.getText();

        if (medicamentID.isEmpty() || name.isEmpty() || company.isEmpty() || quantity.isEmpty() || price.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all fields");
            return;
        }

        String sql = "UPDATE medicament SET name = ?, company_name = ?, quantity = ?, price_per_unit = ? WHERE medicament_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, company);
            preparedStatement.setString(3, String.valueOf(Integer.parseInt(quantity) + Integer.parseInt(addQuantity)));
            preparedStatement.setString(4, price);
            preparedStatement.setString(5, medicamentID);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Update Successful!", "Medicament updated successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed!", "Failed to update medicament");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboardPharmacy.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
