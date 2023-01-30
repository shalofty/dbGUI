package DataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryAccess extends Country {

    private static final String SQL_GET_COUNTRIES = "SELECT Country_ID, Country FROM countries";

    /**
     * @param countryID
     * @param countryName
     */
    public CountryAccess(int countryID, String countryName) {
        super(countryID, countryName);
    }

    /**
     * @return ObservableList<CountryAccess>
     * @throws SQLException
     */
    public static ObservableList<CountryAccess> getAllCountries() throws SQLException {
        ObservableList<CountryAccess> countriesObservableList = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL_GET_COUNTRIES);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int countryID = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            CountryAccess country = new CountryAccess(countryID, countryName);
            countriesObservableList.add(country);
        }
        return countriesObservableList;
    }
}