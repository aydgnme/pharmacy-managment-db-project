package aydgn.me.pharmacymanagementsystem;

import aydgn.me.pharmacymanagementsystem.model.Medicament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SellMedicineController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Medicament> medicineTableView;

    @FXML
    private TableColumn<Medicament, Integer> medicineIDColumn;

    @FXML
    private TableColumn<Medicament, String> medicineNameColumn;

    @FXML
    private TextField medicineIDField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField pricePerUnitField;

    @FXML
    private TextField numberOfUnitsField;

    @FXML
    private TextField totalPriceField;

    @FXML
    private TableView<Medicament> cartTableView;

    @FXML
    private TableColumn<Medicament, Integer> cartMedicineIDColumn;

    @FXML
    private TableColumn<Medicament, String> cartNameColumn;

    @FXML
    private TableColumn<Medicament, String> cartCompanyNameColumn;

    @FXML
    private TableColumn<Medicament, Double> cartPricePerUnitColumn;

    @FXML
    private TableColumn<Medicament, Integer> cartNumberOfUnitsColumn;

    @FXML
    private TableColumn<Medicament, Double> cartTotalPriceColumn;

    private Connection connection;

    private ObservableList<Medicament> medicineList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize the columns
        medicineIDColumn.setCellValueFactory(new PropertyValueFactory<>("medicineID"));
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Load data from database
        loadDataFromDatabase();

        // MedicineTableView'de bir öğe seçildiğinde onItemSelected() metodunun çağrılması
        medicineTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            onItemSelected();
        });
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String searchID = searchField.getText();
        if (searchID == null || searchID.isEmpty()) {
            medicineTableView.setItems(medicineList);
            return;
        }

        ObservableList<Medicament> filteredList = FXCollections.observableArrayList();
        for (Medicament medicine : medicineList) {
            if (String.valueOf(medicine.getMedicamentID()).equals(searchID)) {
                filteredList.add(medicine);
            }
        }
        medicineTableView.setItems(filteredList);
    }

    private void loadDataFromDatabase() {
        connection = DatabaseConnection.getConnection();

        String query = "SELECT medicineID, name FROM medicine_AM";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("medicineID");
                String name = rs.getString("name");
                String companyName = rs.getString("manufacturer");
                double pricePerUnit = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String description = rs.getString("description");
                String expiryDate = rs.getString("expiry_date");

                // Create a new Medicament object with all required parameters
                Medicament medicament = new Medicament(id, name, companyName, pricePerUnit, quantity, description, expiryDate);
                medicineList.add(medicament);
            }


            medicineTableView.setItems(medicineList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onItemSelected() {
        // Seçilen öğenin alınması
        Medicament selectedMedicine = medicineTableView.getSelectionModel().getSelectedItem();

        // Seçili öğenin bilgilerinin doldurulması
        if (selectedMedicine != null) {
            medicineIDField.setText(String.valueOf(selectedMedicine.getMedicamentID()));
            nameField.setText(selectedMedicine.getMedicamentName());
            companyNameField.setText(selectedMedicine.getCompany());
            pricePerUnitField.setText(String.valueOf(selectedMedicine.getPricePerUnit()));
        }
    }

    @FXML
    private void onAddToCart(ActionEvent event) {
        // Add selected medicine to cart logic here
    }

    @FXML
    private void onPurchase(ActionEvent event) {
        // Add purchase and print logic here
    }
}
