package DataAccess;

import models.Appointments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AppointmentMapper {
    /**
     * map appointment data from database to appointment object
     * */
    public static Appointments map(ResultSet resultSet) throws SQLException {
        int appointmentID = resultSet.getInt("Appointment_ID"); // Appointment_ID
        String appointmentTitle = resultSet.getString("Title"); // Title
        String appointmentDescription = resultSet.getString("Description"); // Description
        String appointmentLocation = resultSet.getString("Location"); // Location
        String appointmentType = resultSet.getString("Type"); // Type
        LocalDateTime startTime = resultSet.getTimestamp("Start").toLocalDateTime(); // Start
        LocalDateTime endTime = resultSet.getTimestamp("End").toLocalDateTime(); // End
        int customerID = resultSet.getInt("Customer_ID"); // Customer_ID
        int userID = resultSet.getInt("User_ID"); // User_ID
        int contactID = resultSet.getInt("Contact_ID"); // Contact_ID
        return new Appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, startTime, endTime, customerID, contactID, userID);
    }
}