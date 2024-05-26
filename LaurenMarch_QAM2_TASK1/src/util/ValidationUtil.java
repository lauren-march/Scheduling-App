package util;

import com.mysql.cj.conf.PropertyDefinitions;
import com.mysql.cj.xdevapi.Schema;
import exception.ValidationException;
import helper.AppointmentsDAO;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointments;
import validator.AppointmentOverlapValidator;
import validator.BusinessHoursValidator;
import validator.TimeValidator;

import java.time.LocalDateTime;

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

    // Validation for customer text fields
    public static String validateName(String name) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw  new ValidationException("Name cannot be blank.");
        }
        return name;
    }

    public static String validateAddress(String address) throws ValidationException {
        if (address == null || address.isBlank()) {
            throw new ValidationException("Address cannot be blank.");
        }
        return  address;
    }

    public static String validatePhoneNumber(String phoneNumber) throws ValidationException {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new ValidationException("Phone number cannot be blank");
        }
        if (!phoneNumber.matches("\\d{10}")) {
            throw new ValidationException("Phone number must be 10 digits, do no include '-' between digits");
        }
        return phoneNumber;
    }

    public static String validatePostalCode(String postalCode, String country) throws ValidationException {
        if (postalCode == null || postalCode.isBlank()) {
            throw new ValidationException("Postal code cannot be blank.");
        }

        switch (country.toUpperCase()) {
            case "U.S":
                if (!postalCode.matches("\\d{5}")) {
                    throw new ValidationException("US postal code must be 5 numeric digits.");
                }
                break;
            case "UK":
                if (!postalCode.matches("^[A-Z]{1,2}[0-9R][0-9A-Z]? ?[0-9][A-Z]{2}$")) {
                    throw new ValidationException("UK postal code must follow the format 'A9 9AA' or 'A9A 9AA'.");
                }
                break;
            case "CANADA":
                if (!postalCode.matches("^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$")) {
                    throw new ValidationException("Canadian postal code must follow the format 'A1A 1A1'.");
                }
                break;
            default:
                throw new ValidationException("Invalid country for postal code validation.");
        }

        return postalCode;
    }

    //Validation for appointment text fields
    public static String validateTitle(String title) throws ValidationException {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Title cannot be blank.");
        }
        return title;
    }

    public static String validateDescription(String description) throws ValidationException {
        if (description == null || description.isBlank()){
            throw new ValidationException("Description cannot be blank.");
        }
        return description;
    }

    public static String validateLocation(String location) throws ValidationException {
        if (location == null || location.isBlank()){
            throw new ValidationException("Location cannot be blank.");
        }
        return location;
    }

    public static String validateType(String type) throws ValidationException {
        if (type == null || type.isBlank()){
            throw new ValidationException("Type cannot be blank.");
        }
        return type;
    }

}







