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

import exceptions.GateKeeper;
import helper.JDBC;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML public Label schedulizerLabel;
    @FXML public Label zoneLabel;
    @FXML public static Label newUserLabel;
    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;

    @FXML public static String username, password;
    @FXML private ImageView backgroundView;
    @FXML private Button loginButton;
    @FXML Stage stage;

    /**
     * setUsername() sets the username by getting input from the usernameField
     * This will be cross-referenced later while performing database operations
     * */
    public String setUsername(String username) {
        return LoginController.username = usernameField.getText();
    }

    /**
     * getUsername() gets the username
     * */
    public static String getUsername() {
        return username;
    }

    /**
     * setPassword() sets the password by getting input from the passwordField
     * This will be cross-referenced later while performing database operations
     * */
    public String setPassword(String password) {
        return LoginController.password = passwordField.getText();
    }

    /**
     * getPassword() gets the password
     * */
    public static String getPassword() {
        return password;
    }

    /**
     * loginHandler checks the username and password fields to see if they match the
     * throws an exception if the username or password is incorrect
     * */
    @FXML public void loginHandler() throws Exception {
        String username = setUsername(usernameField.getText());
        String passWord = passwordField.getText();
        try {
            // retrieve username and password from text fields
            // usernameField.getText().equals(JDBC.getUsername()) && passwordField.getText().equals(JDBC.getPassword())
            if (GateKeeper.allowEntry(username, passWord)) {
                FXMLLoader loader = new FXMLLoader(); // creates a new FXMLLoader object
                loader.setLocation(LoginController.class.getResource("/views/aioTabbedMenu.fxml")); // sets the location of the loader to the aioTabbedMenu.fxml file
                Parent root = loader.load(); // loads the root
                stage = (Stage) loginButton.getScene().getWindow(); // gets the stage from the login button
                Scene scene = new Scene(root); // creates a new scene
                stage.setScene(scene); // sets the stage to the scene
//                AgentFord.undercoverObservation(usernameField.getText(), passwordField.getText()); // calls the loginSpy method
                stage.show(); // shows the stage
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Start Failed");
                alert.setHeaderText("Start Failed");
                alert.setContentText("The username or password you entered is incorrect.");
//                AgentFord.undercoverObservation(usernameField.getText(), passwordField.getText());
                alert.showAndWait();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void undercoverObservation(String username, String password) {
        String success; // creates a string variable called success
        // checks to see if the username and password match the username and password in the JDBC class
        if (usernameField.getText().equals(JDBC.getUsername()) && passwordField.getText().equals(JDBC.getPassword())) {
            success = "SUCCESS";
        }
        else {
            success = "FAILED";
        }

        try {
            InetAddress ip = InetAddress.getLocalHost(); // gets the local host
            String time = LocalDateTime.now().toString().replace(":", "-"); // gets the current time and replaces the : with -
            String userName = usernameField.getText(); // gets the username from the username field
            String userLog = ip + ", Time: " + time + ": User: " + userName + ", " + success + "\n"; // creates a string variable called userLog
            String fileName = "loginActivity.txt"; // creates a string variable called fileName
            String filePath = "ActivityLog/"; // creates a string variable called filePath
            FileWriter fileWriter = new FileWriter(filePath + fileName, true); // creates a new FileWriter object
            fileWriter.write(userLog); // writes the userLog string to the file
            fileWriter.close(); // closes the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * initializes the login form scene
     * sets the zone label to the current zone the user is in
     * */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        try{
            zoneLabel.setText(ZoneId.systemDefault().toString()); // sets the zone label to the current zone the user is in
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
