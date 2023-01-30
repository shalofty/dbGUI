package DataAccess;

import helper.JDBC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Customers;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

public class CustomerAccess {
    /**
     * Observablelist that takes all customer data from the database.
     * */
    public static ObservableList<Customers> getCustomers(Connection connection) throws SQLException {
        PreparedStatement statement = JDBC.getConnection().prepareStatement(SQLQueries.CUSTOMER_ACCESS);
        ResultSet set = statement.executeQuery();
        ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
        while (set.next()) {
            int customerID = set.getInt("Customer_ID");
            String customerName = set.getString("Customer_Name");
            String customerAddress = set.getString("Address");
            String customerPostalCode = set.getString("Postal_Code");
            String customerPhone = set.getString("Phone");
            int divisionID = set.getInt("Division_ID");
            String divisionName = set.getString("Division");
            // param ordering must match the constructor in the Customers model
            Customers customer = new Customers(customerName, customerAddress, customerPostalCode,
                    customerPhone, divisionName, customerID, divisionID);
            // add each customer to the observable list of
            customersObservableList.add(customer);
        }
        return customersObservableList;
    }
}