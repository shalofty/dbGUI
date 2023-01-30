package DataAccess;

import models.Appointments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AppointmentMapper {
    public static Appointments map(ResultSet resultSet) throws SQLException {
        int appointmentID = resultSet.getInt("Appointment_ID");
        String appointmentTitle = resultSet.getString("Title");
        String appointmentDescription = resultSet.getString("Description");
        String appointmentLocation = resultSet.getString("Location");
        String appointmentType = resultSet.getString("Type");
        LocalDateTime startTime = resultSet.getTimestamp("Start").toLocalDateTime();
        LocalDateTime endTime = resultSet.getTimestamp("End").toLocalDateTime();
        int customerID = resultSet.getInt("Customer_ID");
        int userID = resultSet.getInt("User_ID");
        int contactID = resultSet.getInt("Contact_ID");
        return new Appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, startTime, endTime, customerID, userID, contactID);
    }
}