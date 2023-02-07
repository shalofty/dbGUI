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

    /**
     * nameCheck() returns true if any of the names aren't formatted properly
     * @param name String name
     * */
    public static boolean incorrectName(String name) throws Exception {
        String firstName = "^[a-zA-Z]+"; // regex for first name
        String lastName = "[a-zA-Z]+$"; // regex for last name
        return !name.matches(firstName + " " + lastName); // returns true if the name does not match
    }

    /**
     * addressCheck() returns true if any of the addresses aren't formatted properly
     * @param address String address
     * */
    public static boolean incorrectAddress(String address) throws Exception {
        String addressNumber = "[0-9]+\\s"; // regex for address number
        String addressName = "([a-zA-Z]+\\s)+"; // regex for address name
        String addressType = "[a-zA-Z]+"; // regex for address type
        return !address.matches(addressNumber + addressName + addressType); // returns true if the address does not match
    }

    /**
     * phoneNumberCheck() returns true if any of the phone numbers aren't formatted properly
     * @param phoneNumber String phone number
     * */
    public static boolean incorrectPhone(String phoneNumber) throws Exception {
        String phone = "[0-9]{3}-[0-9]{3}-[0-9]{4}"; // regex for phone number
        return !phoneNumber.matches(phone); // returns true if the phone number does not match
    }

    /**
     * postalCodeCheck() returns true if the postal code is not formatted properly
     * @param postalCode String postal code
     * */
    public static boolean postalFormat(String postalCode, String country) {
        switch (country) {
            case "U.S": // United States
                return postalCode.matches("^[0-9]{5}(?:-[0-9]{4})?$"); // regex for U.S postal code
            case "Canada": // Canada
                return postalCode.matches("^[A-Z][0-9][A-Z] [0-9][A-Z][0-9]$") ||
                        postalCode.matches("^[A-Z][0-9][A-Z][0-9][A-Z][0-9]$"); // regex for Canadian postal code
            case "UK":
                return postalCode.matches("^[A-Z]{1,2}[0-9][A-Z0-9]? [0-9][ABDEFGHJLNPQRSTUWXYZ]{2}$"); // regex for UK postal code
            default:
                return false;
        }
    }

    /**
     * customerInfoCheck() checks the address and phone number format
     * @param strings String... strings variable length argument, makes the method more flexible
     * */
    public static boolean customerInfoCheck(String... strings) throws Exception {
        if (incorrectAddress(strings[0])) {
            Siren.incorrectAddressFormat(); // displays an error message if the address is not formatted properly
            return false; // returns false if the address is not formatted properly
        }
        if (incorrectPhone(strings[1])) {
            Siren.incorrectPhoneFormat(); // displays an error message if the phone number is not formatted properly
            return false; // returns false if the phone number is not formatted properly
        }
        if (!postalFormat(strings[2], strings[3])) {
            Siren.incorrectPostalCodeFormat(); // displays an error message if the postal code is not formatted properly
            return false; // returns false if the postal code is not formatted properly
        }
        if (incorrectName(strings[4])) {
            Siren.incorrectNameFormat(); // displays an error message if the name is not formatted properly
            return false; // returns false if the name is not formatted properly
        }
        return true; // returns true if the address and phone number are formatted properly
    }
}

