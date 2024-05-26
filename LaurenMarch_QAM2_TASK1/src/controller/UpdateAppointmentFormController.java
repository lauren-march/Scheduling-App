package controller;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

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

    @FXML
    public void initialize() {
        // Load contacts into ComboBox
        ObservableList<Contacts> contacts = ContactsDAO.getContactsList();
        contactsComboBox.setItems(contacts);

        ObservableList<Integer> customers = CustomerDAO.getCustomerIdList();
        customerComboBox.setItems(customers);

        ObservableList<Integer> users = UsersDAO.getUserIdList();
        usersComboBox.setItems(users);

        initializeTimeComboBoxes();
    }

    public void setSelectedAppointment(Appointments selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
        initializeFormForUpdate();
    }

    @FXML
    public void handleUpdateAppointmentButton() {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        Contacts contact = contactsComboBox.getValue();
        Integer customerId = customerComboBox.getValue();
        Integer userId = usersComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = startTimeComboBox.getValue();
        LocalTime endTime = endTimeComboBox.getValue();

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() ||
                contact == null || customerId == null || userId == null || startDate == null ||
                startTime == null || endTime == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Combine date and time
        LocalDateTime startLocalDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endLocalDateTime = LocalDateTime.of(startDate, endTime);

        if (!ValidationUtil.validateTimes.validate(startLocalDateTime, endLocalDateTime)) {
            return;
        }

        if (!ValidationUtil.validateOverlappingAppointments.validate(customerId, startLocalDateTime, endLocalDateTime)) {
            return;
        }

        // Validate business hours using the BusinessHoursValidator
        if (!ValidationUtil.businessHoursValidator.validate(TimeUtil.toET(startLocalDateTime), TimeUtil.toET(endLocalDateTime))) {
            showAlert("Error", "Appointment times must be within business hours (8:00 AM - 10:00 PM ET).");
            return;
        }

        // Convert to UTC for storage
        Timestamp startUTC = TimeUtil.localToTimestamp(startLocalDateTime);
        Timestamp endUTC = TimeUtil.localToTimestamp(endLocalDateTime);

        // Update appointment
        LocalDateTime now = LocalDateTime.now();
        String currentUser = LoginFormController.currentUser;

        Appointments updatedAppointment = new Appointments(
                Integer.parseInt(appointmentIdTextField.getText()),
                title, description, location, type,
                startLocalDateTime, endLocalDateTime,
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
    }

    @FXML
    private void handleCancelButtonAction() {
        navigateToAppointmentsForm();
    }

    private void initializeFormForUpdate() {
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
        }
    }

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
