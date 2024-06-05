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
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import model.Appointments;
import model.Contacts;
import util.ValidationUtil;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

/**
 * This class handles the functionality and UI elements of the AddAppointmentForm.
 */
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

    @FXML private Button addAppointmentButton;

    /**
     * This is the initialize method and is automatically called by JavaFx when this form loads.
     * It adds data to UI elements for AddAppointmentForm.
     */
    @FXML
    public void initialize() {

        ObservableList<Contacts> contacts = ContactsDAO.getContactsList();
        contactsComboBox.setItems(contacts);

        ObservableList<Integer> customers = CustomerDAO.getCustomerIdList();
        customerComboBox.setItems(customers);

        ObservableList<Integer> users = UsersDAO.getUserIdList();
        usersComboBox.setItems(users);

        initializeFormForAppointmentId();
        initializeTimeComboBoxes();
    }

    /**
     * This method handles the functionality of the Add button on the AddAppointmentForm.
     * First it gets the values entered in the corresponding fields and stores them to corresponding variables.
     * Textfields are checked with ValidationUtil methods to make sure they are not blank.
     * Then it runs a check to make sure that there isn't null fields for comboboxes.
     * Then it runs checks for validation lambdas.
     * I chose to use lambdas for ValidateUtil.validateTime, validateOverlappingAppointments, and businessHoursValidator
     * since these can be used in various places in the application (reusable) and it saved about 11-12 lines of code for each
     * which makes my code more readable and concise.
     * Lastly the appointment gets saved to the appointments table in the database with the AppointmentsDAO.addAppointment() method.
     */
    @FXML
    public void handleAddAppointmentButton() {
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

            System.out.println(startLocalDateTime);
            System.out.println(endLocalDateTime);

            System.out.println(localStartZonedDateTime);
            System.out.println(localEndZonedDateTime);


            if (!ValidationUtil.validateTimes.validate(startLocalDateTime, endLocalDateTime)) {
                return;
            }

            if (!ValidationUtil.validateOverlappingAppointments.validate(customerId, startLocalDateTime, endLocalDateTime)) {
                return;
            }

            if (!ValidationUtil.businessHoursValidator.validate(localStartZonedDateTime, localEndZonedDateTime)) {
                showAlert("Error", "Appointment times must be within business hours (8:00 AM - 10:00 PM ET).");
                return;
            }

            LocalDateTime now = LocalDateTime.now();
            String currentUser = LoginFormController.currentUser;

            Appointments newAppointment = new Appointments(
                    Integer.parseInt(appointmentIdTextField.getText()),
                    title, description, location, type,
                    startLocalDateTime, endLocalDateTime,
                    now, currentUser, now, currentUser,
                    customerId, userId, contact.getContactId()
            );

            try {
                AppointmentsDAO.addAppointment(newAppointment, startLocalDateTime, endLocalDateTime);
                navigateToAppointmentsForm();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while adding the appointment.");
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
     * This helper method sets the appointmentIdTextField to an increment of the existing appointmentIds +1 and disables the textfield from being editable.
     */
    private void initializeFormForAppointmentId() {
        int nextAppointmentId = AppointmentsDAO.getNextAppointmentId();
        appointmentIdTextField.setText(String.valueOf(nextAppointmentId));
        appointmentIdTextField.setDisable(true);
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
