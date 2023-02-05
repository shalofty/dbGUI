package dataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Country;

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
            statement = connection.prepareStatement(QueryChronicles.SELECT_COUNTRIES); // create statement

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
            throw e;
        } finally {
            if (set != null) set.close(); // close result set
            if (statement != null) statement.close(); // close statement
            if (connection != null) connection.close(); // close connection
        }
    }

    /**
     * getCountryID method gets the country ID from the country name
     * @param countryName as a String
     * */
    public static int getCountryNamebyID(String countryName) throws SQLException {
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(QueryChronicles.SELECT_COUNTRY_ID_BY_COUNTRY_NAME); // create statement
            statement.setString(1, countryName); // set country name
            set = statement.executeQuery(); // execute statement
            if (set.next()) {
                return set.getInt("Country_ID"); // return country ID
            }
            return 0;
        } catch (Exception e) {
            throw e;
        } finally {
            if (set != null) set.close(); // close result set
            if (statement != null) statement.close(); // close statement
            if (connection != null) connection.close(); // close connection
        }
    }


    /**
     * getCountryName method gets the country name from the division ID
     * @param divisionID as an int
     * */
    public static String innerJoin(int divisionID) throws SQLException {
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(QueryChronicles.INNER_JOIN_STATEMENT); // create statement
            statement.setInt(1, divisionID); // set division ID
            set = statement.executeQuery(); // execute statement
            System.out.println("SET: " + set.toString());
            if (set.next()) {
                return set.getString("Country");
            }
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (set != null) set.close(); // close result set
            if (statement != null) statement.close(); // close statement
            if (connection != null) connection.close(); // close connection
        }
    }
}