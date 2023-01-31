package DataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Contacts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactAccess {
    private static final String SELECT_ALL_CONTACTS_SQL = "SELECT * FROM contacts";
    private static final String SELECT_CONTACT_ID_SQL = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";

    /**
     * Create observablelist from all contacts.
     * @throws SQLException
     * @return contactsObservableList
     */
    public static ObservableList<Contacts> getAllContacts() throws SQLException {
        System.out.println("ContactAccess.getAllContacts()" + "\nTrying: JDBC.openConnection();");
        ObservableList<Contacts> contactsObservableList = FXCollections.observableArrayList();
        try (Connection connection = JDBC.openConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_CONTACTS_SQL);
             ResultSet rs = ps.executeQuery()) {
            JDBC.closeConnection();
            while (rs.next()) {
                contactsObservableList.add(createContact(rs));
            }
        }
        JDBC.closeConnection();
        return contactsObservableList;
    }

    /**
     * Find contact ID given contact name.
     * @throws SQLException
     * @param contactName
     * @return contactID
     */
    public static String findContactID(String contactName) throws SQLException {
        try (Connection connection = JDBC.openConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_CONTACT_ID_SQL)) {
            ps.setString(1, contactName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Contact_ID");
                }
            }
        }
        return null;
    }

    /**
     * Create contact object from result set.
     * @throws SQLException
     * @param set
     * @return contact
     */
    private static Contacts createContact(ResultSet set) throws SQLException {
        int contactID = set.getInt("Contact_ID");
        String contactName = set.getString("Contact_Name");
        String contactEmail = set.getString("Email");
        return new Contacts(contactID, contactName, contactEmail);
    }
}

