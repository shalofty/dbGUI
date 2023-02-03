package exceptions;

import javafx.scene.control.Alert;

public class Confundo {
    public static void businessHours() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Appointment outside of business hours");
        alert.setContentText("Appointments must be between 8am and 10pm EST.");
        alert.showAndWait();
    }
}
