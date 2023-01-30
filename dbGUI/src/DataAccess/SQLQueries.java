package DataAccess;

import helper.JDBC;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SQLQueries {
    /**
     * I thought it would be easier to have all of my SQL queries in one place.
     * To make the code look a bit cleaner
     * */
    // Appointment Statements
    public static final String GET_ALL_APPOINTMENTS = "SELECT * from appointments";
    public static final String DELETE_APPOINTMENT = "DELETE FROM appointments WHERE Appointment_ID=?";
    public static final String INSERT_APPOINTMENTS = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_APPOINTMENT = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ? WHERE Appointment_ID = ?";
    // Customer Statements
    public static final String CUSTOMER_ACCESS = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, customers.Division_ID, first_level_divisions.Division from customers INNER JOIN  first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID";
    public static final String DIVISION_ACCESS = "SELECT * from first_level_divisions";
    public static final String CHECK_USER = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";

    /**
     * insertInto inserts a new appointment into the database
     * */
    public static void insertInto (int ID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) throws Exception {
        try (Connection connection = JDBC.getConnection()) {
            JDBC.setPreparedStatement(JDBC.getConnection(), SQLQueries.INSERT_APPOINTMENTS);
            PreparedStatement statement = JDBC.getPreparedStatement();
            statement.setInt(1, ID);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, location);
            statement.setString(5, type);
            statement.setTimestamp(6, Timestamp.valueOf(start));
            statement.setTimestamp(7, Timestamp.valueOf(end));
            statement.setInt(8, customerID);
            statement.setInt(9, userID);
            statement.setInt(10, contactID);
            statement.execute();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was an error inserting the appointment into the database.");
            alert.setContentText("Error: " + e.getMessage() + "Cause: " + e.getCause());
            alert.showAndWait();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

    public static void updateTable(int ID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) throws Exception {
        try (Connection connection = JDBC.getConnection()) {
            JDBC.setPreparedStatement(JDBC.getConnection(), SQLQueries.UPDATE_APPOINTMENT);
            PreparedStatement statement = JDBC.getPreparedStatement();
            statement.setInt(1, ID);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, location);
            statement.setString(5, type);
            statement.setTimestamp(6, Timestamp.valueOf(start));
            statement.setTimestamp(7, Timestamp.valueOf(end));
            statement.setInt(8, customerID);
            statement.setInt(9, userID);
            statement.setInt(10, contactID);
            statement.execute();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was an error updating the appointment in the database.");
            alert.setContentText("Error: " + e.getMessage() + "Cause: " + e.getCause());
            alert.showAndWait();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }
}