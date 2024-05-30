package controller;

import exception.ValidationException;
import helper.AppointmentsDAO;
import helper.ContactsDAO;
import helper.CustomerDAO;
import helper.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Appointments;
import model.Contacts;
import util.TimeUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

/**
 * This class handles the functionality and UI elements of the UpdateAppointmentForm.
 */
public class UpdateAppointmentFormController {

    @FXML
    private TextField appointmentIdTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private ComboBox<Contacts> contactsComboBox;
    @FXML
    private TextField typeTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    @FXML
    private ComboBox<Integer> customerComboBox;
    @FXML
    private ComboBox<Integer> usersComboBox;

    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button cancelAddAppointmentButton;

    private Appointments selectedAppointment;

    /**
     * This is the initialize method and is automatically called by JavaFx when this form loads.
     * It adds data to UI elements for UpdateAppointmentForm.
     */
    @FXML
    public void initialize() {
        ObservableList<Contacts> contacts = ContactsDAO.getContactsList();
        contactsComboBox.setItems(contacts);

        ObservableList<Integer> customers = CustomerDAO.getCustomerIdList();
        customerComboBox.setItems(customers);

        ObservableList<Integer> users = UsersDAO.getUserIdList();
        usersComboBox.setItems(users);

        initializeTimeComboBoxes();
    }

    /**
     * This method handles the functionality of the Update button on the UpdateAppointmentForm.
     * First it gets the values entered in the corresponding fields and stores them to corresponding variables.
     * Textfields are checked with ValidationUtil methods to make sure they are not blank.
     * Then it runs a check to make sure that there isn't null fields for comboboxes.
     * Then it runs checks for validation lambdas.
     * I chose to use lambdas for ValidateUtil.validateTime, validateOverlappingAppointments, and businessHoursValidator
     * since these can be used in various places in the application (reusable) and it saved about 11-12 lines of code for each
     * which makes my code more readable and concise.
     * Lastly it converts the localtime to UTC and updates and creates a new appointment object with the updates
     * that gets updated to the appointments table in the database with the AppointmentsDAO.updateAppointment() method that uses SQL UPDATE statement.
     */
    @FXML
    public void handleUpdateAppointmentButton() {
        try {
            String title = ValidationUtil.validateTitle(titleTextField.getText());
            String description = ValidationUtil.validateDescription(descriptionTextField.getText());
            String location = ValidationUtil.validateLocation(locationTextField.getText());
            String type = ValidationUtil.validateType(typeTextField.getText());
            Contacts contact = contactsComboBox.getValue();
            Integer customerId = customerComboBox.getValue();
            Integer userId = usersComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalTime startTime = startTimeComboBox.getValue();
            LocalTime endTime = endTimeComboBox.getValue();

            if (contact == null || customerId == null || userId == null || startDate == null || startTime == null || endTime == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }


            LocalDateTime startLocalDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime endLocalDateTime = LocalDateTime.of(startDate, endTime);
            ZoneId localZoneId = ZoneId.systemDefault();
            ZonedDateTime localStartZonedDateTime = ZonedDateTime.of(startLocalDateTime,localZoneId);
            ZonedDateTime localEndZonedDateTime = ZonedDateTime.of(endLocalDateTime,localZoneId);

            System.out.println(localStartZonedDateTime);
            System.out.println(localEndZonedDateTime);

            if (!ValidationUtil.validateTimes.validate(startLocalDateTime, endLocalDateTime)) {
                return;
            }

            if (!ValidationUtil.validateOverlappingAppointments.validate(customerId, startLocalDateTime, endLocalDateTime)) {
                return;
            }

            if (!ValidationUtil.businessHoursValidator.validate(TimeUtil.toEST(startLocalDateTime), TimeUtil.toEST(endLocalDateTime))) {
                showAlert("Error", "Appointment times must be within business hours (8:00 AM - 10:00 PM ET).");
                return;
            }

            ZoneId utcZoneId = ZoneId.of("UTC");
            ZonedDateTime startUTC = ZonedDateTime.ofInstant(localStartZonedDateTime.toInstant(), utcZoneId);
            ZonedDateTime endUTC = ZonedDateTime.ofInstant(localEndZonedDateTime.toInstant(), utcZoneId);

            System.out.println(startUTC);
            System.out.println(endUTC);

            LocalDateTime now = LocalDateTime.now();
            String currentUser = LoginFormController.currentUser;

            Appointments updatedAppointment = new Appointments(
                    Integer.parseInt(appointmentIdTextField.getText()),
                    title, description, location, type,
                    startUTC.toLocalDateTime(), endUTC.toLocalDateTime(),
                    now, currentUser, now, currentUser,
                    customerId, userId, contact.getContactId()
            );

            try {
                AppointmentsDAO.updateAppointment(updatedAppointment, startUTC, endUTC);
                navigateToAppointmentsForm();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while updating the appointment.");
            }
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage());
        }
    }

    /**
     * This method handles the onAction for the cancel button that navigates back to the AppointmentsForm by calling the helper function navigateToAppointmentsForm.
     */
    @FXML
    private void handleCancelButtonAction() {
        navigateToAppointmentsForm();
    }

    /**
     * This helper method sets the selected appointment to the user selected appointment from the AppointmentsForm.
     * @param selectedAppointment this is the user selected appointment from the AppointmentForm
     */
    public void setSelectedAppointment(Appointments selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
        initializeFormForSelectedAppointment();
    }

    /**
     * This helper method loads the data from AppointmentsForm into the corresponding UI fields.
     */
    private void initializeFormForSelectedAppointment() {
        if (selectedAppointment != null) {
            appointmentIdTextField.setText(String.valueOf(selectedAppointment.getAppointmentId()));
            titleTextField.setText(selectedAppointment.getTitle());
            descriptionTextField.setText(selectedAppointment.getDescription());
            locationTextField.setText(selectedAppointment.getLocation());
            typeTextField.setText(selectedAppointment.getType());

            Contacts selectedContact = ContactsDAO.getContactById(selectedAppointment.getContactId());
            contactsComboBox.setValue(selectedContact);

            customerComboBox.setValue(selectedAppointment.getCustomerId());
            usersComboBox.setValue(selectedAppointment.getUserId());

            LocalDateTime startLocalDateTime = selectedAppointment.getStart();
            LocalDateTime endLocalDateTime = selectedAppointment.getEnd();

            startDatePicker.setValue(startLocalDateTime.toLocalDate());
            startTimeComboBox.setValue(startLocalDateTime.toLocalTime());
            endTimeComboBox.setValue(endLocalDateTime.toLocalTime());

            appointmentIdTextField.setDisable(true);
        }
    }

    /**
     * This helper method adds data to the start and end time combo boxes and changes the format from 24h to 12h for better user readability.
     * This method creates a list of times in increments of 15 minutes for the user to select.
     * This list is in 24h format that gets converted to 12h format with the StringConverter.
     */
    private void initializeTimeComboBoxes() {
        ObservableList<LocalTime> times = FXCollections.observableArrayList();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("h:mm a").toFormatter(Locale.ENGLISH);
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                times.add(LocalTime.of(hour, minute));
            }
        }

        startTimeComboBox.setItems(times);
        endTimeComboBox.setItems(times);

        StringConverter<LocalTime> timeStringConverter = new StringConverter<>() {
            @Override
            public String toString(LocalTime time) {
                if (time != null) {
                    return time.format(formatter);
                }
                return null;
            }

            @Override
            public LocalTime fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalTime.parse(string, formatter);
                }
                return null;
            }
        };

        startTimeComboBox.setConverter(timeStringConverter);
        endTimeComboBox.setConverter(timeStringConverter);
    }

    /**
     * This method allows the user to navigate back to the AppointmentForm after clicking Add or Cancel button.
     */
    private void navigateToAppointmentsForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsForm.fxml"));
            Stage stage = (Stage) updateAppointmentButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
