package controllers;

import Exceptions.ExceptionHandler;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML public Label schedulizerLabel, zoneLabel;
    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML private ImageView backgroundView;
    @FXML private Button loginButton;
    @FXML public String userName, passWord;
    Stage stage;

    /**
     * initializes the login form scene
     * sets the zone label to the current zone the user is in
     * */
    @FXML public void initialize(URL url, ResourceBundle bundle) {
        zoneLabel.setText(ZoneId.systemDefault().toString());
        usernameField.setText(JDBC.getUsername());
        passwordField.setText(JDBC.getPassword());
    }

    /**
     * loginHandler checks the username and password fields to see if they match the
     * throws an exception if the username or password is incorrect
     * */
    @FXML public void loginHandler(ActionEvent event) throws Exception {
        try {
            // retrieve username and password from text fields
            if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(LoginController.class.getResource("/views/aioTabbedMenu.fxml"));
                Parent root = loader.load();
                stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Start Failed");
                alert.setHeaderText("Start Failed");
                alert.setContentText("The username or password you entered is incorrect.");
                alert.showAndWait();
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e);
        }
    }
}