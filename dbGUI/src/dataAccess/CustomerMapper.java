package dataAccess;

import models.Customers;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper {
    /**
     * map customer data from database to customer object
     * more efficient than using setters
     * @param resultSet result set from database
     * @return customer object with data from database
     * */
    public static Customers map(ResultSet resultSet) throws SQLException {
        int customerID = resultSet.getInt("Customer_ID"); // Customer_ID
        String customerName = resultSet.getString("Customer_Name"); // Customer_Name
        String address = resultSet.getString("Address"); // Address
        String postalCode = resultSet.getString("Postal_Code"); // Postal_Code
        String phone = resultSet.getString("Phone"); // Phone
        String countryDivision = resultSet.getString("Division"); // Division
        int divisionID = resultSet.getInt("Division_ID"); // Division_ID
        return new Customers(customerName, address, postalCode, phone, countryDivision, customerID, divisionID);
    }
}
