package DataAccess;

import Helper.JDBC;

import java.sql.*;
import java.time.LocalDateTime;

import Exceptions.ExceptionHandler;

public class SQLQueries {
    /**
     * I thought it would be easier to have all of my SQL queries in one place.
     * To make the code look a bit cleaner
     * */
    /// User Statement
    public static final String INSERT_NEW_USER_STATEMENT = "INSERT INTO client_schedule.users " +
            "(USER_ID, User_Name, Password, Create_Date, Created_By, Last_Update, Last_Updated_By)\n" + "VALUES " +
            "(?, ?, ?, ?, ?, ?, ?);\n";
    public static final String SELECT_ALL_USERS_STATEMENT = "SELECT User_ID, User_Name FROM users"; // select all users
    public static final String SELECT_ALL_USER_IDS_STATEMENT = "SELECT User_ID FROM users"; // select all user IDs
    public static final String SELECT_ALL_USER_NAMES_STATEMENT = "SELECT User_Name FROM users"; // select all user names
    public static final String SELECT_USER_PASSWORD_STATEMENT = "SELECT Password FROM users WHERE User_ID = ?"; // select user password by user ID
    public static final String SELECT_USER_NAME_STATEMENT = "SELECT User_Name FROM users WHERE User_ID = ?"; // select user name by user ID
    public static final String GET_USER_ID = "SELECT User_ID, Password FROM users WHERE User_Name = ?"; // get user ID by user name
    public static final String CHECK_USER = "SELECT * FROM users WHERE User_Name = ? AND Password = ? AND User_ID = ?"; // check user credentials
    public static final String SELECT_USER_ID_STATEMENT = "SELECT User_ID FROM users WHERE User_Name = ? AND Password = ?"; // select user ID by user name
    /// Appointment Statements
    public static final String GET_ALL_APPOINTMENTS_STATEMENT = "SELECT * from appointments"; // select all appointments
    public static final String GET_ALL_APPOINTMENTS_WITHIN_7_DAYS_STATEMENT = "SELECT * FROM appointments WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY);"; // select all appointments within 7 days
    public static final String GET_ALL_APPOINTMENTS_WITHIN_30_DAYS_STATEMENT = "SELECT * FROM appointments WHERE start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 30 DAY)"; // select all appointments within 30 days
    public static final String GET_ALL_APPOINTMENTS_BY_CUSTOMER_ID_STATEMENT = "SELECT * FROM appointments WHERE Customer_ID = ?"; // select all appointments by customer ID
    public static final String DELETE_FROM_APPOINTMENTS_STATEMENT = "DELETE FROM appointments WHERE Appointment_ID=?"; // delete appointment by appointment ID
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
            "UPDATE appointments SET Title = ?, " +
            "Description = ?, " +
            "Location = ?, " +
            "Type = ?, " +
            "Start = ?, " +
            "End = ?, " +
            "Last_Update = ?, " +
            "Last_Updated_By = ? " +
            "WHERE Appointment_ID = ?"; // update appointment
    /// Contacts Statements
    public static final String SELECT_ALL_CONTACTS_STATEMENT = "SELECT * from contacts"; // select all contacts
    public static String SELECT_CONTACTS_BY_ID_STATEMENT = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?"; // select contact name by contact ID
    public static final String SELECT_CONTACTS_BY_NAME_STATEMENT = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?"; // select contact ID by contact name
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
    public static final String SELECT_CUSTOMER_NAME_BY_ID_STATEMENT = "SELECT Customer_Name FROM customers WHERE Customer_ID = ?"; // select customer name by customer ID
    public static final String SELECT_CUSTOMER_ID_BY_NAME_STATEMENT = "SELECT Customer_ID FROM customers WHERE Customer_Name = ?"; // select customer ID by customer name

    public static final String DELETE_FROM_CUSTOMERS_STATEMENT = "DELETE FROM customers WHERE Customer_ID=?"; // delete customer by customer ID
    public static final String SELECT_DIVISIONS = "SELECT * from first_level_divisions"; // select all divisions
    public static final String SELECT_ID_BY_DIVISION = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?"; // select division ID by division name
    public static final String SELECT_COUNTRIES = "SELECT Country_ID, Country FROM countries"; // select all countries
    public static final String SELECT_COUNTRY_FROM_DIVISION =
            "SELECT countries.Country\n" +
            "FROM countries\n" +
            "JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID\n" +
            "JOIN customers ON first_level_divisions.Division_ID = customers.Division_ID\n" +
            "WHERE customers.Division_ID = ?"; // select country by division ID
    public static final String GET_DIVISION_FOR_COUNTRY =
            "SELECT first_level_divisions.Division_ID, first_level_divisions.Division\n" +
            "FROM countries\n" +
            "JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID\n" +
            "WHERE countries.Country = ?"; // select division by country name

    /// Appointment Methods //////////////////////////////////////////////////////////////////////////
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
                                                       int contactID)
        throws Exception, SQLException {
        try {
            // set the parameters for the prepared statement
            // the order of the parameters must match the order of the columns in the database
            JDBC.setPreparedStatement(connection, SQLQueries.APPOINTMENT_INSERT_STATEMENT);
            PreparedStatement statement = JDBC.getPreparedStatement();
            statement.setInt(1, ID); // appointment ID
            statement.setString(2, title); // title
            statement.setString(3, description); // description
            statement.setString(4, location); // location
            statement.setString(5, type); // type
            statement.setString(6, start); // start
            statement.setString(7, end); // end

            // This might work
//            String[] columns = new String[]{title, description, location, type, start, end}; // array of columns
//            IntStream.range(0,columns.length).forEach(i-> {try {statement.setString(i+2, columns[i]);} catch (SQLException e) {throw new RuntimeException(e);}}); // set the parameters for the prepared statement

            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // create date
            statement.setString(9, JDBC.getUsername()); // created by
            statement.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now())); // last update
            statement.setString(11, JDBC.getUsername()); // last updated by
            statement.setInt(12, customerID); // customer ID
            statement.setInt(13, userID); // user ID
            statement.setInt(14, contactID); // contact ID
            statement.execute();
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
    }

    /**
     * updateAppointment updates an existing appointment in the database
     * */
    public static void UPDATE_APPOINTMENT_METHOD(Connection connection,
                                                 int ID,
                                                 String title,
                                                 String description,
                                                 String location,
                                                 String type,
                                                 LocalDateTime start,
                                                 LocalDateTime end,
                                                 int customerID,
                                                 int userID,
                                                 int contactID)
        throws Exception {
        try {
            // set the parameters for the prepared statement
            // the order of the parameters must match the order of the columns in the database
            JDBC.setPreparedStatement(connection, SQLQueries.UPDATE_APPOINTMENT_STATEMENT);
            PreparedStatement statement = JDBC.getPreparedStatement();
            statement.setString(1, title); // title
            statement.setString(2, description); // description
            statement.setString(3, location); // location
            statement.setString(4, type); // type
            statement.setTimestamp(5, Timestamp.valueOf(start)); // start
            statement.setTimestamp(6, Timestamp.valueOf(end)); // end
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); // last update
            statement.setInt(8, userID); // last updated by
            statement.setInt(9, ID); // appointment ID

//            statement.setInt(12, customerID); // customer ID
//            statement.setInt(13, userID); // user ID
//            statement.setInt(14, contactID); // contact ID
            statement.execute();
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
    }

    /**
     * deleteFromAppointments deletes an appointment from the database
     * */
    public static void DELETE_APPOINTMENT_METHOD(Connection connection, int customerID)
    {
        try {
            JDBC.setPreparedStatement(connection, SQLQueries.DELETE_FROM_APPOINTMENTS_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            statement.setInt(1, customerID); // set the customer ID
            statement.execute();
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
    }

    /// Customer Methods //////////////////////////////////////////////////////////////////////////

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
            JDBC.setPreparedStatement(connection, SQLQueries.INSERT_CUSTOMER_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            statement.setInt(1, customerID); // set the customer ID
            statement.setString(2, customerName); // set the customer name
            statement.setString(3, address); // set the address
            statement.setString(4, postalCode); // set the postal code
            statement.setString(5, phone); // set the phone number
            statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // set the create date
            statement.setString(7, JDBC.getUsername()); // set the created by
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // set the last update
            statement.setString(9, JDBC.getUsername()); // set the last updated by
            statement.setInt(10, division); // set the division ID
            statement.execute();
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
    }

    /**
     * updateCustomer updates an existing customer in the database
     * */
    public static void UPDATE_CUSTOMER_METHOD
    (Connection connection,
      int customerID,
      String customerName,
      String address,
      String postalCode,
      String phone,
      int division)
        throws Exception
    {
        try{
            JDBC.setPreparedStatement(connection, SQLQueries.UPDATE_CUSTOMER_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            statement.setString(1, customerName); // set the customer name
            statement.setString(2, address); // set the address
            statement.setString(3, postalCode); // set the postal code
            statement.setString(4, phone); // set the phone number
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now())); // set the last update
            statement.setString(6, JDBC.getUsername()); // set the last updated by
            statement.setInt(7, division); // set the division ID
            statement.setInt(8, customerID); // set the customer ID
            statement.execute();
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
    }

    // deleteFromCustomers deletes a customer from the database
    public static void DELETE_CUSTOMER_METHOD(Connection connection, int customerID)
    {
        try {
            JDBC.setPreparedStatement(connection, SQLQueries.DELETE_FROM_CUSTOMERS_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            statement.setInt(1, customerID); // set the customer ID
            statement.execute(); // execute the statement
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
    }
}