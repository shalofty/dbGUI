package models;

import dataAccess.ContactAccess;
import dataAccess.CustomerAccess;
import dataAccess.UserAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Appointments {
    private final String appointmentTitle;
    private final String appointmentDescription;
    private final String appointmentLocation;
    private final String appointmentType;
    private final int appointmentID;
    public final int customerID, userID, contactID;
    private final LocalDateTime startTime, endTime;
    private final LocalDate appointmentDate;

    /**
     * Appointments constructor
     * */
    public Appointments(int appointmentID,
                        String appointmentTitle,
                        String appointmentDescription,
                        String appointmentLocation,
                        String appointmentType,
                        LocalDate appointmentDate,
                        LocalDateTime startTime,
                        LocalDateTime endTime,
                        int customerID,
                        int contactID,
                        int userID) {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.contactID = contactID;
        this.userID = userID;
    }

    /**
     * getAppointmentID method to
     * @return appointmentID
     * */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * getAppointmentTitle method to
     * @return appointmentTitle
     * */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * getAppointmentDescription method to
     * @return appointmentDescription
     * */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     * getAppointmentLocation method to
     * @return appointmentLocation
     * */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * getAppointmentType method to
     * @return appointmentType
     * */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * getAppointmentDate method to
     * @return appointmentDate
     * */
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * getStarTime method to
     * @return starTime
     * */
    public LocalDateTime getStartTime() {return startTime;}

    /**
     * getEndTime method to
     * @return endTime
     * */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * getCustomerID method to
     * @return customerID
     * */
    public int getCustomerID() {
        return customerID;
    }

    // get customerName from customerID
    public String getCustomerName(int customerID) throws SQLException {
        try{
            return CustomerAccess.getCustomerNameByID(customerID);
        }
        catch (Exception e) {
            throw e;
        }
    }

    /**
     * getContactID method to
     * @return contactID
     * */
    public int getContactID() {
        return contactID;
    }

    /**
     * getContactName method to get contact name from contactID
     * @return contactName from contactID as a string from ContactAccess
     * practically redundent, should've just called in the controller, but I'm too lazy to change it :P
     * */
    public String getContactName(int contactID) throws SQLException {
        try{
            return ContactAccess.getContactName(contactID);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * getUserID method to
     * @return userID
     * */
    public int getUserID() {
        return userID;
    }

    // a method to get the user name from the user ID
    public String getUserName(int userID) throws SQLException {
        try{
            return UserAccess.getUserName(userID);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static ObservableList<Appointments> getAppointmentsList() {
        return FXCollections.<Appointments>observableArrayList();
    }

    /**
     * toString method to
     * @return appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, starTime, endTime, customerID, contactID, userID
     * used for debugging easy debugging, because the method is automatically called when the object is printed
     *  */
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentID=" + appointmentID +
                ", appointmentTitle='" + appointmentTitle + '\'' +
                ", appointmentDescription='" + appointmentDescription + '\'' +
                ", appointmentLocation='" + appointmentLocation + '\'' +
                ", appointmentType='" + appointmentType + '\'' +
                ", starTime=" + startTime +
                ", endTime=" + endTime +
                ", customerID=" + customerID +
                ", contactID=" + contactID +
                ", userID=" + userID +
                '}';
    }
}
