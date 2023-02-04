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

import dataAccess.QueryChronicles;
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
import numericNexus.NumberGenie;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML public Label schedulizerLabel;
    @FXML public Label zoneLabel;
    @FXML public static Label newUserLabel;
    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;

    @FXML public static String username, password;
    @FXML private ImageView backgroundView;
    @FXML private Button loginButton, newUserButton;
    @FXML public ResourceBundle languageBundles;
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
     * Create a new user
     * */
    @FXML public void newUser() {
        try {
            GateKeeper.collectCredentials(); // collects the username and password from the user
            String username = GateKeeper.getNewUserName(); // gets the username
            String password = GateKeeper.getNewPassWord(); // gets the password
            int userID = NumberGenie.magicUser(); // generates a unique random user ID
            Connection connection = JDBC.openConnection(); // opens a connection to the database
            QueryChronicles.INSERT_NEW_USER(connection, userID, username, password); // inserts the new user into the database
            System.out.println("New user created");
            connection.close(); // closes the connection to the database
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * loginHandler checks the username and password fields to see if they match the
     * throws an exception if the username or password is incorrect
     * */
    @FXML public void loginHandler() throws Exception {
        String username = setUsername(usernameField.getText());
        String passWord = passwordField.getText();
        try {
            if (GateKeeper.allowEntry(username, passWord)) {
                FXMLLoader loader = new FXMLLoader(); // creates a new FXMLLoader object
                loader.setLocation(LoginController.class.getResource("/views/aioTabbedMenu.fxml")); // sets the location of the loader to the aioTabbedMenu.fxml file
                Parent root = loader.load(); // loads the root
                stage = (Stage) loginButton.getScene().getWindow(); // gets the stage from the login button
                Scene scene = new Scene(root); // creates a new scene
//                scene.getStylesheets().add("/views/styles.css"); // adds the styles.css file to the scene
                stage.setScene(scene); // sets the stage to the scene
                stage.show(); // shows the stage
            } else {
                String currentLocale = Locale.getDefault().getLanguage(); // gets the current locale
                if (currentLocale.equals("fr")) {
                    ResourceBundle languageBundles = ResourceBundle.getBundle("bundles/fr_lang", Locale.FRANCE); // gets the login resource bundle
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(languageBundles.getString("errorTitle.text"));
                    alert.setHeaderText(languageBundles.getString("errorHeader.text"));
                    alert.setContentText(languageBundles.getString("errorText.text"));
                    alert.showAndWait();
                }
                else if (currentLocale.equals("en")) {
                    ResourceBundle languageBundles = ResourceBundle.getBundle("bundles/en_lang", Locale.US); // gets the login resource bundle
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(languageBundles.getString("errorTitle.text"));
                    alert.setHeaderText(languageBundles.getString("errorHeader.text"));
                    alert.setContentText(languageBundles.getString("errorContent.text"));
                    alert.showAndWait();
                }
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
            String currentLocale = Locale.getDefault().getLanguage(); // gets the current locale
            String frenchLocale = Locale.FRENCH.toString(); // gets the french locale
            String englishLocale = Locale.ENGLISH.toString(); // gets the english locale
            ResourceBundle englishLanguageBundle = ResourceBundle.getBundle("bundles/en_lang", Locale.forLanguageTag(englishLocale)); // gets the language bundles
            ResourceBundle frenchLanguageBundle = ResourceBundle.getBundle("bundles/fr_lang", Locale.forLanguageTag(frenchLocale)); // gets the language bundles
            zoneLabel.setText(ZoneId.systemDefault().toString()); // sets the zone label to the current zone the user is in
            if (frenchLocale.equals(currentLocale)) {
                loginButton.setText(frenchLanguageBundle.getString("loginButton.text")); // sets the login button text to the text in the language bundle
                newUserButton.setText(frenchLanguageBundle.getString("newUserButton.text")); // sets the new user button text to the text in the language bundle
                usernameField.setPromptText(frenchLanguageBundle.getString("usernameField.PromptText")); // sets the username field prompt text to the text in the language bundle
                passwordField.setPromptText(frenchLanguageBundle.getString("passwordField.PromptText")); // sets the password field prompt text to the text in the language bundle
            }
            else if (englishLocale.equals(currentLocale)) {
                loginButton.setText(englishLanguageBundle.getString("loginButton.text")); // sets the login button text to the text in the language bundle
                newUserButton.setText(englishLanguageBundle.getString("newUserButton.text")); // sets the new user button text to the text in the language bundle
                usernameField.setPromptText(englishLanguageBundle.getString("usernameField.PromptText")); // sets the username field prompt text to the text in the language bundle
                passwordField.setPromptText(englishLanguageBundle.getString("passwordField.PromptText")); // sets the password field prompt text to the text in the language bundle
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
