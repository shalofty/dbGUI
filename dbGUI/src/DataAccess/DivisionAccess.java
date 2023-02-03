package DataAccess;

import Exceptions.ExceptionHandler;
import Helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionAccess extends Division {
    public static Connection connection;
    public static PreparedStatement statement;
    public static ResultSet set;
    /**
     * @param divisionID
     * @param divisionName
     * @param countryID
     * a constructor that takes the divisionID, divisionName, and countryID
     * */
    public DivisionAccess(int divisionID, String divisionName, int countryID) {
        super(divisionID, divisionName, countryID); // call the super constructor
    }

    /**
     * @return ObservableList<DivisionAccess>
     * @throws SQLException
     * */
    public static ObservableList<DivisionAccess> getDivisions() throws SQLException {
        try {
            connection = JDBC.openConnection(); // open the connection
            statement = JDBC.openConnection().prepareStatement(SQLQueries.SELECT_DIVISIONS); // prepare the statement
            set = statement.executeQuery(); // execute the statement
            ObservableList<DivisionAccess> divisionsObservableList = FXCollections.observableArrayList(); // create an observable list
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
            ExceptionHandler.eAlert(e);
            throw e;
        } finally {
            if (set != null) set.close(); // close connection
            if (statement != null) statement.close(); // close statement
            if (connection != null) connection.close(); // close connection
        }
    }

    /**
     * getDivisionID method gets the division ID
     * @param divisionName
     * @return divisionID as an int
     * */
    public static int getDivisionID(String divisionName) throws SQLException {
        try {
            connection = JDBC.openConnection(); // open the connection
            statement = connection.prepareStatement(SQLQueries.SELECT_ID_BY_DIVISION); // prepare the statement
            statement.setString(1, divisionName);
            set = statement.executeQuery(); // execute the statement
            int divisionID = 0;
            // loop through the result set
            while (set.next()) {
                divisionID = set.getInt("Division_ID"); // get the division ID
            }
            return divisionID;
        } catch (SQLException e) {
            ExceptionHandler.eAlert(e);
            throw e;
        } finally {
            if (set != null) set.close(); // close set
            if (statement != null) statement.close(); // close statement
            if (connection != null) connection.close(); // close connection
        }
    }
}
