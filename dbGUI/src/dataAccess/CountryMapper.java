package dataAccess;

import models.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * a class that maps the country data from the database to a country object
 * more efficient than using setters
 * */
public class CountryMapper {
    public static Country mapCountry(ResultSet set) throws SQLException {
        int countryID = set.getInt("Country_ID"); // Country_ID
        String countryName = set.getString("Country"); // Country
        return new Country(countryID, countryName); // return the country
    }
}
