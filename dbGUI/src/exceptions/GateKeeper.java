package exceptions;
import dataAccess.QueryChronicles;
import helper.JDBC;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.sql.*;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * GateKeeper class has two main jobs, aid in creating new users and checking data going to server
 * @method collectCredentials() collects the username and password from the user
 * @method getNewUserName() gets the username
 * @method getNewPassWord() gets the password
 * @method allowEntry() used to verify credentials for the login screen. -- scratch method for now
 * @method verifyTraveler() returns true if the credentials are verified. -- another scratch method
 *
 * @method stringCheck() returns true if all fields are not empty.
 * @method numberCheck() returns true if any of the values are null.
 * @method fieldCheck() returns true if any of the fields are empty.
 * @method dataCheck() returns true if any of the functions return true.
 *
 * */
public class GateKeeper {
    static Connection connection = null;
    static PreparedStatement statement = null;
    static ResultSet set = null;
    private static String username;
    private static String password;

    /**
     * collectCredentials() collects the username and password from the user
     * */
    public static void collectCredentials() {
        try {
            String currentLocale = Locale.getDefault().getCountry(); // get the current locale
            if (currentLocale.equals("US")) {
                TextInputDialog usernameDialog = new TextInputDialog();
                usernameDialog.setTitle("Username");
                usernameDialog.setHeaderText("Enter your username");
                usernameDialog.showAndWait();
                username = usernameDialog.getResult(); // get the username

                TextInputDialog passwordDialog = new TextInputDialog();
                passwordDialog.setTitle("Password");
                passwordDialog.setHeaderText("Enter your password");
                passwordDialog.showAndWait();
                password = passwordDialog.getResult(); // get the password

                if (username == null || password == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Username or password missing");
                    alert.setContentText("Please enter your username and password");
                    alert.showAndWait();
                }
            }
            else if (currentLocale.equals("FR")) {
                TextInputDialog usernameDialog = new TextInputDialog();
                usernameDialog.setTitle("Nom d'utilisateur");
                usernameDialog.setHeaderText("Entrez votre nom d'utilisateur");
                usernameDialog.showAndWait();
                username = usernameDialog.getResult(); // get the username

                TextInputDialog passwordDialog = new TextInputDialog();
                passwordDialog.setTitle("Mot de passe");
                passwordDialog.setHeaderText("Entrez votre mot de passe");
                passwordDialog.showAndWait();
                password = passwordDialog.getResult(); // get the password

                if (username == null || password == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Nom d'utilisateur ou mot de passe manquant");
                    alert.setContentText("Veuillez entrer votre nom d'utilisateur et votre mot de passe");
                    alert.showAndWait();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getUsername() gets the username
     * */
    public static String getNewUserName() {
        return username;
    }
    /**
     * getPassword() gets the password
     * */
    public static String getNewPassWord() {
        return password;
    }

    /**
     * allowEntry() used to verify credentials for the login screen.
     * @return boolean true if the credentials are verified, false otherwise
     * */
    public static boolean allowEntry(String username, String password) throws SQLException {
        connection = JDBC.openConnection(); // Open a connection to the database
        statement = connection.prepareStatement(QueryChronicles.USER_LOGIN_STATEMENT); // Set the prepared statement
        statement.setString(1, username); // Set the username
        statement.setString(2, password); // Set the password
        set = statement.executeQuery(); // Execute the query
        return set.next();
    }

    /**
     * verifyTraveler() returns true if the credentials are verified.
     * @param username the username
     * @param user_id the user_id
     * @param password the password
     * */
    public static boolean verifyTraveler(String username, int user_id, String password) throws Exception {
        boolean isVerified = false; // Initialize the boolean to false
        connection = null; // Initialize the connection
        statement = null; // Initialize the prepared statement
        set = null; // Initialize the result set
        try {
            connection = JDBC.openConnection(); // Open a connection to the database
            JDBC.setPreparedStatement(connection, QueryChronicles.CHECK_USER); // Set the prepared statement
            statement = JDBC.getPreparedStatement(); // Get the prepared statement
            statement.setString(1, username); // Set the username
            statement.setInt(2, user_id); // Set the user_id
            statement.setString(3, password); // Set the password
            set = statement.executeQuery(); // Execute the query
            if (set.next())
            { // If the query returns a result
                isVerified = true; // The credentials are verified
            } else {
                return false; // The credentials are not verified
            }
            return isVerified;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (set != null) {
                set.close(); // Close the result set
            }
            if (statement != null) {
                statement.close(); // Close the prepared statement
            }
            if (connection != null) {
                connection.close(); // Close the connection
            }
        }
    }

    /**
     * aspiciansCampos() returns true if all fields are not empty.
     * @param field String... field variable length argument, makes the method more flexible
     * @return boolean true if all fields are not empty, false otherwise
     * */
    public static boolean stringCheck(String... field) throws Exception {
        return Stream.of(field)
                .anyMatch(f -> !f.isEmpty()); // returns true if any fields are empty
    }

    /**
     * numberCheck() returns true if any of the values are null.
     * @param values int... values variable length argument, makes the method more flexible
     * @return boolean true if any of the values are null, false otherwise
     * */
    public static boolean numberCheck(int... values) throws Exception {
        return Stream.of(values)
                .anyMatch(Objects::isNull); // returns true if any of the values are null
    }

    /**
     * fieldCheck() returns true if any of the fields are empty.
     * @param field TextField... field variable length argument, makes the method more flexible
     * @return boolean true if any of the fields are empty, false otherwise
     * */
    public static boolean fieldCheck(TextField... field) throws Exception {
        return Stream.of(field)
                .anyMatch(f -> f.getText().isEmpty()); // returns true if any fields are empty
    }

    /**
     * dataCheck() returns true if any of the fields are empty.
     * @param strings String[] strings array of strings
*      @param ints int[] ints array of ints
     * @param fields TextField[] fields array of text fields
     * @return boolean true if any of the fields are empty, false otherwise
     * */
    public static boolean dataCheck(String[] strings, int[] ints, TextField[] fields) throws Exception {
        boolean stringResult = stringCheck(strings);
        boolean numberResult = numberCheck(ints);
        boolean textResult = fieldCheck(fields);
        return stringResult || numberResult || textResult;
    }

}

