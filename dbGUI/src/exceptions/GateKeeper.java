package exceptions;
import dataAccess.QueryChronicles;
import helper.JDBC;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.regex.Pattern;

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
        return set.next(); // Return true if the query returns a result
    }

    /**
     * nameCheck() returns true if any of the names aren't formatted properly
     * @param name String name
     * */
    public static boolean correctName(String name) throws Exception {
        final Pattern aName = Pattern.compile("^[a-zA-Z]*+\\s[a-zA-Z]*", Pattern.CASE_INSENSITIVE); // regex for a name
        return aName.matcher(name).matches(); // returns true if the name does not match
    }

    /**
     * phoneNumberCheck() returns true if any of the phone numbers aren't formatted properly
     * @param phoneNumber String phone number
     * */
    public static boolean correctPhone(String phoneNumber) throws Exception {
        // ^(\+\d{1,3}[- ]?)?\d{10,15}$ is a phone alternative, but it has it's own issues as well.
        final Pattern phone = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}"); // regex for a phone number in the form 123-456-7890
        return phone.matcher(phoneNumber).matches(); // returns true if the phone number matches
    }

    /**
     * customerInfoCheck() checks the address and phone number format
     * @param strings String... strings variable length argument, makes the method more flexible
     * I had regex to check the formatting of other fields, but ultimately removed them because they weren't very effective.
     * I tried several solutions, and even used online regex testing resources, but nothing was good enough to use in the project.
     * The problem with using regex on an address is there is no standard format for an address. There are many ways to format an address.
     * I used regex for the name and phone number.
     * */
    public static boolean correctCustomer(String... strings) throws Exception {
        if (!correctPhone(strings[1])) {
            Siren.incorrectPhoneFormat(); // displays an error message if the phone number is not formatted properly
            return false; // returns false if the phone number is not formatted properly
        }
        if (!correctName(strings[4])) {
            Siren.incorrectNameFormat(); // displays an error message if the name is not formatted properly
            return false; // returns false if the name is not formatted properly
        }
        return true; // returns true if all of the fields are formatted properly
    }

    /*
      The following methods are essentially deprecated, but I'm keeping them for now.
      The use of boolean binding in the GUI makes these methods unnecessary.
      I'm keeping them for now in case I need to use them in the future if the boolean binding doesn't work properly.
      @method stringCheck() returns true if all fields are not empty.
     * @method numberCheck() returns true if any of the values are null.
     * @method fieldCheck() returns true if any of the fields are empty.
     * @method dateCheck() returns true if any of the dates are null.
     * */

    /**
     * stringCheck() returns true if all fields are not empty.
     * @param field String... field variable length argument, makes the method more flexible
     * @return boolean true if all fields are not empty, false otherwise
     * */
    public static boolean stringCheck(String... field) throws Exception {
        return Stream.of(field)
                .anyMatch(s-> s==null || s.isEmpty()); // returns true if any fields are empty
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
     * dateCheck() returns true if any of the dates are null.
     * @param dates LocalDate... dates variable length argument, makes the method more flexible
     * @return boolean true if any of the dates are null, false otherwise
     * */
    public static boolean dateCheck(LocalDate... dates) throws Exception {
        return Stream.of(dates)
                .anyMatch(Objects::isNull);
    }

}

