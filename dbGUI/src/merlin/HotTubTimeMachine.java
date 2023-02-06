package merlin;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import dataAccess.AppointmentAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import models.Appointments;

/**
 * HotTubTimeMachine is a utility class that provides methods for getting hours and minutes, and manipulating dates and times.
 * @method getHours() returns an ObservableList of Strings representing the hours of the day.
 * @method getMinutes() returns an ObservableList of Strings representing the minutes of the hour.
 * @method isWithinBusinessHours() returns true if the appointment is within business hours.
 * @method convertToUTC() converts a LocalDateTime to UTC.
 * @method convertFromUTC() converts a LocalDateTime from UTC.
 * @method getDateTimeFromPickers() returns a LocalDateTime from the DatePicker and ComboBoxes.
 * Some of these aren't currently used, but I'm keeping them in case I need them later. Essentially scratch code
 * */
public class HotTubTimeMachine {

    private static final int START_HOUR = 8; // 8am
    private static final int END_HOUR = 22; // 10pm
    private static final ZoneId UTC = ZoneId.of("UTC"); // Universal Time Coordinated
    private static final ZoneId EST = ZoneId.of("America/New_York"); // Eastern Standard Time

    /**
     * getHours() returns an ObservableList of Strings representing the hours of the day.
     *
     * @return ObservableList<String>
     */
    public static ObservableList<String> getHours() {
        List<String> hours = new ArrayList<>(); // Create a new ArrayList of Strings to hold the hours
        for (int i = START_HOUR; i <= END_HOUR; i++) { // Loop through the hours
            hours.add(String.format("%02d", i)); // Add the hour to the ArrayList
        }
        return FXCollections.observableArrayList(hours); // Return the ArrayList as an ObservableList
    }

    /**
     * getMinutes() returns an ObservableList of Strings representing the minutes of the hour.
     *
     * @return ObservableList<String>
     */
    public static ObservableList<String> getMinutes() {
        List<String> minutes = new ArrayList<>();  // Create a new ArrayList of Strings to hold the minutes
        for (int i = 0; i < 60; i++) {  // Loop through the minutes
            minutes.add(String.format("%02d", i)); // Add the minute to the ArrayList
        }
        return FXCollections.observableArrayList(minutes); // Return the ArrayList as an ObservableList
    }

    /**
     * isWithinBusinessHours() returns true if the appointment is within business hours.
     */
    public static boolean withinBusinessHours(LocalDate startDate, LocalTime startTime, LocalTime endTime) {
        final int START_HOUR = 8; // 8am
        final int END_HOUR = 22; // 10pm
        try {
            LocalDateTime start = LocalDateTime.of(startDate, startTime); // Convert to LocalDateTime
            LocalDateTime end = LocalDateTime.of(startDate, endTime); // Convert to LocalDateTime

            // Convert to Eastern Standard Time
            start = start.atZone(EST).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            end = end.atZone(EST).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

            // Check if the appointment is within business hours
            return start.toLocalTime().isAfter(LocalTime.of(START_HOUR, 0))
                    && end.toLocalTime().isBefore(LocalTime.of(END_HOUR, 0))
                    && start.toLocalDate().equals(end.toLocalDate());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * convertToUTC() converts a LocalDateTime to UTC.
     */
    public static LocalDateTime convertToUTC(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(UTC).toLocalDateTime(); // Convert to UTC
    }

    /**
     * convertFromUTC() converts a LocalDateTime from UTC.
     */
    public static LocalDateTime convertFromUTC(LocalDateTime dateTime) {
        return dateTime.atZone(UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(); // Convert from UTC
    }

    /**
     * getDateTimeFromPickers() returns a LocalDateTime from the DatePicker and ComboBoxes.
     */
    public static LocalDateTime getDateTimeFromPickers(DatePicker datePicker, ComboBox<String> hourPicker, ComboBox<String> minutePicker) {
        LocalDate date = datePicker.getValue(); // Get the date from the DatePicker
        String hour = hourPicker.getValue(); // Get the hour from the ComboBox
        String minute = minutePicker.getValue(); // Get the minute from the ComboBox
        return LocalDateTime.of(date, LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute))); // Return the LocalDateTime
    }

    /**
     * isOverlappingAppointment() returns true if the appointment overlaps with another appointment.
     * @param start start time of the appointment
     * @param end end time of the appointment
     * @return boolean true if the appointment overlaps with another appointment
     * */
    public static boolean isOverlappingAppointment(LocalDateTime start, LocalDateTime end) throws SQLException {
        // Loop through all appointments
        for (Appointments appointment : AppointmentAccess.allAppointments()) {
            // Check if the appointment overlaps with another appointment
            if (appointment.getStartTime().isBefore(end) && appointment.getEndTime().isAfter(start)) {
                return true; // Return true if the appointment overlaps with another appointment
            }
        }
        return false; // Return false if the appointment does not overlap with another appointment
    }

    public static boolean checkForAppointments() throws SQLException {
        try {
            // Loop through all appointments
            for (Appointments appointment : AppointmentAccess.allAppointments()) {
                LocalDateTime startTime = HotTubTimeMachine.convertFromUTC(appointment.getStartTime()); // Convert to Eastern Standard Time
                LocalDate appointmentDate = appointment.getAppointmentDate(); // Get the appointment date
                System.out.println(startTime);
                // Check if the appointment is within 15 minutes
                if (appointmentDate.equals(LocalDate.now()) && startTime.isBefore(LocalDateTime.now().plusMinutes(15)) && startTime.isAfter(LocalDateTime.now())) {
                    return true; // Return true if the appointment is within 15 minutes
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false if no appointments are within 15 minutes
    }

    public static void appointmentsAlert() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Alert");
        alert.setHeaderText("Appointment Alert");
        alert.setContentText("You have an appointment within 15 minutes.");
        alert.showAndWait();
    }

    public static Timestamp[] interdimensionalWarpDrive(LocalDate localDate, LocalTime startTime, LocalTime endTime) {
        LocalDateTime localStartDateTime = LocalDateTime.of(localDate, startTime); // Convert to LocalDateTime
        LocalDateTime localEndDateTime = LocalDateTime.of(localDate, endTime); // Convert to LocalDateTime

        ZonedDateTime localStartZDT = localStartDateTime.atZone(ZoneId.systemDefault()); // Convert to ZonedDateTime
        ZonedDateTime localEndZDT = localEndDateTime.atZone(ZoneId.systemDefault()); // Convert to ZonedDateTime

        ZonedDateTime utcStartZDT = localStartZDT.withZoneSameInstant(ZoneId.of("UTC")); // Convert to UTC
        ZonedDateTime utcEndZDT = localEndZDT.withZoneSameInstant(ZoneId.of("UTC")); // Convert to UTC

        Timestamp dbStartTime = Timestamp.valueOf(utcStartZDT.toLocalDateTime()); // Convert to Timestamp
        Timestamp dbEndTime = Timestamp.valueOf(utcEndZDT.toLocalDateTime()); // Convert to Timestamp

        return new Timestamp[]{dbStartTime, dbEndTime}; // Return the Timestamps
    }

}