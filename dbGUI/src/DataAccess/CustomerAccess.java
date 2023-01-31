package DataAccess;

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
    /**
     * Observablelist that takes all customer data from the database.
     * */
    public static ObservableList<Customers> getCustomers() {
        ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
        try (Connection connection = JDBC.openConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQLQueries.SELECT_CUSTOMERS_STATEMENT);
            ResultSet set = statement.executeQuery();
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
            JDBC.closeConnection();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error retrieving customers");
            alert.setContentText("Error: " + e.getMessage() + "\nCause: " + e.getCause());
            alert.showAndWait();
            e.printStackTrace();
        }
        return customersObservableList;
    }
}