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
import model.Appointments;
import model.Contacts;
import model.Customer;
import model.Users;
import util.TimeUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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

            // Validate times
            if (!validateTimes(startLocalDateTime, endLocalDateTime)) {
                return;
            }

            // Validate business hours
            if (!validateBusinessHours(startLocalDateTime, endLocalDateTime)) {
                return;
            }

            // Validate overlapping appointments
            if (!validateOverlappingAppointments(customerId, startLocalDateTime, endLocalDateTime)) {
                return;
            }

            // Convert to UTC for storage
            ZonedDateTime startUTC = TimeUtil.toUTC(startLocalDateTime);
            ZonedDateTime endUTC = TimeUtil.toUTC(endLocalDateTime);

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
                AppointmentsDAO.addAppointment(newAppointment, now, now);

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while adding the appointment.");
            }
        navigateToAppointmentsForm();
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
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                times.add(LocalTime.of(hour, minute));
            }
        }

        startTimeComboBox.setItems(times);
        endTimeComboBox.setItems(times);
    }

    private boolean validateTimes(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && !end.isAfter(start)) {
            showAlert("Error", "End time must be after start time.");
            return false;
        }
        return true;
    }

    private boolean validateBusinessHours(LocalDateTime start, LocalDateTime end) {
        ZonedDateTime startET = TimeUtil.toET(start);
        ZonedDateTime endET = TimeUtil.toET(end);

        LocalTime businessStart = LocalTime.of(8, 0);
        LocalTime businessEnd = LocalTime.of(22, 0);

        if (startET.toLocalTime().isBefore(businessStart) || endET.toLocalTime().isAfter(businessEnd)) {
            showAlert("Error", "Appointment times must be within business hours (8:00 AM - 10:00 PM ET).");
            return false;
        }
        return true;
    }

    private boolean validateOverlappingAppointments(int customerId, LocalDateTime start, LocalDateTime end) {
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
