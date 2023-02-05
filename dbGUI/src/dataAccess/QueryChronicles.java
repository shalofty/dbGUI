package dataAccess;

import controllers.LoginController;
import helper.JDBC;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * QueryChronicles is a class that contains all the SQL queries that I use in my program.
 * I thought it would be easier to have all (well, most) of my SQL queries in one place.
 * This is rather extensive, but I wanted to have all of my queries in one place.
 * */
public class QueryChronicles {
    /// User Statement
    public static final String SELECT_ALL_USER_IDS_STATEMENT =
            "SELECT User_ID " +
            "FROM users"; // select all user IDs
    public static final String SELECT_ALL_USER_NAMES_STATEMENT =
            "SELECT User_Name " +
            "FROM users"; // select all user names
    public static final String SELECT_USER_NAME_STATEMENT =
            "SELECT User_Name " +
            "FROM users " +
            "WHERE User_ID = ?"; // select username by user ID
    public static final String GET_ALL_USERS =
            "SELECT * FROM users"; // get all users
    public static final String NEW_USER_STATEMENT =
            "INSERT INTO users " +
            "(User_ID, User_Name, Password) " +
            "VALUES (?,?,?)"; // insert new user
    public static final String USER_LOGIN_STATEMENT =
            "SELECT * FROM users " +
            "WHERE User_Name = ? " +
            "AND Password = ?"; // user login
    public static final String CHECK_USER =
            "SELECT * FROM users " +
            "WHERE User_Name = ? " +
            "AND Password = ? " +
            "AND User_ID = ?"; // check user credentials
    public static final String SELECT_USER_ID_STATEMENT =
            "SELECT User_ID FROM users " +
            "WHERE User_Name = ?"; // select user ID by username
    /// Appointment Statements
    public static final String GET_ALL_APPOINTMENTS_STATEMENT =
            "SELECT * from appointments"; // select all appointments
    public static final String GET_ALL_APPOINTMENTS_WITHIN_7_DAYS_STATEMENT =
            "SELECT * FROM appointments " +
            "WHERE Start BETWEEN NOW() " +
            "AND DATE_ADD(NOW(), INTERVAL 7 DAY);"; // select all appointments within 7 days
    public static final String GET_ALL_APPOINTMENTS_WITHIN_30_DAYS_STATEMENT =
            "SELECT * FROM appointments " +
            "WHERE start BETWEEN NOW() " +
            "AND DATE_ADD(NOW(), INTERVAL 30 DAY)"; // select all appointments within 30 days
    public static final String GET_ALL_APPOINTMENTS_BY_CUSTOMER_ID_STATEMENT =
            "SELECT * FROM appointments " +
            "WHERE Customer_ID = ?"; // select all appointments by customer ID
    public static final String DELETE_FROM_APPOINTMENTS_STATEMENT =
            "DELETE FROM appointments " +
            "WHERE Appointment_ID=?"; // delete appointment by appointment ID
    public static final String APPOINTMENT_INSERT_STATEMENT =
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
            "Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // insert appointment
    public static final String UPDATE_APPOINTMENT_STATEMENT =
            "UPDATE appointments " +
            "SET Title = ?, " +
            "Description = ?, " +
            "Location = ?, " +
            "Type = ?, " +
            "Start = ?, " +
            "End = ?, " +
            "Last_Update = ?, " +
            "Last_Updated_By = ?, " +
            "Customer_ID = ?, " +
            "User_ID = ?, " +
            "Contact_ID = ? " +
            "WHERE Appointment_ID = ?"; // update appointment
    /// Contacts Statements
    public static final String SELECT_ALL_CONTACTS_STATEMENT =
            "SELECT * from contacts"; // select all contacts
    public static String SELECT_CONTACTS_BY_ID_STATEMENT =
            "SELECT Contact_Name " +
            "FROM contacts " +
            "WHERE Contact_ID = ?"; // select contact name by contact ID
    public static final String SELECT_CONTACTS_BY_NAME_STATEMENT =
            "SELECT Contact_ID " +
            "FROM contacts " +
            "WHERE Contact_Name = ?"; // select contact ID by contact name
    /// Customer Statements
    public static final String SELECT_CUSTOMERS_STATEMENT =
            "SELECT customers.Customer_ID, " +
            "customers.Customer_Name, " +
            "customers.Address, " +
            "customers.Postal_Code, " +
            "customers.Phone, " +
            "customers.Division_ID, " +
            "first_level_divisions.Division from customers " +
            "INNER JOIN first_level_divisions " +
            "ON customers.Division_ID = first_level_divisions.Division_ID"; // select all customers
    public static final String INSERT_CUSTOMER_STATEMENT =
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
            "Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)"; // insert customer
    public static final String UPDATE_CUSTOMER_STATEMENT =
            "UPDATE customers " +
            "SET Customer_Name = ?, " +
            "Address = ?, " +
            "Postal_Code = ?, " +
            "Phone = ?, " +
            "Last_Update = ?, " +
            "Last_Updated_By = ?, " +
            "Division_ID = ? " +
            "WHERE Customer_ID = ?"; // update customer
    public static final String SELECT_CUSTOMER_NAME_BY_ID_STATEMENT =
            "SELECT Customer_Name " +
            "FROM customers " +
            "WHERE Customer_ID = ?"; // select customer name by customer ID
    public static final String SELECT_CUSTOMER_ID_BY_NAME_STATEMENT =
            "SELECT Customer_ID " +
            "FROM customers " +
            "WHERE Customer_Name = ?"; // select customer ID by customer name
    public static final String DELETE_FROM_CUSTOMERS_STATEMENT =
            "DELETE FROM customers " +
            "WHERE Customer_ID=?"; // delete customer by customer ID

    /// Division & Country Statements
    public static final String SELECT_DIVISION_ID_BY_DIVISION_NAME =
            "SELECT Division_ID " +
            "FROM first_level_divisions " +
            "WHERE Division = ?"; // select division ID by division name
    public static final String SELECT_COUNTRIES =
            "SELECT Country_ID, Country " +
            "FROM countries"; // select all countries
    public static final String SELECT_COUNTRY_ID_BY_COUNTRY_NAME =
            "SELECT Country_ID " +
            "FROM countries WHERE Country = ?"; // select country ID by country name
    public static final String SELECT_DIVISION_BY_COUNTRY_ID_STATEMENT =
            "SELECT Division " +
            "FROM first_level_divisions " +
            "WHERE Country_ID = ?"; // select division by division ID
    public static final String INNER_JOIN_STATEMENT =
            "SELECT countries.country\n" +
            "FROM first_level_divisions\n" +
            "INNER JOIN countries\n" +
            "ON first_level_divisions.country_id = countries.country_id\n" +
            "WHERE first_level_divisions.division_id = ?;\n"; // select country by division ID

    /// New User Method //////////////////////////////////////////////////////////////////////////////

    /**
     * Inserts a new user into the database. Wasn't required, but I thought it would be a nice feature.
     * Took the opportunity to practice with prepared statements and incorporating this into the GUI functionality
     * Decided not to remove it, as it is a nice feature to have. I also added a new user to the database.
     * */
    public static void INSERT_NEW_USER(Connection connection,
                                       int ID,
                                       String userName,
                                       String password) throws Exception, SQLException {
        try {
            // set the parameters for the prepared statement
            // the order of the parameters must match the order of the columns in the database
            JDBC.setPreparedStatement(connection, QueryChronicles.NEW_USER_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            statement.setInt(1, ID); // user ID
            statement.setString(2, userName); // username
            statement.setString(3, password); // password
            if (userName != null && password != null) statement.execute(); // execute the statement
            if (statement.getUpdateCount() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("New User");
                alert.setHeaderText("New User Created");
                alert.setContentText("New user " + userName + " created successfully.");
                alert.showAndWait();
                System.out.println("New user " + userName + " created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * insertInto inserts a new appointment into the database
     * */
    public static void INSERT_INTO_APPOINTMENTS_METHOD(Connection connection,
                                                       int ID,
                                                       String title,
                                                       String description,
                                                       String location,
                                                       String type,
                                                       String start,
                                                       String end,
                                                       int customerID,
                                                       int userID,
                                                       int contactID) throws Exception, SQLException {
        try {
            // set the parameters for the prepared statement
            // the order of the parameters must match the order of the columns in the database
            JDBC.setPreparedStatement(connection, QueryChronicles.APPOINTMENT_INSERT_STATEMENT);
            PreparedStatement statement = JDBC.getPreparedStatement();
            statement.setInt(1, ID); // appointment ID
            statement.setString(2, title); // title
            statement.setString(3, description); // description
            statement.setString(4, location); // location
            statement.setString(5, type); // type
            statement.setString(6, start); // start
            statement.setString(7, end); // end
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // create date
            statement.setString(9, LoginController.getUsername()); // created by
            statement.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now())); // last update
            statement.setString(11, LoginController.getUsername()); // last updated by
            statement.setInt(12, customerID); // customer ID
            statement.setInt(13, userID); // user ID
            statement.setInt(14, contactID); // contact ID
            statement.execute();
        }
        catch (Exception e) {
            e.printStackTrace(); // print the stack trace
        }
    }

    /**
     * updateAppointment updates an existing appointment in the database
     * */
    public static void UPDATE_APPOINTMENT_METHOD(Connection connection,
                                                 int appointmentID,
                                                 String title,
                                                 String description,
                                                 String location,
                                                 String type,
                                                 LocalDateTime start,
                                                 LocalDateTime end,
                                                 int customerID,
                                                 int userID,
                                                 int contactID) throws Exception {
        try {
            // set the parameters for the prepared statement
            // the order of the parameters must match the order of the columns in the database
            JDBC.setPreparedStatement(connection, QueryChronicles.UPDATE_APPOINTMENT_STATEMENT);
            PreparedStatement statement = JDBC.getPreparedStatement();
            statement.setString(1, title); // title
            statement.setString(2, description); // description
            statement.setString(3, location); // location
            statement.setString(4, type); // type
            statement.setTimestamp(5, Timestamp.valueOf(start)); // start
            statement.setTimestamp(6, Timestamp.valueOf(end)); // end
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); // last update
            statement.setString(8, LoginController.getUsername()); // last updated by
            statement.setInt(9, customerID); // customer ID
            statement.setInt(10, userID); // user ID
            statement.setInt(11, contactID); // contact ID
            statement.setInt(12, appointmentID); // appointment ID LAST
            System.out.println(statement);
            statement.execute();
            System.out.println(statement.execute());
        }
        catch (SQLException e) {
            e.printStackTrace(); // print the stack trace
            throw e;
        }
    }

    /**
     * deleteFromAppointments deletes an appointment from the database
     * */
    public static void DELETE_APPOINTMENT_METHOD(Connection connection, int customerID)
    {
        try {
            JDBC.setPreparedStatement(connection, QueryChronicles.DELETE_FROM_APPOINTMENTS_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            statement.setInt(1, customerID); // set the customer ID
            statement.execute();
        }
        catch (Exception e) {
            e.printStackTrace(); // print the stack trace
        }
    }

    /**
     * addCustomer adds a new customer to the database
     * */
    public static void INSERT_INTO_CUSTOMERS_METHOD(Connection connection,
                                                    int customerID,
                                                    String customerName,
                                                    String address,
                                                    String postalCode,
                                                    String phone,
                                                    int division)
    throws Exception
    {
        try{
            JDBC.setPreparedStatement(connection, QueryChronicles.INSERT_CUSTOMER_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            statement.setInt(1, customerID); // set the customer ID
            statement.setString(2, customerName); // set the customer name
            statement.setString(3, address); // set the address
            statement.setString(4, postalCode); // set the postal code
            statement.setString(5, phone); // set the phone number
            statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // set the create date
            statement.setString(7, LoginController.getUsername()); // set the created by
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // set the last update
            statement.setString(9, LoginController.getUsername()); // set the last updated by
            statement.setInt(10, division); // set the division ID
            statement.execute();
        }
        catch (Exception e) {
            e.printStackTrace(); // print the stack trace
            throw e;
        }
    }

    /**
     * updateCustomer updates an existing customer in the database
     * */
    public static void UPDATE_CUSTOMER_METHOD(Connection connection,
                                              int customerID,
                                              String customerName,
                                              String address,
                                              String postalCode,
                                              String phone,
                                              int divisionID)
        throws Exception
    {
        try{
            JDBC.setPreparedStatement(connection, QueryChronicles.UPDATE_CUSTOMER_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            statement.setString(1, customerName); // set the customer name
            statement.setString(2, address); // set the address
            statement.setString(3, postalCode); // set the postal code
            statement.setString(4, phone); // set the phone number
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now())); // set the last update
            statement.setString(6, LoginController.getUsername()); // set the last updated by
            statement.setInt(7, divisionID); // set the division ID
            statement.setInt(8, customerID); // set the customer ID
            statement.execute();
            System.out.println(statement.execute());
        }
        catch (Exception e) {
            e.printStackTrace(); // print the stack trace
        }
    }

    /**
     * deleteFromCustomers deletes a customer from the database
     * */
    public static void DELETE_CUSTOMER_METHOD(Connection connection, int customerID)
    {
        try {
            JDBC.setPreparedStatement(connection, QueryChronicles.DELETE_FROM_CUSTOMERS_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            statement.setInt(1, customerID); // set the customer ID
            statement.execute(); // execute the statement
        }
        catch (Exception e) {
            e.printStackTrace(); // print the stack trace
        }
    }
}