package dataAccess;

import helper.JDBC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public static ObservableList<Customers> getAllCustomers() throws SQLException {
        try (Connection connection = JDBC.openConnection(); // open the connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_CUSTOMERS_STATEMENT)) // prepare the statement
        {
            ObservableList<Customers> customersObservableList = FXCollections.observableArrayList(); // create an observable list of customers
            set = statement.executeQuery(); // execute the statement
            while (set.next()) { // while there is a next result
                Customers customer = CustomerMapper.map(set); // map the result set to a customer object
                customersObservableList.add(customer); // add the customer to the observable list
            }
            return customersObservableList; // return the observable list
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (set != null) {
                set.close(); // close the set
            }
            if (statement != null) {
                statement.close(); // close the statement
            }
            if (connection != null) {
                connection.close() ; // close the connection
            }
        }
    }

    /**
     * getAllCustomerNameStrings method to get all customer names from the database.
     * used to populate the customer name combo box
     * @return an observable list of strings
     * */
    public static ObservableList<String> getAllCustomerNameStrings() throws SQLException {
        try (Connection connection = JDBC.openConnection(); // open the connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_CUSTOMERS_STATEMENT)) // prepare the statement
        {
            ObservableList<String> customerNames = FXCollections.observableArrayList(); // create an observable list of strings
            set = statement.executeQuery(); // execute the statement
            while (set.next()) { // while there is a next result
                String customerName = set.getString("Customer_Name"); // get the customer name
                customerNames.add(customerName); // add the customer name to the observable list
            }
            return customerNames; // return the observable list
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (set != null) {
                set.close(); // close the set
            }
            if (statement != null) {
                statement.close(); // close the statement
            }
            if (connection != null) {
                connection.close(); // close the connection
            }
        }
    }

    /**
     * getCustomerNameByID method to get a customer name by ID
     * @param customerID the customer ID
     * @return the customer name
     * */
    public static String getCustomerNameByID(int customerID) throws SQLException {
        try (Connection connection = JDBC.openConnection(); // open the connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_CUSTOMER_NAME_BY_ID_STATEMENT)) // prepare the statement
        {
            statement.setInt(1, customerID); // set the customer ID
            set = statement.executeQuery(); // execute the statement
            if (set.next()) { // if there is a next result
                return set.getString("Customer_Name"); // return the customer name
            }
            else {
                return null; // return null if there is no customer name
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (set != null) {
                set.close(); // nullify set
            }
            if (statement != null) {
                statement.close(); // nullify statement
            }
            if (connection != null) {
                connection.close(); // close the connection
            }
        }
    }

    // a method to find the customer ID by their name
    /**
     * getCustomerIDByName method to get a customer ID by name
     * @param customerName the customer name
     * @return the customer ID
     * */
    public static int getCustomerID(String customerName) throws SQLException {
        try (Connection connection = JDBC.openConnection(); // open the connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.SELECT_CUSTOMER_ID_BY_NAME_STATEMENT))  // prepare the statement
        {
            statement.setString(1, customerName); // set the customer name
            set = statement.executeQuery(); // execute the statement
            if (set.next()) { // if there is a next result
                return set.getInt("Customer_ID"); // return the customer ID
            }
            else {
                return 0; // return 0 if there is no customer ID
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (set != null) {
                set.close(); // nullify set
            }
            if (statement != null) {
                statement.close(); // nullify statement
            }
            if (connection != null) {
                connection.close(); // close the connection
            }
        }
    }
}