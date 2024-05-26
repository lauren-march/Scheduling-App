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
import javafx.stage.Stage;
import javafx.scene.control.*;
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

public class AddAppointmentFormController {

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
    private Button addAppointmentButton;
    @FXML
    private Button cancelAddAppointmentButton;

    @FXML
    public void initialize() {
        // Load contacts into ComboBox
        ObservableList<Contacts> contacts = ContactsDAO.getContactsList();
        contactsComboBox.setItems(contacts);

        ObservableList<Integer> customers = CustomerDAO.getCustomerIdList();
        customerComboBox.setItems(customers);

        ObservableList<Integer> users = UsersDAO.getUserIdList();
        usersComboBox.setItems(users);

        initializeFormForAdd();
        initializeTimeComboBoxes();
    }

    @FXML
    public void handleAddAppointmentButton() {
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

        // Validate business hours
        if (!ValidationUtil.businessHoursValidator.validate(TimeUtil.toET(startLocalDateTime), TimeUtil.toET(endLocalDateTime))) {
            showAlert("Error", "Appointment times must be within business hours (8:00 AM - 10:00 PM ET).");
            return;
        }

        // Convert to UTC for storage
        Timestamp startUTC = TimeUtil.localToTimestamp(startLocalDateTime);
        Timestamp endUTC = TimeUtil.localToTimestamp(endLocalDateTime);

        // Create new appointment
        LocalDateTime now = LocalDateTime.now();
        String currentUser = LoginFormController.currentUser;

        Appointments newAppointment = new Appointments(
                Integer.parseInt(appointmentIdTextField.getText()),
                title, description, location, type,
                startUTC.toLocalDateTime(), endUTC.toLocalDateTime(),
                now, currentUser, now, currentUser,
                customerId, userId, contact.getContactId()
        );

        try {
            AppointmentsDAO.addAppointment(newAppointment, startUTC, endUTC);
            navigateToAppointmentsForm();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while adding the appointment.");
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        navigateToAppointmentsForm();
    }

    private void initializeFormForAdd() {
        int nextAppointmentId = AppointmentsDAO.getNextAppointmentId();
        appointmentIdTextField.setText(String.valueOf(nextAppointmentId));
        appointmentIdTextField.setDisable(true);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AppointmentsForm.fxml"));
            Parent root = loader.load();

            AppointmentsFormController controller = loader.getController();
            controller.loadAppointmentData();

            Stage stage = (Stage) addAppointmentButton.getScene().getWindow();
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
