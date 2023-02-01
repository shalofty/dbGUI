package DataAccess;

import Exceptions.ExceptionHandler;
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
     * @throws SQLException
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
            ExceptionHandler.eAlert(e); // handle exception
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
     * @throws SQLException
     * @param contactName
     * @return contactID
     */
    public static String findContactID(String contactName) throws SQLException {
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.SELECT_CONTACTS_BY_NAME_STATEMENT); // prepare statement
            statement.setString(1, contactName); // set contact name
            set = statement.executeQuery(); // execute query
            if (set.next()) {
                return set.getString("Contact_ID");
            } else {
                throw new SQLException("Contact not found.");
            }
        }
        catch (SQLException e) {
            ExceptionHandler.eAlert(e); // handle exception
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
     * Create contact object from result set.
     * @throws SQLException
     * @param set
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
            ExceptionHandler.eAlert(e); // handle exception
            throw e;
        }
    }
}


