package models;

import dataAccess.SQLQueries;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {
    private final String userName, userPassword;
    private int userID;

    /**
     * constructor for Users
     * @param userID, userName, userPassword
     * */
    public Users(int userID, String userName, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * @param userID, userName, userPassword
     * @return Users object
     * */
    public static Users create(int userID, String userName, String userPassword) {
        if (userID <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Invalid user name");
        }
        if (userPassword == null || userPassword.isEmpty()) {
            throw new IllegalArgumentException("Invalid user password");
        }
        return new Users(userID, userName, userPassword);
    }

    /**
     * @return userName
     * */
    public String getUserName() {
        return userName;
    }

    /**
     * @return userPassword
     * */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @return userID
     * */
    public int getUserID() {
        return userID;
    }

    /**
     * getAllUsers() used to get all users from the database
     * @return ObservableList<Users><
     * */
    public static ObservableList<Users> getAllUsers() throws SQLException {
        try {
            ObservableList<Users> users = FXCollections.observableArrayList(); // create an observable list
            Connection connection = JDBC.openConnection();  // open connection
            String sql = "SELECT * FROM users";  // sql statement
            PreparedStatement statement = connection.prepareStatement(SQLQueries.GET_ALL_USERS);  // set the prepared statement
            ResultSet set = statement.executeQuery();  // execute the query
            while (set.next()) {
                int userID = set.getInt("User_ID");
                String userName = set.getString("User_Name");
                String userPassword = set.getString("Password");
                Users user = Users.create(userID, userName, userPassword);
                users.add(user);
            }
            connection.close();  // close the connection
            return users; // return the observable list
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
