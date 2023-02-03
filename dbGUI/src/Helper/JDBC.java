package Helper;

import java.net.InetAddress;
import java.sql.*;
import java.time.LocalTime;
import java.time.ZoneId;

import Exceptions.ExceptionHandler;

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
     * every established connection needs to be closed!!!
     * */
    public static Connection openConnection()
    {
        try {
            InetAddress address = InetAddress.getLocalHost(); // get the local host address
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, passWord); // Reference Connection object
            StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
            System.out.println(LocalTime.now(ZoneId.systemDefault()) +
                                ": " +
                                address + " " +
                                trace.getFileName() +
                                " ln" +
                                trace.getLineNumber() +
                                ". " +
                                " CONNECTED(" +
                                userName +
                                ", " +
                                databaseName +
                                ") SESSION ID: " +
                                connection);

        }
        catch (Exception e)
        {
            ExceptionHandler.eAlert(e); // eAlert method
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
    public static Connection closeConnection() {
        try {
            InetAddress address = InetAddress.getLocalHost(); // get the local host address
            StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
            System.out.println(LocalTime.now(ZoneId.systemDefault()) +
                                            ": " +
                                            address + " " +
                                            trace.getFileName() +
                                            " ln" +
                                            trace.getLineNumber() +
                                            ". " +
                                            " DISCONNECTED(" +
                                            userName +
                                            ", " +
                                            databaseName +
                                            ") SESSION ID: " +
                                            connection);
            connection.close();
        }
        catch(Exception e)
        {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        return connection;
    }

    public static void setPreparedStatement(Connection connection, String statement) throws Exception {
        preparedStatement = connection.prepareStatement(statement);
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