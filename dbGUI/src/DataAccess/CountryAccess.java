package DataAccess;

import Exceptions.ExceptionHandler;
import Helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryAccess extends Country {

    public static Connection connection = null;
    public static PreparedStatement statement = null;
    public static ResultSet set = null;
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
        try {
            ObservableList<CountryAccess> countriesObservableList = FXCollections.observableArrayList();
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.SELECT_COUNTRIES); // create statement
            set = statement.executeQuery(); // execute statement
            // loop through result set
            while (set.next()) {
                int countryID = set.getInt("Country_ID"); // get country ID
                String countryName = set.getString("Country"); // get country name
                CountryAccess country = new CountryAccess(countryID, countryName); // create country object
                countriesObservableList.add(country); // add country to observable list
            }
            return countriesObservableList; // return observable list
        } catch (Exception e) {
            ExceptionHandler.eAlert(e);
            throw e;
        } finally {
            if (set != null) set.close(); // close result set
            if (statement != null) statement.close(); // close statement
            if (connection != null) connection.close(); // close connection
        }
    }
}