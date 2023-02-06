package exceptions;

import javafx.scene.control.Alert;
import models.Appointments;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Siren class functions as a library of alert methods
 * I create this class to keep the code clean and easy to read
 * */
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

    /**
     * emptyAlert() displays an alert if the user is trying to schedule an appointment without filling out all fields
     * */
    public static void emptyAlert() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Alert");
        alert.setHeaderText("Appointment Alert");
        alert.setContentText("Please fill out all fields, and make sure all data in the fields is correct.");
        alert.showAndWait();
    }

    /**
     * appointmentConfirm() displays a confirmation alert if the user is trying to schedule an appointment
     * */
    public static boolean appointmentConfirm() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Appointment Confirmation");
        confirm.setHeaderText("Appointment Confirmation");
        confirm.setContentText("Are you sure you want to schedule this appointment?");
        confirm.showAndWait();
        return confirm.getResult().getText().equals("OK");
    }

    /**
     * customerConfirm() displays a confirmation alert if the user is trying to schedule an appointment
     * */
    public static boolean addCustomerConfirm() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Customer Confirmation");
        confirm.setHeaderText("Customer Confirmation");
        confirm.setContentText("Are you sure you want to add this customer?");
        confirm.showAndWait();
        return confirm.getResult().getText().equals("OK");
    }

    public static boolean modifyCustomerConfirm() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Customer Confirmation");
        confirm.setHeaderText("Customer Confirmation");
        confirm.setContentText("Are you sure you want to modify this customer?");
        confirm.showAndWait();
        return confirm.getResult().getText().equals("OK");
    }

    public static boolean deleteConfirm() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Confirmation");
        confirm.setHeaderText("Delete Confirmation");
        confirm.setContentText("Are you sure you want to delete this?");
        confirm.showAndWait();
        return confirm.getResult().getText().equals("OK");
    }

    /**
     * quick scratch code, create package for these later
     * */
    public static void successAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText("Success");
        successAlert.setContentText("Operation successful.");
        successAlert.showAndWait();
    }

    /**
     * hasAppointments() displays an error message if the customer has appointments.
     * */
    public static void hasAppointments() {
        Alert hasAppointments = new Alert(Alert.AlertType.ERROR);
        hasAppointments.setTitle("Error");
        hasAppointments.setHeaderText("Customer has Appointments");
        hasAppointments.setContentText("This customer has appointments and cannot be deleted.");
        hasAppointments.showAndWait();
    }

    /**
     * invalidCredentials() displays an error message if the credentials are invalid.
     * @param currentLocale the current locale
     * */
    public static void accessDenied(String currentLocale) {
        if (currentLocale.equals("fr")) {
            ResourceBundle languageBundles = ResourceBundle.getBundle("bundles/fr_lang", Locale.FRANCE); // gets the login resource bundle
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(languageBundles.getString("errorTitle.text"));
            alert.setHeaderText(languageBundles.getString("errorHeader.text"));
            alert.setContentText(languageBundles.getString("errorText.text"));
            alert.showAndWait();
        }
        else if (currentLocale.equals("en")) {
            ResourceBundle languageBundles = ResourceBundle.getBundle("bundles/en_lang", Locale.US); // gets the login resource bundle
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(languageBundles.getString("errorTitle.text"));
            alert.setHeaderText(languageBundles.getString("errorHeader.text"));
            alert.setContentText(languageBundles.getString("errorContent.text"));
            alert.showAndWait();
        }
    }

    // fifteenminAlert() displays an alert if the user has an appointment within 15 minutes
    public static void fifteenMinuteAlert(Appointments appointment) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Alert");
        alert.setHeaderText("Appointment Alert");
        String appointmentID = String.valueOf(appointment.getAppointmentID()); // convert appointmentID to string
        String appointmentDate = String.valueOf(appointment.getAppointmentDate()); // convert appointmentDate to string
        LocalDateTime appointmentTimeRaw = appointment.getStartTime(); // get appointment time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a"); // format appointment time
        String appointmentTime = appointmentTimeRaw.format(formatter); // convert appointmentTime to string
        alert.setContentText("You have an appointment within 15 minutes." +
                            "\nAppointment ID: " + appointmentID +
                            "\nAppointment Date: " + appointmentDate +
                            "\nAppointment Time: " + appointmentTime);
        alert.showAndWait();
    }
}
