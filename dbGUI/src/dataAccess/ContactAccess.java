package dataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.Contacts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactAccess {
    public static Connection connection = null;
    public static PreparedStatement statement = null;
    public static ResultSet set = null;

    /**
     * Create observablelist from all contacts.
     * @return contactsObservableList
     */
    public static ObservableList<Contacts> getAllContacts() throws SQLException {
        ObservableList<Contacts> contactsObservableList = FXCollections.observableArrayList(); // create observable list
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.SELECT_ALL_CONTACTS_STATEMENT); // prepare statement
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                contactsObservableList.add(createContact(set)); // add contact to observable list
            }
            return contactsObservableList; // return observable list
        }
        catch (SQLException e) {
            e.printStackTrace(); // print stack trace
            throw e;
        }
        finally {
            if (set != null) {
                set.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Find contact ID given contact name.
     * @return contactID
     * TESTED ✓
     */
    public static int findContactID(String contactName) throws SQLException, Exception {
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.SELECT_CONTACTS_BY_NAME_STATEMENT); // prepare statement
            statement.setString(1, contactName); // set contact name
            set = statement.executeQuery(); // execute query
            if (set.next()) {
                return set.getInt("Contact_ID"); // return contact ID
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Contact not found");
                alert.setContentText("Contact not found in database.");
                alert.showAndWait();
            }
        }
        catch (SQLException e) {
            e.printStackTrace(); // print stack trace
            throw e; // throw exception
        }
        finally {
            if (set != null) {
                set.close(); // close result set
            }
            if (statement != null) {
                statement.close(); // close statement
            }
            if (connection != null) {
                connection.close(); // close connection
            }
        }
        return 0;
    }

    /**
     * Create contact object from result set.
     * @param set result set from query execution
     * @return contact
     */
    private static Contacts createContact(ResultSet set) throws SQLException {
        try {
            int contactID = set.getInt("Contact_ID"); // get contact ID
            String contactName = set.getString("Contact_Name"); // get contact name
            String contactEmail = set.getString("Email"); // get contact email
            return new Contacts(contactID, contactName, contactEmail); // return contact
        }
        catch (SQLException e) {
            e.printStackTrace(); // print stack trace
            throw e;
        }
    }

    // a method that generates an ObservableList of all contacts to be used in CentralNervousSystem in a ComboBox
    /**
     * @return contactsObservableList of contact names
     * called in CentralNervousSystem initialize method to populate contactComboBox
     * TESTED ✓
     */
    public static ObservableList<String> getContactNames() throws SQLException {
        ObservableList<String> contactsObservableList = FXCollections.observableArrayList(); // create observable list
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.SELECT_ALL_CONTACTS_STATEMENT); // prepare statement
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                contactsObservableList.add(set.getString("Contact_Name")); // add contact to observable list
            }
            return contactsObservableList; // return observable list
        }
        catch (SQLException e) {
            e.printStackTrace(); // print stack trace
            throw e;
        }
        finally {
            if (set != null) {
                set.close(); // close result set
            }
            if (statement != null) {
                statement.close(); // close statement
            }
            if (connection != null) {
                connection.close(); // close connection
            }
        }
    }

    /**
     * @param contactID contact ID to get contact name
     * @return contactName contact name from contact ID
     * called in CentralNervousSystem initialize & ContactAccess getContactNames methods
     * TESTED ✓
     */
    public static String getContactName(int contactID) throws SQLException {
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.SELECT_CONTACTS_BY_ID_STATEMENT); // prepare statement
            statement.setInt(1, contactID); // set contact ID
            set = statement.executeQuery(); // execute query
            String contactName = "";
            while (set.next()) {
                contactName = set.getString("Contact_Name"); // get contact name
            }
            return contactName; // return contact name
        }
        catch (SQLException e) {
            e.printStackTrace(); // print stack trace
            throw e;
        }
        finally {
            if (set != null) {
                set.close(); // close result set
            }
            if (statement != null) {
                statement.close(); // close statement
            }
            if (connection != null) {
                connection.close(); // close connection
            }
        }
    }
}


