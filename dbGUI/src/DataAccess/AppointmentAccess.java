package DataAccess;

import Helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Appointments;

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

    /**
     * allAppointmentsByCustomerID gets all appointments for a customer
     * @param customerID
     * */
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

    /**
     * noAppointments checks if a customer has any appointments
     * @param customerID
     * */
    public static boolean noAppointments(int customerID) {
        ObservableList<Appointments> appointmentsObservableList = allAppointmentsByCustomerID(customerID); // get all appointments for customer
        return appointmentsObservableList.isEmpty(); // return true if list is empty
    }

    /**
     * allAppointmentsWithin7Days gets all appointments within the next 7 days
     * @return appointmentsObservableList of all appointments within 7 days
     * */
    public static ObservableList<Appointments> allAppointmentsWithin7Days() {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(); // create observable list
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.GET_ALL_APPOINTMENTS_WITHIN_7_DAYS_STATEMENT); // prepare statement
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

    /**
     * allAppointmentsWithin30Days gets all appointments within the next 30 days
     * @return appointmentsObservableList of all appointments within 30 days
     * */
    public static ObservableList<Appointments> allAppointmentsWithin30Days() {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(); // create observable list
        try {
            connection = JDBC.openConnection(); // open connection
            statement = connection.prepareStatement(SQLQueries.GET_ALL_APPOINTMENTS_WITHIN_30_DAYS_STATEMENT); // prepare statement
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
}