package controller;

import helper.AppointmentsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import util.TimeUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class AppointmentsFormController {

    @FXML
    private TableView<Appointments> appointmentsTableView;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsIdColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsTitleColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsDescriptionColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsLocationColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsContactColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsTypeColumn;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> appointmentsStartDateColumn;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> appointmentsEndDateColumn;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsCustomerIDColumn;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsUserIdColumn;

    @FXML
    private TableView<Appointments> appointmentsTableViewMonth;
    @FXML
    private TableView<Appointments> appointmentsTableViewWeek;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsIdColumnMonth;
    @FXML
    private TableColumn<Appointments, String> appointmentsTitleColumnMonth;
    @FXML
    private TableColumn<Appointments, String> appointmentsDescriptionColumnMonth;
    @FXML
    private TableColumn<Appointments, String> appointmentsLocationColumnMonth;
    @FXML
    private TableColumn<Appointments, String> appointmentsContactColumnMonth;
    @FXML
    private TableColumn<Appointments, String> appointmentsTypeColumnMonth;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> appointmentsStartDateColumnMonth;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> appointmentsEndDateColumnMonth;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsCustomerIDColumnMonth;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsUserIdColumnMonth;

    @FXML
    private TableColumn<Appointments, Integer> appointmentsIdColumnWeek;
    @FXML
    private TableColumn<Appointments, String> appointmentsTitleColumnWeek;
    @FXML
    private TableColumn<Appointments, String> appointmentsDescriptionColumnWeek;
    @FXML
    private TableColumn<Appointments, String> appointmentsLocationColumnWeek;
    @FXML
    private TableColumn<Appointments, String> appointmentsContactColumnWeek;
    @FXML
    private TableColumn<Appointments, String> appointmentsTypeColumnWeek;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> appointmentsStartDateColumnWeek;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> appointmentsEndDateColumnWeek;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsCustomerIDColumnWeek;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsUserIdColumnWeek;

    @FXML
    private Button customerFormButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;

    @FXML
    public void initialize() {
        // Initialize columns for the all view
        appointmentsIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentsTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentsDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentsLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentsContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentsStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentsEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentsCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentsUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Initialize columns for monthly view
        appointmentsIdColumnMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentsTitleColumnMonth.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentsDescriptionColumnMonth.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentsLocationColumnMonth.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentsContactColumnMonth.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentsTypeColumnMonth.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentsStartDateColumnMonth.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentsEndDateColumnMonth.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentsCustomerIDColumnMonth.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentsUserIdColumnMonth.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Initialize columns for weekly view
        appointmentsIdColumnWeek.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentsTitleColumnWeek.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentsDescriptionColumnWeek.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentsLocationColumnWeek.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentsContactColumnWeek.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentsTypeColumnWeek.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentsStartDateColumnWeek.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentsEndDateColumnWeek.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentsCustomerIDColumnWeek.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentsUserIdColumnWeek.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Load data
        loadAppointmentData();
        makeColumnsAdjustable(appointmentsTableViewMonth);
        makeColumnsAdjustable(appointmentsTableViewWeek);
    }

    @FXML
    private void handleLoadAddAppointmentForm() {
        loadAddAppointmentForm();
    }

    private void loadAddAppointmentForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointmentForm.fxml"));
            Stage stage = (Stage) addAppointmentButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadUpdateAppointmentForm() {
        Appointments selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            loadUpdateAppointmentForm(selectedAppointment);
        } else {
            showAlert("Error", "No appointment selected. Please select an appointment to update.");
        }
    }

    private void loadUpdateAppointmentForm(Appointments selectedAppointment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateAppointmentForm.fxml"));
            Parent root = loader.load();

            UpdateAppointmentFormController controller = loader.getController();
            controller.setSelectedAppointment(selectedAppointment);

            Stage stage = (Stage) updateAppointmentButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadCustomerFormButton() {
        loadCustomersForm();
    }

    @FXML
    private void handleDeleteButtonAction() {
        Appointments selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            deleteButtonAction(selectedAppointment);
        } else {
            showAlert("Error", "No appointment selected. Please select an appointment to delete.");
        }
    }

    public void loadAppointmentData() {
        ObservableList<Appointments> appointmentsList = AppointmentsDAO.getAppointmentsList();
        ObservableList<Appointments> monthlyAppointments = FXCollections.observableArrayList();
        ObservableList<Appointments> weeklyAppointments = FXCollections.observableArrayList();

        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        LocalDate firstDayOfWeek = now.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(6);

        for (Appointments appointment : appointmentsList) {
            LocalDate appointmentDate = appointment.getStart().toLocalDate();

            // Check for monthly view
            if ((appointmentDate.isEqual(firstDayOfMonth) || appointmentDate.isAfter(firstDayOfMonth)) &&
                    (appointmentDate.isEqual(lastDayOfMonth) || appointmentDate.isBefore(lastDayOfMonth))) {
                monthlyAppointments.add(appointment);
            }

            // Check for weekly view
            if ((appointmentDate.isEqual(firstDayOfWeek) || appointmentDate.isAfter(firstDayOfWeek)) &&
                    (appointmentDate.isEqual(lastDayOfWeek) || appointmentDate.isBefore(lastDayOfWeek))) {
                weeklyAppointments.add(appointment);
            }
        }

        appointmentsTableView.setItems(appointmentsList);
        appointmentsTableViewMonth.setItems(monthlyAppointments);
        appointmentsTableViewWeek.setItems(weeklyAppointments);
    }

    private void makeColumnsAdjustable(TableView<?> table) {
        table.getColumns().forEach(column -> {
            column.setPrefWidth(column.getWidth());
            column.setMinWidth(110);  // Set a minimum width for better appearance
        });

        table.getItems().addListener((ListChangeListener<Object>) change -> {
            table.getColumns().forEach(column -> {
                column.setPrefWidth(column.getWidth());
                column.setMinWidth(50);  // Set a minimum width for better appearance
            });
        });
    }

    private void loadCustomersForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
            Stage stage = (Stage) customerFormButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteButtonAction(Appointments selectedAppointment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Appointment");
        alert.setContentText("Are you sure you want to delete this appointment?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

        alert.showAndWait().ifPresent(type -> {
            if (type == buttonTypeYes) {
                // Delete the appointment from the database
                AppointmentsDAO.deleteAppointment(selectedAppointment.getAppointmentId());
                // Refresh the table view
                loadAppointmentData();
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
