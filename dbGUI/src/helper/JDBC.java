package helper;

import controllers.LoginController;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JDBC class establishes a connection to the database
 * per the instructions in the class video
 * */
public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static final String passWord = "passw0rd!"; // Password
    public static Connection connection;  // Connection Interface
    private static PreparedStatement preparedStatement;

    /**
     * openConnection method opens a connection to the database
     * make sure mysql server is running
     * Win key + R, services.msc and start it
     * */
    public static Connection openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, passWord); // Reference Connection object
            System.out.println("Connected to database as: " + userName);
        }
        catch (Exception e)
        {
            System.out.println("Error:" + e.getMessage());
            System.out.println("Cause:" + e.getCause());
        }
        return connection;
    }

    /**
     * @return Connection
     * */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * closeConnection method closes the connection to the database
     * */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public static void setPreparedStatement(Connection connection, String sqlStatement) throws Exception {
        preparedStatement = connection.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public static String getUsername() {
        return userName;
    }

    public static String getPassword() {
        return passWord;
    }

}