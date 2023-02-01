package DataAccess;

import helper.JDBC;
import models.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccess extends Users {
    public static Connection connection = null;
    public static PreparedStatement statement = null;
    public static ResultSet set = null;

    public UserAccess(int userID, String userName, String userPassword) {
        super(userID, userName, userPassword);
    }

    /**
     * @param userName, password
     * This method validates the user for the login form.
     * */
    public static boolean checkUser(String userName, String password) throws SQLException {
        try {
            connection = JDBC.openConnection(); // Open connection
            statement = connection.prepareStatement(SQLQueries.CHECK_USER); // Prepare statement
            statement.setString(1, userName); // Set parameters
            statement.setString(2, password); // Set parameters
            set = statement.executeQuery(); // Execute query
            // If the user exists, return the user ID
            return set.next() && set.getString("User_Name").equals(userName) && set.getString("Password").equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (set != null) {
                set = null; // Close result set
            }
            if (statement != null) {
                statement = null; // Close statement
            }
            if (connection != null) {
                connection = JDBC.closeConnection(); // Close connection
            }
        }
    }
}