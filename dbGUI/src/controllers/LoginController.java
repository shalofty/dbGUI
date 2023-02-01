//1.  Create a log-in form with the following capabilities:
//
//        •  accepts username and password and provides an appropriate error message ✓
//
//        •  determines the user’s location (i.e., ZoneId) and displays it in a label on the log-in form ✓
//
//        •  displays the log-in form in English or French based on the user’s computer language setting to translate all the text, labels, buttons, and errors on the form
//
//        •  automatically translates error control messages into English or French based on the user’s computer language setting

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
        zoneLabel.setText(ZoneId.systemDefault().toString()); // sets the zone label to the current zone the user is in
        usernameField.setText(JDBC.getUsername()); // sets the username field to the username in the JDBC class
        passwordField.setText(JDBC.getPassword()); // sets the password field to the password in the JDBC class
    }

    /**
     * loginHandler checks the username and password fields to see if they match the
     * throws an exception if the username or password is incorrect
     * */
    @FXML public void loginHandler() throws Exception {
        try {
            // retrieve username and password from text fields
            if (usernameField.getText().equals(JDBC.getUsername()) && passwordField.getText().equals(JDBC.getPassword())) {
                FXMLLoader loader = new FXMLLoader(); // creates a new FXMLLoader object
                loader.setLocation(LoginController.class.getResource("/views/aioTabbedMenu.fxml")); // sets the location of the loader to the aioTabbedMenu.fxml file
                Parent root = loader.load(); // loads the root
                stage = (Stage) loginButton.getScene().getWindow(); // gets the stage from the login button
                Scene scene = new Scene(root); // creates a new scene
                stage.setScene(scene); // sets the stage to the scene
                loginSpy(); // calls the loginSpy method
                stage.show(); // shows the stage
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Start Failed");
                alert.setHeaderText("Start Failed");
                alert.setContentText("The username or password you entered is incorrect.");
                loginSpy();
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
        String success; // creates a string variable called success
        // checks to see if the username and password match the username and password in the JDBC class
        if (usernameField.getText().equals(JDBC.getUsername()) && passwordField.getText().equals(JDBC.getPassword())) {
            success = "SUCCESS";
        }
        else {
            success = "FAILED";
        }
        String time = LocalDateTime.now().toString().replace(":", "-"); // gets the current time and replaces the : with -
        String userName = usernameField.getText(); // gets the username from the username field
        String userLog = time + ": User: " + userName + ", " + success + "\n"; // creates a string variable called userLog
        String fileName = "loginActivity.txt"; // creates a string variable called fileName
        String filePath = "ActivityLog/"; // creates a string variable called filePath
        try {
            FileWriter fileWriter = new FileWriter(filePath + fileName, true); // creates a new FileWriter object
            fileWriter.write(userLog); // writes the userLog string to the file
            fileWriter.close(); // closes the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}