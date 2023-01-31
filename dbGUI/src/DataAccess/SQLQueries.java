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
    public static final String DELETE_FROM_APPOINTMENTS = "DELETE FROM appointments WHERE Appointment_ID=?";
    public static final String INSERT_APPOINTMENT =
            "INSERT INTO appointments " +
            "(Appointment_ID, " +
            "Title, " +
            "Description, " +
            "Location, " +
            "Type, " +
            "Start, " +
            "End, " +
            "Create_Date, " +
            "Created_By, " +
            "Last_Update, " +
            "Last_Updated_By, " +
            "Customer_ID, " +
            "User_ID, " +
            "Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_APPOINTMENT =
            "UPDATE appointments SET Title = ?, " +
            "Description = ?, " +
            "Location = ?, " +
            "Type = ?, " +
            "Start = ?, " +
            "End = ?, " +
            "Last_Update = ?, " +
            "Last_Updated_By = ? WHERE Appointment_ID = ?";

    // Customer Statements
    public static final String SELECT_CUSTOMERS =
            "SELECT customers.Customer_ID, " +
            "customers.Customer_Name, " +
            "customers.Address, " +
            "customers.Postal_Code, " +
            "customers.Phone, " +
            "customers.Division_ID, " +
            "first_level_divisions.Division from customers " +
            "INNER JOIN first_level_divisions " +
            "ON customers.Division_ID = first_level_divisions.Division_ID";

    public static final String INSERT_CUSTOMER =
            "INSERT INTO customers " +
            "(Customer_ID, " +
            "Customer_Name, " +
            "Address, " +
            "Postal_Code, " +
            "Phone, " +
            "Create_Date, " +
            "Created_By, " +
            "Last_Update, " +
            "Last_Updated_By, " +
            "Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";

    public static final String UPDATE_CUSTOMER =
            "UPDATE customers SET Customer_Name = ?, " +
            "Address = ?, " +
            "Postal_Code = ?, " +
            "Phone = ?, " +
            "Last_Update = ?, " +
            "Last_Updated_By = ?, " +
            "Division_ID = ? " +
            "WHERE Customer_ID = ?";

    public static final String DELETE_FROM_CUSTOMERS = "DELETE FROM customers WHERE Customer_ID=?";
    public static final String SELECT_DIVISIONS = "SELECT * from first_level_divisions";
    public static final String SELECT_COUNTRIES = "SELECT Country_ID, Country FROM countries";
    public static final String CHECK_USER = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";

    /**
     * insertInto inserts a new appointment into the database
     * */
    public static void insertIntoAppointments(int ID,
                                              String title,
                                              String description,
                                              String location,
                                              String type,
                                              String start,
                                              String end,
                                              int customerID,
                                              int userID,
                                              int contactID) throws Exception {
        try (Connection connection = JDBC.openConnection()) {
            JDBC.setPreparedStatement(connection, SQLQueries.INSERT_APPOINTMENT);
            PreparedStatement statement = JDBC.getPreparedStatement();

            // set the parameters for the prepared statement
            // the order of the parameters must match the order of the columns in the database
            statement.setInt(1, ID); // appointment ID
            statement.setString(2, title); // title
            statement.setString(3, description); // description
            statement.setString(4, location); // location
            statement.setString(5, type); // type
            statement.setString(6, start); // start
            statement.setString(7, end); // end
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // create date
            statement.setString(9, JDBC.getUsername()); // created by
            statement.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now())); // last update
            statement.setString(11, JDBC.getUsername()); // last updated by
            statement.setInt(12, customerID); // customer ID
            statement.setInt(13, userID); // user ID
            statement.setInt(14, contactID); // contact ID
            statement.execute();
            JDBC.closeConnection();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was an error inserting the appointment into the database.");
            alert.setContentText("Error: " + e.getMessage() + "Cause: " + e.getCause());
            alert.showAndWait();
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * updateAppointment updates an existing appointment in the database
     * */
    public static void updateAppointment(int ID,
                                         String title,
                                         String description,
                                         String location,
                                         String type,
                                         LocalDateTime start,
                                         LocalDateTime end,
                                         int customerID,
                                         int userID,
                                         int contactID) throws Exception {
        try (Connection connection = JDBC.openConnection()) {
            JDBC.setPreparedStatement(connection, SQLQueries.UPDATE_APPOINTMENT);
            // set the parameters for the prepared statement
            // the order of the parameters must match the order of the columns in the database
            PreparedStatement statement = JDBC.getPreparedStatement();
            statement.setInt(1, ID); // appointment ID
            statement.setString(2, title); // title
            statement.setString(3, description); // description
            statement.setString(4, location); // location
            statement.setString(5, type); // type
            statement.setTimestamp(6, Timestamp.valueOf(start)); // start
            statement.setTimestamp(7, Timestamp.valueOf(end)); // end
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // create date
            statement.setString(9, JDBC.getUsername()); // created by
            statement.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now())); // last update
            statement.setString(11, JDBC.getUsername()); // last updated by
            statement.setInt(12, customerID); // customer ID
            statement.setInt(13, userID); // user ID
            statement.setInt(14, contactID); // contact ID
            statement.execute();
            JDBC.closeConnection();
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