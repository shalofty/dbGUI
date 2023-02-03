package Merlin;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import Exceptions.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

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
    public static boolean isWithinBusinessHours(LocalDate startDate, LocalTime startTime, LocalTime endTime) {
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
            ExceptionHandler.eAlert(e);
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
}