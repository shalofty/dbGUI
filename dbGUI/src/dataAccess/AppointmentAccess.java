package dataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentAccess {
    public static Connection connection = null;
    public static PreparedStatement statement = null;
    public static ResultSet set = null;

    /**
     * get all appointments from database
     * @return appointmentsObservableList of all appointments
     * */
    public static ObservableList<Appointments> allAppointments() throws SQLException {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(); // create observable list
        try (Connection connection = JDBC.openConnection(); // open connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.GET_ALL_APPOINTMENTS_STATEMENT))  // prepare statement
        {
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                Appointments appointment = AppointmentMapper.map(set); // map result set to appointment object
                appointmentsObservableList.add(appointment); // add appointment to observable list
            }
            return appointmentsObservableList; // return observable list
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (set != null) {
                set.close(); // close result set
            }
            if (statement != null) {
                statement.close(); // close statement
            }
            if (connection != null) {
                connection.close(); // close connection
            }
        }
    }

    /**
     * allAppointmentsByCustomerID gets all appointments for a customer
     * @param customerID customer ID to get appointments
     * */
    public static ObservableList<Appointments> allAppointmentsByCustomerID(int customerID) throws SQLException {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(); // create observable list
        try (Connection connection = JDBC.openConnection(); // open connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.GET_ALL_APPOINTMENTS_BY_CUSTOMER_ID_STATEMENT)) // prepare statement
        {
            statement.setInt(1, customerID); // set customerID
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                Appointments appointment = AppointmentMapper.map(set); // map result set to appointment object
                appointmentsObservableList.add(appointment); // add appointment to observable list
            }
            return appointmentsObservableList; // return observable list
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (set != null) {
                set.close(); // close result set
            }
            if (statement != null) {
                statement.close(); // close statement
            }
            if (connection != null) {
                connection.close(); // close connection
            }
        }
    }

    /**
     * noAppointments checks if a customer has any appointments
     * @param customerID customer ID to check appointments
     * */
    public static boolean noAppointments(int customerID) throws SQLException {
        ObservableList<Appointments> appointmentsObservableList = allAppointmentsByCustomerID(customerID); // get all appointments for customer
        return appointmentsObservableList.isEmpty(); // return true if list is empty
    }

    /**
     * allAppointmentsWithin7Days gets all appointments within the next 7 days
     * @return appointmentsObservableList of all appointments within 7 days
     * */
    public static ObservableList<Appointments> allAppointmentsWithin7Days() throws SQLException {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(); // create observable list
        try (Connection connection = JDBC.openConnection(); // open connection
             PreparedStatement statement = connection.prepareStatement(QueryChronicles.GET_ALL_APPOINTMENTS_WITHIN_7_DAYS_STATEMENT)) // prepare statement
        {
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                Appointments appointment = AppointmentMapper.map(set); // map result set to appointment object
                appointmentsObservableList.add(appointment); // add appointment to observable list
            }
            return appointmentsObservableList; // return observable list
        } catch (SQLException e) {
            e.printStackTrace(); // print stack trace
            throw new RuntimeException(e);
        } finally {
            if (set != null) {
                set.close(); // close result set
            }
            if (statement != null) {
                statement.close(); // close statement
            }
            if (connection != null) {
                connection.close(); // close connection
            }
        }
    }

    /**
     * allAppointmentsWithin30Days gets all appointments within the next 30 days
     * @return appointmentsObservableList of all appointments within 30 days
     * */
    public static ObservableList<Appointments> allAppointmentsWithin30Days() throws SQLException {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(); // create observable list
        try (Connection connection = JDBC.openConnection(); PreparedStatement statement = connection.prepareStatement(QueryChronicles.GET_ALL_APPOINTMENTS_WITHIN_30_DAYS_STATEMENT)) // prepare statement
        {
            set = statement.executeQuery(); // execute query
            // loop through result set
            while (set.next()) {
                Appointments appointment = AppointmentMapper.map(set); // map result set to appointment object
                appointmentsObservableList.add(appointment); // add appointment to observable list
            }
            return appointmentsObservableList; // return observable list
        } catch (SQLException e) {
            e.printStackTrace(); // print stack trace
            throw new RuntimeException(e);
        } finally {
            if (set != null) {
                set.close(); // close result set
            }
            if (statement != null) {
                statement.close(); // close statement
            }
            if (connection != null) {
                connection.close(); // close connection
            }
        }
    }
}