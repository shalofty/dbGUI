package DataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Exceptions.ExceptionHandler;

public class AppointmentAccess {
    public static Connection connection = null;
    public static PreparedStatement statement = null;
    public static ResultSet set = null;

    /**
     * get all appointments from database
     * @return appointmentsObservableList of all appointments
     * */
    public static ObservableList<Appointments> allAppointments() {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(); // create observable list
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.GET_ALL_APPOINTMENTS_STATEMENT); // prepare statement
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                Appointments appointment = AppointmentMapper.map(set); // map result set to appointment object
                appointmentsObservableList.add(appointment); // add appointment to observable list
            }
            return appointmentsObservableList; // return observable list
        } catch (SQLException e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw new RuntimeException(e);
        } finally {
            if (set != null) {
                set = null; // close result set
            }
            if (statement != null) {
                statement = null; // close statement
            }
            if (connection != null) {
                connection = JDBC.closeConnection(); // close connection
            }
        }
    }

    // a method that uses a customerID to get all appointments for that customer
    public static ObservableList<Appointments> allAppointmentsByCustomerID(int customerID) {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(); // create observable list
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.GET_ALL_APPOINTMENTS_BY_CUSTOMER_ID_STATEMENT); // prepare statement
            statement.setInt(1, customerID); // set customerID
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                Appointments appointment = AppointmentMapper.map(set); // map result set to appointment object
                appointmentsObservableList.add(appointment); // add appointment to observable list
            }
            return appointmentsObservableList; // return observable list
        } catch (SQLException e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw new RuntimeException(e);
        } finally {
            if (set != null) {
                set = null; // close result set
            }
            if (statement != null) {
                statement = null; // close statement
            }
            if (connection != null) {
                connection = JDBC.closeConnection(); // close connection
            }
        }
    }

    // a function that checks the size of the returned ObservableList from allAppointmentsByCustomerID and returns true if the list is empty
    public static boolean noAppointments(int customerID) {
        ObservableList<Appointments> appointmentsObservableList = allAppointmentsByCustomerID(customerID); // get all appointments for customer
        return appointmentsObservableList.isEmpty(); // return true if list is empty
    }


}