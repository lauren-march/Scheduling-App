package util;

import helper.AppointmentsDAO;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointments;
import validator.AppointmentOverlapValidator;
import validator.BusinessHoursValidator;
import validator.TimeValidator;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ValidationUtil {

    public static final TimeValidator validateTimes = (start, end) -> {
        if (start != null && end != null && !end.isAfter(start)) {
            showAlert("Error", "End time must be after start time.");
            return false;
        }
        return true;
    };

    public static final AppointmentOverlapValidator validateOverlappingAppointments = (customerId, start, end) -> {
        ObservableList<Appointments> appointments = AppointmentsDAO.getAppointmentsByCustomerId(customerId);

        for (Appointments appointment : appointments) {
            LocalDateTime appointmentStart = appointment.getStart();
            LocalDateTime appointmentEnd = appointment.getEnd();

            if (start.isBefore(appointmentEnd) && end.isAfter(appointmentStart)) {
                showAlert("Error", "The appointment overlaps with an existing appointment.");
                return false;
            }
        }
        return true;
    };


    public static BusinessHoursValidator businessHoursValidator = (start, end) -> {
        if (!TimeUtil.isWithinBusinessHours(start, end)) {
            showAlert("Error", "Appointment times must be within business hours (8:00 AM - 10:00 PM ET).");
            return false;
        }
        return true;
    };

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
