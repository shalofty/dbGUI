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
     * @return ObservableList<DivisionAccess>
     * @throws SQLException if there is an error with the SQL query
     * */
    public static ObservableList<DivisionAccess> getDivisions() throws SQLException {
        try (Connection connection = JDBC.openConnection(); // open the connection
             PreparedStatement statement = JDBC.openConnection().prepareStatement(QueryChronicles.SELECT_DIVISIONS))  // prepare the statement
        {
            ObservableList<DivisionAccess> divisionsObservableList = FXCollections.observableArrayList(); // create an observable list
            set = statement.executeQuery(); // execute the statement
            // loop through the result set
            while (set.next()) {
                int divisionID = set.getInt("Division_ID"); // get the division ID
                String divisionName = set.getString("Division"); // get the division name
                int countryID = set.getInt("Country_ID"); // get the country ID
                // param ordering must match the constructor in the Customers model
                DivisionAccess division = new DivisionAccess(divisionID, divisionName, countryID);
                // add each customer to the observable list of
                divisionsObservableList.add(division);
            }
            return divisionsObservableList; // return the observable list
        } catch (SQLException e) {
            throw e;
        } finally {
            if (set != null) set.close(); // close connection
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
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_ID_BY_DIVISION))  // prepare the statement
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
}
