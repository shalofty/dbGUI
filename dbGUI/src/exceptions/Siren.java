package exceptions;

import javafx.scene.control.Alert;

import java.sql.SQLException;

public class Siren {

    /**
     * appointmentsAlert() displays an alert if the user has an appointment within 15 minutes
     * */
    public static void appointmentsAlert() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Alert");
        alert.setHeaderText("Appointment Alert");
        alert.setContentText("You have an appointment within 15 minutes.");
        alert.showAndWait();
    }

    /**
     * overlapAlert() displays an alert if the user is trying to schedule an appointment that overlaps with a previously scheduled appointment
     * */
    public static void overlapAlert() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Alert");
        alert.setHeaderText("Appointment Alert");
        alert.setContentText("The appointment you're trying to schedule overlaps with a previously scheduled appointment.");
        alert.showAndWait();
    }

    public static void emptyAlert() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Alert");
        alert.setHeaderText("Appointment Alert");
        alert.setContentText("Please fill out all fields, and make sure all data in the fields is correct.");
        alert.showAndWait();
    }
}
