package models;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;

public final class Contacts {
    public final String contactName, contactEmail;
    public static int contactID;

    public Contacts(int contactID, String contactName, String contactEmail) {
        if (contactID <= 0) {
            throw new IllegalArgumentException("Invalid contact ID");
        }
        if (contactName == null || contactName.isEmpty()) {
            throw new IllegalArgumentException("Invalid contact name");
        }
        if (contactEmail == null || contactEmail.isEmpty()) {
            throw new IllegalArgumentException("Invalid contact email");
        }
        Contacts.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * @return contactID
     * */
    public String getContactName() {
        return contactName;
    }

    /**
     * @return contactEmail
     * */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @return contactID
     * */
    public static int getContactID() {
        return contactID;
    }

    /**
     * toString method to
     * @return contactName + contactEmail + contactID as a string
     * used for debugging easy debugging, because the method is automatically called when the object is printed
     * */
    @Override
    public String toString() {
        return "Contacts{" +
                "contactID=" + contactID +
                ", contactName='" + contactName + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                '}';
    }
}
