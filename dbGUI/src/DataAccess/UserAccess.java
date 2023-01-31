package DataAccess;

import helper.JDBC;
import models.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccess extends Users {
    public UserAccess(int userID, String userName, String userPassword) {
        super(userID, userName, userPassword);
    }

    /**
     * @param userName, password
     * This method validates the user for the login form.
     * */
    public static int checkUser(String userName, String password) {
        try {
            PreparedStatement statement = JDBC.openConnection().prepareStatement(SQLQueries.CHECK_USER);
            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet set = statement.executeQuery();
            if (set.next() && set.getString("User_Name").equals(userName) && set.getString("Password").equals(password)) {
                return set.getInt("User_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}