package helper;

import java.net.InetAddress;
import java.sql.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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
    public static Connection openConnection() {
        try {
            InetAddress address = InetAddress.getLocalHost(); // get the IP address
            Class.forName(driver); // load the driver
            connection = DriverManager.getConnection(jdbcUrl, userName, passWord); // establish the connection
            StackTraceElement trace = Thread.currentThread().getStackTrace()[2]; // get the stack trace
            System.out.println(LocalTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS) + ":"); // print the time
            System.out.println("\t" + address + " " + trace.getFileName() + " ln" + trace.getLineNumber() + "."); // print the IP address
            System.out.println("\tCONNECTED(" + userName + ", " + databaseName + ")"); // print the username and database name
            System.out.println("\tSESSION ID: " + connection); // print the session ID
        } catch (Exception e) {
            e.printStackTrace();
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
            InetAddress address = InetAddress.getLocalHost(); // get the IP address
            Class.forName(driver); // load the driver
            connection = DriverManager.getConnection(jdbcUrl, userName, passWord); // establish the connection
            StackTraceElement trace = Thread.currentThread().getStackTrace()[2]; // get the stack trace
            System.out.println(LocalTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS) + ":"); // print the time
            System.out.println("\t" + address + " " + trace.getFileName() + " ln" + trace.getLineNumber() + "."); // print the IP address
            System.out.println("\tDISCONNECTED(" + userName + ", " + databaseName + ")"); // print the username and database name
            System.out.println("\tSESSION ID: " + connection); // print the session ID
            connection.close(); // close the connection
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return connection;
    }

    public static void setPreparedStatement(Connection connection, String statement) throws Exception {
        preparedStatement = connection.prepareStatement(statement);
    }

    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}