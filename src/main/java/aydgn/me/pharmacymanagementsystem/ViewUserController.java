package aydgn.me.pharmacymanagementsystem;

import aydgn.me.pharmacymanagementsystem.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class ViewUserController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> userRole;

    @FXML
    private TableColumn<User, String> userName;

    @FXML
    private TableColumn<User, String> userDOB;

    @FXML
    private TableColumn<User, String> userMobileNumber;

    @FXML
    private TableColumn<User, String> userMail;

    @FXML
    private TableColumn<User, String> userUsername;

    @FXML
    private TableColumn<User, String> userPassword;

    @FXML
    private TableColumn<User, String> userAddress;

    @FXML
    private Button backButton;

    private Connection connection;

    public void initialize() {
        initializeColumns();
        connectToDatabase();
        loadUserDataFromDatabase();
    }

    private void initializeColumns() {
        userRole.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        userName.setCellValueFactory(new PropertyValueFactory<>("name"));
        userDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        userMobileNumber.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        userMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        userUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        userPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        userAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void connectToDatabase() {
        connection = DatabaseConnection.getConnection();
    }

    private void loadUserDataFromDatabase() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM app_user_AM";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Kontrol edin ve user_id'nin gerçekten var olup olmadığını doğrulayın
                int userID;
                try {
                    userID = resultSet.getInt("user_id");
                } catch (SQLException e) {
                    // user_id sütunu yoksa, -1 olarak ayarlayabiliriz.
                    userID = -1;
                }

                String userRole = resultSet.getString("user_role");
                String name = resultSet.getString("name");
                String dob = resultSet.getString("dob");
                String mobileNumber = resultSet.getString("mobile_number");
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String address = resultSet.getString("address");

                User user = new User(userID, userRole, name, dob, mobileNumber, email, username, password, address);
                userList.add(user);
            }

            userTable.getItems().addAll(userList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent actionEvent) {
        System.out.println("Back button clicked");
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
