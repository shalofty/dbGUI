package exceptions;

import javafx.scene.control.Alert;

/**
 * Confundo class is used to display error messages to the user
 * Confundo is a spell from Harry Potter that confuses people
 * @method businessHours() displays an error message if the user tries to schedule an appointment outside of business hours
 * @method incorrectPassword() displays an error message if the user enters an incorrect password
 * */
public class Confundo {
    public static void businessHours() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Appointment outside of business hours");
        alert.setContentText("Appointments must be between 8am and 10pm EST.");
        alert.showAndWait();
    }

    public static void incorrectPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Incorrect Password");
        alert.setContentText("Please enter a valid password.");
        alert.showAndWait();
    }
}
