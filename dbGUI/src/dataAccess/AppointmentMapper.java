package dataAccess;

import models.Appointments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentMapper {
    /**
     * map appointment data from database to appointment object
     * more efficient than using setters
     * @param resultSet result set from database
     * @return appointment object with data from database
     * */
    public static Appointments map(ResultSet resultSet) throws SQLException {
        int appointmentID = resultSet.getInt("Appointment_ID"); // Appointment_ID
        String appointmentTitle = resultSet.getString("Title"); // Title
        String appointmentDescription = resultSet.getString("Description"); // Description
        String appointmentLocation = resultSet.getString("Location"); // Location
        String appointmentType = resultSet.getString("Type"); // Type
        LocalDate appointmentDate = resultSet.getDate("Start").toLocalDate(); // Date
        LocalDateTime startTime = resultSet.getTimestamp("Start").toLocalDateTime(); // Start
        LocalDateTime endTime = resultSet.getTimestamp("End").toLocalDateTime(); // End
        int customerID = resultSet.getInt("Customer_ID"); // Customer_ID
        int userID = resultSet.getInt("User_ID"); // User_ID
        int contactID = resultSet.getInt("Contact_ID"); // Contact_ID
        return new Appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, appointmentDate, startTime, endTime, customerID, contactID, userID);
    }

    /**
     * map appointment data from database to appointment object
     * */
    public static Appointments mapByContactID(ResultSet resultSet, int contactID) throws SQLException {
        int appointmentID = resultSet.getInt("Appointment_ID"); // Appointment_ID
        String appointmentTitle = resultSet.getString("Title"); // Title
        String appointmentDescription = resultSet.getString("Description"); // Description
        String appointmentLocation = resultSet.getString("Location"); // Location
        String appointmentType = resultSet.getString("Type"); // Type
        LocalDate appointmentDate = resultSet.getDate("Start").toLocalDate(); // Date
        LocalDateTime startTime = resultSet.getTimestamp("Start").toLocalDateTime(); // Start
        LocalDateTime endTime = resultSet.getTimestamp("End").toLocalDateTime(); // End
        int customerID = resultSet.getInt("Customer_ID"); // Customer_ID
        int userID = resultSet.getInt("User_ID"); // User_ID
        int resultContactID = resultSet.getInt("Contact_ID"); // Contact_ID
        if (contactID == resultContactID) {
            return new Appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, appointmentDate, startTime, endTime, customerID, resultContactID, userID);
        }
        return null;
    }
}