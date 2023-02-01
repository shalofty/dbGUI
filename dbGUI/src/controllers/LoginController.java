package controllers;

import DataAccess.SQLQueries;
import DataAccess.UserAccess;
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
import controllers.aioController;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
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
    @FXML public void loginHandler() throws Exception {
        try {
            // retrieve username and password from text fields
            if (usernameField.getText().equals(JDBC.getUsername()) && passwordField.getText().equals(JDBC.getPassword())) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(LoginController.class.getResource("/views/aioTabbedMenu.fxml"));
                Parent root = loader.load();
                stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                loginSpy();
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

    /**
     * loginSpy logs the user's login attempt to a text file
     * */
    @FXML public void loginSpy() {
        String success;
        if (usernameField.getText().equals(JDBC.getUsername()) && passwordField.getText().equals(JDBC.getPassword())) {
            success = "SUCCESS";
        }
        else {
            success = "FAILED";
        }
        String time = LocalDateTime.now().toString().replace(":", "-");
        String userName = usernameField.getText();
        String userLog = time + ": User: " + userName + ", " + success + "\n";
        String fileName = "loginActivity.txt";
        String filePath = "ActivityLog/";
        try {
            FileWriter fileWriter = new FileWriter(filePath + fileName, true);
            fileWriter.write(userLog);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}