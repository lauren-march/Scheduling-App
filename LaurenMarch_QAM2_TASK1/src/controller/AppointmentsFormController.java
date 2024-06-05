package controller;

import helper.AppointmentsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import util.UserInterfaceUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * This class handles the functionality and UI elements of the AppointmentsForm.
 */
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
    private TableColumn<Appointments, LocalDateTime> appointmentsStartDateColumn;
    @FXML
    private TableColumn<Appointments, LocalDateTime> appointmentsEndDateColumn;
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
    private TabPane tabPane;
    @FXML
    private Tab tabAll;
    @FXML
    private Tab tabMonth;
    @FXML
    private Tab tabWeek;
    
    @FXML
    private Button customersButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button logoutButton;

    /**
     * This is the initialize method and is automatically called by JavaFx when this form loads.
     * It adds data to UI elements for AppointmentsForm.
     * The lambda function UserInterfaceUtil.adjuster.adjustColumns() was chosen because I have multiple tables in my program.
     * This keeps reusable code organized and easily accessible across multiple forms.
     * The allows for consistency for the tableview column sizing for a cleaner look.
     * This also allows me to only need to make changes from one location to adjust table column sizes instead of multiple files.
     */
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

        loadAppointmentData();

        UserInterfaceUtil.adjuster.adjustColumns(appointmentsTableView);
        UserInterfaceUtil.adjuster.adjustColumns(appointmentsTableViewMonth);
        UserInterfaceUtil.adjuster.adjustColumns(appointmentsTableViewWeek);

        // Add listeners to clear selections when tabs are selected
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == tabAll) {
                clearSelectionsExcept(appointmentsTableView);
            } else if (newTab == tabMonth) {
                clearSelectionsExcept(appointmentsTableViewMonth);
            } else if (newTab == tabWeek) {
                clearSelectionsExcept(appointmentsTableViewWeek);
            }
        });
    }

    /**
     * This method handles the onAction for the Add button.
     * Calls helper function loadAppointmentForm().
     */
    @FXML
    private void handleAddAppointmentButton() {
        loadAddAppointmentForm();
    }

    /**
     * This method handles the onAction for the Update button.
     * Forces user to first select an appointment from the appointmentsTableView before navigating to UpdateAppointmentForm.
     * If no appointment is selected it prompts an error dialogue window.
     */
    @FXML
    private void handleUpdateAppointmentButton() {
        Appointments selectedAppointment = null;

        selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            selectedAppointment = appointmentsTableViewMonth.getSelectionModel().getSelectedItem();
        }

        if (selectedAppointment == null) {
            selectedAppointment = appointmentsTableViewWeek.getSelectionModel().getSelectedItem();
        }

        if (selectedAppointment != null) {
            loadUpdateAppointmentForm(selectedAppointment);
        } else {
            showAlert("Error", "No appointment selected. Please select an appointment to update.");
        }
    }

    /**
     * This method handles the onAction for the Reports button.
     * Calls helper function loadReportsForm();
     */
    @FXML
    private void handleReportButton(){
        loadReportsForm();
    }

    /**
     * This method handles the onAction for the Customers button.
     * Calls helper function loadCustomersForm().
     */
    @FXML
    private void handleCustomersButton() {
        loadCustomersForm();
    }

    /**
     * This method handles the onAction for the Delete button.
     * If an appointment is not selected Delete button will not delete appointment and prompts an error dialogue window.
     * Calls helper function deleteButtonAction().
     */
    @FXML
    private void handleDeleteButton() {
        Appointments selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            deleteButtonAction(selectedAppointment);
        } else {
            showAlert("Error", "No appointment selected. Please select an appointment to delete.");
        }
    }

    /**
     * This method handles the onAction for the Logs button.
     * Calls helper function createLogFile().
     */
    @FXML
    private void handleLogsButton() {
        createLogFile();
    }

    /**
     * This method handles the onAction for the Logout button.
     * Calls helper function logout().
     */
    @FXML
    private void handleLogoutButton() {
        logout();
    }

    /**
     * This helper method loads the data from the database into the Appointments tableview.
     * The for loop checks the appointment data and correctly filters appointments for each view (all, monthly, weekly).
     */
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

            if ((appointmentDate.isEqual(firstDayOfMonth) || appointmentDate.isAfter(firstDayOfMonth)) &&
                    (appointmentDate.isEqual(lastDayOfMonth) || appointmentDate.isBefore(lastDayOfMonth))) {
                monthlyAppointments.add(appointment);
            }

            if ((appointmentDate.isEqual(firstDayOfWeek) || appointmentDate.isAfter(firstDayOfWeek)) &&
                    (appointmentDate.isEqual(lastDayOfWeek) || appointmentDate.isBefore(lastDayOfWeek))) {
                weeklyAppointments.add(appointment);
            }
        }

        appointmentsTableView.setItems(appointmentsList);
        appointmentsTableViewMonth.setItems(monthlyAppointments);
        appointmentsTableViewWeek.setItems(weeklyAppointments);
    }

    /**
     * This helper method loads the AddAppointmentForm when the Add button is clicked.
     */
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

    /**
     * This helper method clears the selected appointment when switching tabs in the table view.
     * @param tableViewToKeep
     */
    private void clearSelectionsExcept(TableView<Appointments> tableViewToKeep) {
        if (tableViewToKeep != appointmentsTableView) {
            appointmentsTableView.getSelectionModel().clearSelection();
        }
        if (tableViewToKeep != appointmentsTableViewMonth) {
            appointmentsTableViewMonth.getSelectionModel().clearSelection();
        }
        if (tableViewToKeep != appointmentsTableViewWeek) {
            appointmentsTableViewWeek.getSelectionModel().clearSelection();
        }
    }

    /**
     * This helper method loads the UpdateAppointmentForm when the Update button is clicked.
     * @param selectedAppointment user selected appointment from tableview.
     */
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

    /**
     * This helper method loads the CustomerForm when the Customers button is clicked.
     */
    private void loadCustomersForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
            Stage stage = (Stage) customersButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This helper method will delete a selected appointment when the Delete button is clicked.
     * The user will be prompted to make sure they want to delete the selected appointment with a pop-up warning window.
     * @param selectedAppointment user selected appointment from tableview.
     */
    private void deleteButtonAction(Appointments selectedAppointment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Appointment");
        alert.setContentText("Are you sure you want to delete this appointment?" + "\n" + "Appointment ID: " + selectedAppointment.getAppointmentId() +
                " " + "Type: " + selectedAppointment.getType());

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

        alert.showAndWait().ifPresent(type -> {
            if (type == buttonTypeYes) {
                AppointmentsDAO.deleteAppointment(selectedAppointment.getAppointmentId());
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Information");
                alert1.setHeaderText("Appointment Cancelled.");
                alert1.setContentText("Appointment ID: " + selectedAppointment.getAppointmentId() +
                " " + "Type: " + selectedAppointment.getType() + " has been cancelled.");

                alert1.showAndWait();

                loadAppointmentData();
            }
        });
    }

    /**
     * This helper method will load the Reports form when the Reports button is clicked.
     */
    private void loadReportsForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/ReportsForm.fxml"));
            Stage stage = (Stage) reportsButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This helper function will read a log file and display a log window in the UI when the Logs button is clicked.
     * If the log file is not found or is named something other than login_activity.txt an alert window will pop up.
     * Calls showLogs() which will display the read in log file that gets created when clicking Login button from LoginForm.
     */
    private void createLogFile () {
        try {
            StringBuilder logs = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader("login_activity.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logs.append(line).append("\n");
                }
            }

            showLogs(logs.toString());
        } catch (IOException e) {
            showAlert("Error", "Unable to read log file.");
            e.printStackTrace();
        }
    }

    /**
     * This helper file will pop up an information dialogue window.
     * @param logs read in log file saved to the directory from the Login button functionality.
     */
    private void showLogs(String logs) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Activity Logs");
        alert.setHeaderText("Login Activity");
        TextArea textArea = new TextArea(logs);
        textArea.setEditable(false);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    /**
     * This helper function navigates to the LoginForm when the user clicks the Logout button.
     */
    private void logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
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
