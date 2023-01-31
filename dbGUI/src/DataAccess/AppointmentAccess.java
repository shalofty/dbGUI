package DataAccess;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.Appointments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentAccess {
    /**
     * get all appointments from database
     * */
    public static ObservableList<Appointments> getAllAppointments() {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList();
        try (Connection connection = JDBC.openConnection();
            PreparedStatement statement = connection.prepareStatement(SQLQueries.GET_ALL_APPOINTMENTS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointments appointment = AppointmentMapper.map(resultSet);
                appointmentsObservableList.add(appointment);
            }
            JDBC.closeConnection();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error retrieving appointments");
            alert.setContentText("Error: " + e.getMessage() + "\nCause: " + e.getCause());
            alert.showAndWait();
        }
        return appointmentsObservableList;
    }

    /**
     * delete appointment from database
     * */
    public static void deleteAppointment(Connection connection, int customer) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_APPOINTMENT)) {
            statement.setInt(1, customer);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error deleting appointment");
            alert.setContentText("Error: " + e.getMessage() + "\nCause: " + e.getCause());
            alert.showAndWait();
        }
    }
}