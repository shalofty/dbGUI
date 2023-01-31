package DataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryAccess extends Country {

    /**
     * @param countryID
     * @param countryName
     * a constructor that takes the countryID and countryName
     */
    public CountryAccess(int countryID, String countryName) {
        super(countryID, countryName);
    }

    /**
     * @return ObservableList<CountryAccess>
     * @throws SQLException
     */
    public static ObservableList<CountryAccess> getCountries() throws SQLException {
        ObservableList<CountryAccess> countriesObservableList = FXCollections.observableArrayList();
        PreparedStatement statement = JDBC.openConnection().prepareStatement(SQLQueries.SELECT_COUNTRIES);
        ResultSet set = statement.executeQuery();
        while (set.next()) {
            int countryID = set.getInt("Country_ID");
            String countryName = set.getString("Country");
            CountryAccess country = new CountryAccess(countryID, countryName);
            countriesObservableList.add(country);
        }
        JDBC.closeConnection(); // close connection
        return countriesObservableList;
    }
}