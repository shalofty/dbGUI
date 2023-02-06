package merlin;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

import dataAccess.AppointmentAccess;
import javafx.scene.control.Alert;
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
    private static final ZoneId UTC = ZoneId.of("UTC"); // Universal Time Coordinated
    private static final ZoneId EST = ZoneId.of("America/New_York"); // Eastern Standard Time

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
     * timeTransmutation() returns a formatted LocalTime[] of the start and end times
     * @param startTime the start time of the appointment
     * @param endTime the end time of the appointment
     * @return LocalTime[] the start and end times
     * */
    public static LocalTime[] timeTransmutation(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a"); // create a DateTimeFormatter object
        LocalTime localStartTime = LocalTime.parse(startTime, formatter); // parse the start time
        LocalTime localEndTime = LocalTime.parse(endTime, formatter); // parse the end time
        return new LocalTime[] {localStartTime, localEndTime};
    }

    /**
     * interdimensionalUTCWarpDrive() returns a Timestamp[] of the start and end times in UTC.
     * @param localDate the date of the appointment
     * @param startTime the start time of the appointment
     * @param endTime the end time of the appointment
     * @return Timestamp[] the start and end times in UTC
     * */
    public static Timestamp[] interdimensionalUTCWarpDrive(LocalDate localDate, LocalTime startTime, LocalTime endTime) {
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