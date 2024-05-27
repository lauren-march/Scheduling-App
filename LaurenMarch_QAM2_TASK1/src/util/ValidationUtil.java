package util;

import exception.ValidationException;
import helper.AppointmentsDAO;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointments;
import validator.AppointmentOverlapValidator;
import validator.BusinessHoursValidator;
import validator.TimeValidator;

import java.time.LocalDateTime;

/**
 * This class is for program validation and error handling.
 */
public class ValidationUtil {

    /**
     * This lambda method validates start and end times to ensure start times are not after end times.
     * (Justification for lambda is where this gets called).
     */
    public static final TimeValidator validateTimes = (start, end) -> {
        if (start != null && end != null && !end.isAfter(start)) {
            showAlert("Error", "End time must be after start time.");
            return false;
        }
        return true;
    };

    /**
     * This lambda method validates whether a customer already has an appointment scheduled for this time slot/range.
     * (Justification for lambda is where this gets called).
     */
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

    /**
     * This lambda method validates whether the chosen times are withing business hours.
     * (Justification for lambda is where this gets called).
     */
    public static BusinessHoursValidator businessHoursValidator = (start, end) -> {
        if (!TimeUtil.isWithinBusinessHours(start, end)) {
            showAlert("Error", "Appointment times must be within business hours (8:00 AM - 10:00 PM ET).");
            return false;
        }
        return true;
    };

    // Validation for customer text fields
    public static String validateName(String name) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw  new ValidationException("Name cannot be blank.");
        }
        return name;
    }

    public static String validateAddress(String address, String country) throws ValidationException {
        if (address == null || address.isBlank()) {
            throw new ValidationException("Address cannot be blank.");
        }

        String[] addressParts = address.trim().split("\\s*,\\s*");

        String usAddressPattern = "^[0-9]+\\s+[^,]+,\\s*[^,]+$";
        String ukAddressPattern = "^[0-9]+\\s+[^,]+,\\s*[^,]+,\\s*[^,]+$";
        String canadaAddressPattern = "^[0-9]+\\s+[^,]+,\\s*[^,]+$";

        switch (country.toUpperCase()) {
            case "U.S":
                if (!address.matches(usAddressPattern)) {
                    throw new ValidationException("U.S. address must be in the format: '123 ABC Street, City'.");
                }
                break;
            case "UK":
                if (!address.matches(ukAddressPattern)) {
                    throw new ValidationException("UK address must be in the format: '123 ABC Street, City, London'.");
                }
                break;
            case "CANADA":
                if (!address.matches(canadaAddressPattern)) {
                    throw new ValidationException("Canadian address must be in the format: '123 ABC Street, City'.");
                }
                break;
            default:
                throw new ValidationException("Invalid country for address validation.");
        }

        return address;
    }


    public static String validatePhoneNumber(String phoneNumber, String country) throws ValidationException {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new ValidationException("Phone number cannot be blank.");
        }

        String usPhonePattern = "^(\\+1)?\\d{10}$";
        String ukPhonePattern = "^(\\+44)?\\d{10}$";
        String canadaPhonePattern = "^(\\+1)?\\d{10}$";

        switch (country.toUpperCase()) {
            case "U.S":
                if (!phoneNumber.matches(usPhonePattern)) {
                    throw new ValidationException("U.S. phone number must be 10 digits optionally preceded by +1.");
                }
                break;
            case "UK":
                if (!phoneNumber.matches(ukPhonePattern)) {
                    throw new ValidationException("UK phone number must be 10 digits optionally preceded by +44.");
                }
                break;
            case "CANADA":
                if (!phoneNumber.matches(canadaPhonePattern)) {
                    throw new ValidationException("Canadian phone number must be 10 digits optionally preceded by +1.");
                }
                break;
            default:
                throw new ValidationException("Invalid country for phone number validation.");
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

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}







