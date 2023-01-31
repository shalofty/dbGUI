package DataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionAccess {
    /**
     * Observablelist that takes all customer data from the database.
     * */
    public static ObservableList<Division> getDivisions(Connection connection) throws SQLException {
        PreparedStatement statement = JDBC.openConnection().prepareStatement(SQLQueries.DIVISION_ACCESS);
        ResultSet set = statement.executeQuery();
        ObservableList<Division> divisionsObservableList = FXCollections.observableArrayList();
        while (set.next()) {
            int divisionID = set.getInt("Division_ID");
            String divisionName = set.getString("Division");
            int countryID = set.getInt("Country_ID");
            // param ordering must match the constructor in the Customers model
            Division division = new Division(divisionID, divisionName, countryID);
            // add each customer to the observable list of
            divisionsObservableList.add(division);
        }
        return divisionsObservableList;
    }
}
