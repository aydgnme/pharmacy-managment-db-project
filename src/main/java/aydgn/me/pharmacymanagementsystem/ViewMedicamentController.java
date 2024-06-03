package aydgn.me.pharmacymanagementsystem;

import aydgn.me.pharmacymanagementsystem.model.Medicament;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewMedicamentController {

    @FXML
    private TableView<Medicament> medicamentTable;

    @FXML
    private TableColumn<Medicament, String> medicamentNameColumn;

    @FXML
    private TableColumn<Medicament, String> companyColumn;

    @FXML
    private TableColumn<Medicament, Double> pricePerUnitColumn;

    @FXML
    private TableColumn<Medicament, Integer> quantityColumn;

    @FXML
    private TableColumn<Medicament, String> descriptionColumn;

    @FXML
    private TableColumn<Medicament, String> expiryDateColumn;

    private Connection connection;

    public void initialize() {
        initializeColumns();
        connectToDatabase();
        loadMedicamentDataFromDatabase();
    }

    private void initializeColumns() {
        medicamentNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicamentName"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
        pricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
    }

    private void connectToDatabase() {
        connection = DatabaseConnection.getConnection();
    }

    private void loadMedicamentDataFromDatabase() {
        List<Medicament> medicamentList = new ArrayList<>();
        String sql = "SELECT * FROM medicine_AM";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String medicamentName = resultSet.getString("name");
                String company = resultSet.getString("manufacturer");
                double pricePerUnit = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity"); // Assuming quantity is an integer in the database
                String description = resultSet.getString("description"); // Assuming description is a String in the database
                String expiryDate = resultSet.getString("expiry_date"); // Assuming expiry date is a String in the database

                Medicament medicament = new Medicament(null, medicamentName, company, pricePerUnit, quantity, description, expiryDate);
                medicamentList.add(medicament);
            }

            medicamentTable.getItems().addAll(medicamentList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent actionEvent) {
        System.out.println("Back button clicked");
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("dashboardPharmacy.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
