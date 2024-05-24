package aydgn.me.pharmacymanagementsystem;

import aydgn.me.pharmacymanagementsystem.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class DashboardController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private User currentUser; // Kullanıcı bilgilerini saklamak için currentUser değişkeni

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setUser(User user) {
        this.currentUser = user;
        usernameField.setText(user.getUsername());
    }

    public void exitApplication() {
        Platform.exit();
    }

    public void addUserButton(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(AddUserController.class.getResource("addUser.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void viewProfileButton(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(EditProfileController.class.getResource("editProfile.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void viewUserButton(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(AddUserController.class.getResource("viewUser.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void viewUserUpdateButton(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(UpdateUserController.class.getResource("updateUser.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void logout(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            // Alert ile kullanıcıdan çıkış işlemini onaylamasını iste
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Logout");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Kullanıcı çıkış işlemini onayladıysa, Login ekranına geri dön
                redirectToLogin((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
            }
        });
    }

    private void redirectToLogin(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("login.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            // Kullanıcı adı ve şifre alanlarını sıfırla
            TextField usernameField = (TextField) scene.lookup("#usernameField");
            PasswordField passwordField = (PasswordField) scene.lookup("#passwordField");
            usernameField.clear();
            passwordField.clear();

            // Kullanıcıya giriş yapması için odaklan
            usernameField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
