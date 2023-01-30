package models;

import java.time.LocalDateTime;

public final class Appointments {
    private final String appointmentTitle, appointmentDescription, appointmentLocation, appointmentType;
    private final int appointmentID;
    public final int customerID, userID, contactID;
    private final LocalDateTime startTime, endTime;

    /**
     * Appointments constructor
     * */
    public Appointments(int appointmentID, String appointmentTitle, String appointmentDescription, String appointmentLocation,
                        String appointmentType, LocalDateTime startTime, LocalDateTime endTime, int customerID, int contactID, int userID) {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
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

    /**
     * getContactID method to
     * @return contactID
     * */
    public int getContactID() {
        return contactID;
    }

    /**
     * getUserID method to
     * @return userID
     * */
    public int getUserID() {
        return userID;
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
