package DataAccess;

import Exceptions.ExceptionHandler;
import Helper.JDBC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Customers;

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
    public static ObservableList<Customers> getAllCustomers() {
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

    /**
     * getAllCustomerNameStrings method to get all customer names from the database.
     * used to populate the customer name combo box
     * @return an observable list of strings
     * */
    public static ObservableList<String> getAllCustomerNameStrings() {
        try {
            ObservableList<String> customerNames = FXCollections.observableArrayList(); // create an observable list of strings
            connection = JDBC.openConnection(); // open the connection
            statement = connection.prepareStatement(SQLQueries.SELECT_CUSTOMERS_STATEMENT); // prepare the statement
            set = statement.executeQuery(); // execute the statement
            while (set.next()) { // while there is a next result
                String customerName = set.getString("Customer_Name"); // get the customer name
                customerNames.add(customerName); // add the customer name to the observable list
            }
            return customerNames; // return the observable list
        } catch (SQLException e) {
            ExceptionHandler.eAlert(e);
            throw new RuntimeException(e);
        } finally {
            if (set != null) {
                set = null;
            }
            if (statement != null) {
                statement = null;
            }
            if (connection != null) {
                connection = JDBC.closeConnection();
            }
        }
    }

    /**
     * getCustomerNameByID method to get a customer name by ID
     * @param customerID the customer ID
     * @return the customer name
     * */
    public static String getCustomerNameByID(int customerID) {
        try {
            connection = JDBC.openConnection(); // open the connection
            statement = connection.prepareStatement(SQLQueries.SELECT_CUSTOMER_NAME_BY_ID_STATEMENT); // prepare the statement
            statement.setInt(1, customerID); // set the customer ID
            set = statement.executeQuery(); // execute the statement
            if (set.next()) { // if there is a next result
                return set.getString("Customer_Name"); // return the customer name
            }
            return null;
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

    // a method to find the customer ID by their name
    public static int getCustomerIDByName(String customerName) {
        try {
            connection = JDBC.openConnection(); // open the connection
            statement = connection.prepareStatement(SQLQueries.SELECT_CUSTOMER_ID_BY_NAME_STATEMENT); // prepare the statement
            statement.setString(1, customerName); // set the customer name
            set = statement.executeQuery(); // execute the statement
            if (set.next()) { // if there is a next result
                return set.getInt("Customer_ID"); // return the customer ID
            }
            return 0;
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