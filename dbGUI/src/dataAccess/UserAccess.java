package dataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Users;

import java.sql.*;

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
            statement = connection.prepareStatement(QueryChronicles.CHECK_USER); // Prepare statement
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
                connection.close(); // Close connection
            }
        }
    }

    // a method that returns an observable list of all the user_id's in the database
    public static ObservableList<Integer> getAllUserIDs() throws SQLException {
        ObservableList<Integer> usersObservableList = FXCollections.observableArrayList(); // create observable list
        try (Connection connection = JDBC.openConnection(); // open connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_ALL_USER_IDS_STATEMENT)) // prepare statement
        {
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                usersObservableList.add(set.getInt("User_ID")); // add user to observable list
            }
            return usersObservableList; // return observable list
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (set != null) {
                set.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    // a method that returns an observablelist of all the user names in the database
    public static ObservableList<String> getAllUserNames() throws SQLException {
        ObservableList<String> usersObservableList = FXCollections.observableArrayList(); // create observable list
        try (Connection connection = JDBC.openConnection(); // open connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_ALL_USER_NAMES_STATEMENT)) // prepare statement)
        {
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                usersObservableList.add(set.getString("User_Name")); // add user to observable list
            }
            return usersObservableList; // return observable list
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (set != null) {
                set.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * getUserName method returns the user name for a given user ID from the database
     * @param userID the user ID to search for
     * */
    public static String getUserName(int userID) throws SQLException {
        try (Connection connection = JDBC.openConnection();
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_USER_NAME_STATEMENT))
        {
            statement.setInt(1, userID); // set parameters
            set = statement.executeQuery(); // execute query
            if (!set.isBeforeFirst()) {
                connection.close(); // close connection
                return "No Users"; // return no user if no user is found
            }
            if (set.next()) {
                return set.getString("User_Name"); // return the username
            }
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (set != null) {
                set.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return "No Users"; // return no user if no user is found
    }

    /**
     * getUserID method returns the user ID for a given username from the database
     * @param userName the username to search for
     * @return the user ID
     * */
    public static int getUserID(String userName) throws SQLException {
        try (Connection connection = JDBC.openConnection();
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_USER_ID_STATEMENT))
        {
            statement.setString(1, userName); // set parameters
            set = statement.executeQuery(); // execute query
            if (set.next()) {
                return set.getInt("User_ID"); // return the user id
            }
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (set != null) {
                set.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return 0; // return 0 if no user is found
    }
}