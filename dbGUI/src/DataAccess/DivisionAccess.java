package DataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionAccess extends Division {
    /**
     * @param divisionID
     * @param divisionName
     * @param countryID
     * a constructor that takes the divisionID, divisionName, and countryID
     * */
    public DivisionAccess(int divisionID, String divisionName, int countryID) {
        super(divisionID, divisionName, countryID);
    }

    /**
     * @return ObservableList<DivisionAccess>
     * @throws SQLException
     * */
    public static ObservableList<DivisionAccess> getDivisions() throws SQLException {
        PreparedStatement statement = JDBC.openConnection().prepareStatement(SQLQueries.SELECT_DIVISIONS);
        ResultSet set = statement.executeQuery();
        ObservableList<DivisionAccess> divisionsObservableList = FXCollections.observableArrayList();
        while (set.next()) {
            int divisionID = set.getInt("Division_ID");
            String divisionName = set.getString("Division");
            int countryID = set.getInt("Country_ID");
            // param ordering must match the constructor in the Customers model
            DivisionAccess division = new DivisionAccess(divisionID, divisionName, countryID);
            // add each customer to the observable list of
            divisionsObservableList.add(division);
        }
        JDBC.closeConnection(); // close the connection
        return divisionsObservableList;
    }
}
