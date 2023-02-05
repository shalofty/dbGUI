package dataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionAccess extends Division {
    public static Connection connection;
    public static PreparedStatement statement;
    public static ResultSet set;
    /**
     * @param divisionID as an int
     * @param divisionName as a String
     * @param countryID as an int
     * a constructor that takes the divisionID, divisionName, and countryID
     * */
    public DivisionAccess(int divisionID, String divisionName, int countryID) {
        super(divisionID, divisionName, countryID); // call the super constructor
    }

    /**
     * getDivisions method gets the divisions from the database
     * @return ObservableList<DivisionAccess> as a list of divisions
     * */
    public static ObservableList<String> getDivisions(int countryID) throws SQLException {
        try (Connection connection = JDBC.openConnection();  // open the connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_DIVISION_BY_COUNTRY_ID_STATEMENT))  // prepare the statement
        {
            statement.setInt(1, countryID);
            set = statement.executeQuery(); // execute the statement
            ObservableList<String> divisionNames = FXCollections.observableArrayList(); // create an observable list
            // loop through the result set
            while (set.next()) {
                String divisionName = set.getString("Division"); // get the division name
                divisionNames.add(divisionName); // add the division name to the observable list
            }
            return divisionNames; // return the observable list
        } catch (SQLException e) {
            throw e;
        } finally {
            if (set != null) set.close(); // close set
            if (statement != null) statement.close(); // close statement
            if (connection != null) connection.close(); // close connection
        }
    }

    /**
     * getDivisionID method gets the division ID
     * @param divisionName as a String
     * @return divisionID as an int
     * */
    public static int getDivisionID(String divisionName) throws SQLException {
        try (Connection connection = JDBC.openConnection();  // open the connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_DIVISION_ID_BY_DIVISION_NAME))  // prepare the statement
        {
            statement.setString(1, divisionName); // set the division name
            set = statement.executeQuery(); // execute the statement
            int divisionID = 0; // initialize the division ID
            // loop through the result set
            while (set.next()) {
                divisionID = set.getInt("Division_ID"); // get the division ID
            }
            return divisionID; // return the division ID
        } catch (SQLException e) {
            throw e;
        } finally {
            if (set != null) set.close(); // close set
            if (statement != null) statement.close(); // close statement
            if (connection != null) connection.close(); // close connection
        }
    }

    /**
     * getDivisionName method gets the division name
     * @param divisionID as an int
     * @return divisionName as a String
     * */
    public static String getDivisionName(int divisionID) throws SQLException {
        try (Connection connection = JDBC.openConnection();  // open the connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_DIVISION_NAME_BY_DIVISION_ID_STATEMENT))  // prepare the statement
        {
            statement.setInt(1, divisionID); // set the division ID
            set = statement.executeQuery(); // execute the statement
            String divisionName = ""; // initialize the division name
            // loop through the result set
            while (set.next()) {
                divisionName = set.getString("Division"); // get the division name
            }
            return divisionName; // return the division name
        } catch (SQLException e) {
            throw e;
        } finally {
            if (set != null) set.close(); // close set
            if (statement != null) statement.close(); // close statement
            if (connection != null) connection.close(); // close connection
        }
    }
}
