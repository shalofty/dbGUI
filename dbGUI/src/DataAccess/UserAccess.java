package DataAccess;

import Exceptions.ExceptionHandler;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Users;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

    // a method that returns an observable list of all the user_id's and usernames from the database
//    public static ObservableList<Users> getAllUsers() throws SQLException {
//        ObservableList<Users> usersObservableList = FXCollections.observableArrayList(); // create observable list
//        try {
//            connection = JDBC.openConnection(); // open connection
//            statement = connection.prepareStatement(SQLQueries.SELECT_ALL_USERS_STATEMENT); // prepare statement
//            set = statement.executeQuery(); // execute query
//            // loop through result set
//            while (set.next()) {
//                usersObservableList.add(createUser(set)); // add user to observable list
//            }
//            return usersObservableList; // return observable list
//        }
//        catch (SQLException e) {
//            ExceptionHandler.eAlert(e); // handle exception
//            throw e;
//        }
//        finally {
//            if (set != null) {
//                set.close();
//            }
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }
//    }

    // a method that returns an observable list of all the user_id's in the database
    public static ObservableList<Integer> getAllUserIDs() throws SQLException {
        ObservableList<Integer> usersObservableList = FXCollections.observableArrayList(); // create observable list
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.SELECT_ALL_USER_IDS_STATEMENT); // prepare statement
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                usersObservableList.add(set.getInt("User_ID")); // add user to observable list
            }
            return usersObservableList; // return observable list
        }
        catch (SQLException e) {
            ExceptionHandler.eAlert(e); // handle exception
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
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.SELECT_ALL_USER_NAMES_STATEMENT); // prepare statement
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                usersObservableList.add(set.getString("User_Name")); // add user to observable list
            }
            return usersObservableList; // return observable list
        }
        catch (SQLException e) {
            ExceptionHandler.eAlert(e); // handle exception
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
     * @param userID
     * */
    public static String getUserName(int userID) throws SQLException {
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.SELECT_USER_NAME_STATEMENT); // prepare statement
            statement.setInt(1, userID); // set parameters
            set = statement.executeQuery(); // execute query
            if (!set.isBeforeFirst()) {
                return "No Users"; // return no user if no user is found
            }
            if (set.next()) {
                return set.getString("User_Name"); // return the user name
            }
        }
        catch (SQLException e) {
            ExceptionHandler.eAlert(e); // handle exception
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

    // a method that creates a new user in the database
    public static void createUser(String userName, String password) throws SQLException {
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.INSERT_NEW_USER_STATEMENT); // prepare statement
            statement.setString(2, userName); // set parameters
            statement.setString(3, password); // set parameters
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault()))); // set parameters
            statement.setString(5, JDBC.getUsername()); // set parameters
            statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault()))); // set parameters
            statement.setString(7, JDBC.getUsername()); // set parameters
            statement.executeUpdate(); // execute update
        }
        catch (SQLException e) {
            ExceptionHandler.eAlert(e); // handle exception
            throw e;
        }
        finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}