package controller;

import helper.AppointmentsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class AppointmentsFormController {
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
    private TableColumn<Appointments, LocalDateTime> appointmentsStartDateColumnMonth;
    @FXML
    private TableColumn<Appointments, LocalDateTime> appointmentsEndDateColumnMonth;
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
    private TableColumn<Appointments, LocalDateTime> appointmentsStartDateColumnWeek;
    @FXML
    private TableColumn<Appointments, LocalDateTime> appointmentsEndDateColumnWeek;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsCustomerIDColumnWeek;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsUserIdColumnWeek;

    @FXML
    private Button customerFormButton;

    @FXML
    private void handleLoadCustomerFormButton() {
        loadCustomersForm();
    }

    @FXML
    public void initialize() {
        // Initialize columns for monthly view
        appointmentsIdColumnMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentsTitleColumnMonth.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentsDescriptionColumnMonth.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentsLocationColumnMonth.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentsContactColumnMonth.setCellValueFactory(new PropertyValueFactory<>("contactId"));
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
        appointmentsContactColumnWeek.setCellValueFactory(new PropertyValueFactory<>("contactId"));
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

    private void loadAppointmentData() {
        ObservableList<Appointments> appointmentsList = AppointmentsDAO.getAppointmentsList();
        ObservableList<Appointments> monthlyAppointments = FXCollections.observableArrayList();
        ObservableList<Appointments> weeklyAppointments = FXCollections.observableArrayList();

        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(2020, now.getMonth(), 1); // For testing
        LocalDate lastDayOfMonth = LocalDate.of(2020, now.getMonth(), now.lengthOfMonth()); // For testing
        LocalDate firstDayOfWeek = LocalDate.of(2020, now.getMonth(), 1).with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1); // For testing
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


}
