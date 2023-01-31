package models;

import DataAccess.AppointmentAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class CompanyTime {
    public static LocalDateTime appointmentTimeUTC;


    public static LocalDateTime setAppointmentTime(LocalDateTime appointmentTime) {
        ZoneId localZone = ZoneId.systemDefault();
        ZoneId estZone = ZoneId.of("America/New_York");
        LocalDateTime appointmentTimeEST = appointmentTime.atZone(localZone).withZoneSameInstant(estZone).toLocalDateTime();
        LocalDateTime startEST = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(8, 0));
        LocalDateTime endEST = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(22, 0));

        if (appointmentTimeEST.isBefore(startEST) || appointmentTimeEST.isAfter(endEST)) {
            throw new IllegalArgumentException("Appointment must be between 8:00 AM and 10:00 PM EST");
        }

        appointmentTimeUTC = appointmentTime.atZone(localZone).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return appointmentTimeUTC;
    }

    public LocalDateTime getAppointmentTime() {
        ZoneId localZone = ZoneId.systemDefault();
        return appointmentTimeUTC.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone).toLocalDateTime();
    }

    public boolean isOverlapping(LocalDateTime appointmentTimeUTC) {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList(AppointmentAccess.getAllAppointments());

        for (Appointments appointment : appointments) {
            if (appointmentTimeUTC.isAfter(appointment.getStartTime()) && appointmentTimeUTC.isBefore(appointment.getEndTime())) {
                return true;
            }
        }
        return false;
    }
}
