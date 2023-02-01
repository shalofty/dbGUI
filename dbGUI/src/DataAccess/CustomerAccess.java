package DataAccess;

import Exceptions.ExceptionHandler;
import helper.JDBC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.Customers;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

public class CustomerAccess {
    public static Connection connection = null;
    public static PreparedStatement statement = null;
    public static ResultSet set = null;

    /**
     * Observablelist that takes all customer data from the database.
     * */
    public static ObservableList<Customers> getCustomers() {
        try {
            ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
            connection = JDBC.openConnection();
            statement = connection.prepareStatement(SQLQueries.SELECT_CUSTOMERS_STATEMENT);
            set = statement.executeQuery();
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
                customersObservableList.add(customer);}
            return customersObservableList; // return the observable list
        } catch (SQLException e) {
            ExceptionHandler.eAlert(e);
            throw new RuntimeException(e);
        }
        finally {
            if (set != null) {
                set = null; // nullify set
            }
            if (statement != null) {
                statement = null; // nullify statement
            }
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
        }
    }
}