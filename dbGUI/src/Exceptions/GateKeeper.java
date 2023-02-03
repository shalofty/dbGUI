package Exceptions;
import DataAccess.SQLQueries;
import Helper.JDBC;

import java.sql.*;

public class GateKeeper {
    /**
     * verifyTraveler() returns true if the credentials are verified.
     * @param username the username
     * @param user_id the user_id
     * @param password the password
     * */
    public static boolean verifyTraveler(String username, int user_id, String password) throws Exception {
        boolean isVerified = false; // Initialize the boolean to false
        Connection connection = null; // Initialize the connection
        PreparedStatement statement = null; // Initialize the prepared statement
        ResultSet set = null; // Initialize the result set

        try {
            connection = JDBC.openConnection(); // Open a connection to the database
            JDBC.setPreparedStatement(connection, SQLQueries.CHECK_USER); // Set the prepared statement
            statement = JDBC.getPreparedStatement(); // Get the prepared statement
            statement.setString(1, username); // Set the username
            statement.setInt(2, user_id); // Set the user_id
            statement.setString(3, password); // Set the password
            set = statement.executeQuery(); // Execute the query
            if (set.next())
            { // If the query returns a result
                isVerified = true; // The credentials are verified
            }
            return isVerified;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}

