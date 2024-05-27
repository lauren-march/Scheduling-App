package controller;

import helper.ContactsDAO;
import helper.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import helper.AppointmentsDAO;
import model.Appointments;
import model.Contacts;
import util.UserInterfaceUtil;

public class ReportsFormController {

    @FXML
    private TableView<Appointments> contactAppointmentsTableView;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsIdColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsTitleColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsDescriptionColumn;
    @FXML
    private TableColumn<Appointments, String> appointmentsTypeColumn;
    @FXML
    private TableColumn<Appointments, LocalDateTime> appointmentsStartDateColumn;
    @FXML
    private TableColumn<Appointments, LocalDateTime> appointmentsEndDateColumn;
    @FXML
    private TableColumn<Appointments, Integer> appointmentsCustomerIDColumn;

    @FXML
    private PieChart numberCustomerByMonthAndType;
    @FXML
    private PieChart numberCustomerByCountry;
    @FXML
    private ComboBox<Month> monthComboBox;
    @FXML
    private ComboBox<Contacts> contactScheduleComboBox;

    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        // Initialize columns
        appointmentsIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentsTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentsDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentsStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentsEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentsCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        // Set the current month as default
        Month currentMonth = LocalDate.now().getMonth();
        monthComboBox.setValue(currentMonth);

        // Add listener to ComboBox
        monthComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updatePieChart(newValue);
            }
        });

        // Add listener to contactScheduleComboBox
        contactScheduleComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateTableView(newValue);
            }
        });

        loadAppointmentData();
        UserInterfaceUtil.adjuster.adjustColumns(contactAppointmentsTableView);
        initializeMonthComboBox();
        initializeContactScheduleComboBox();
        updatePieChart(currentMonth);
        updateCustomerByCountryChart();
    }

    @FXML
    private void handleExitButton() {
        exitButtonAction();
    }

    private void updatePieChart(Month selectedMonth) {
        int monthValue = selectedMonth.getValue();
        ObservableList<PieChart.Data> pieChartData = AppointmentsDAO.getAppointmentsByTypeForMonth(monthValue);

        if (pieChartData.isEmpty()) {
            numberCustomerByMonthAndType.setTitle("No appointments for " + selectedMonth.name());
            numberCustomerByMonthAndType.setData(FXCollections.observableArrayList(new PieChart.Data("No Appointments", 1)));
        } else {
            // Update pie chart title with total number of appointments
            int totalAppointments = pieChartData.stream().mapToInt(data -> (int) data.getPieValue()).sum();
            numberCustomerByMonthAndType.setTitle("Total Appointments: " + totalAppointments);

            // Add number of appointments next to type
            pieChartData.forEach(data -> data.setName(data.getName() + " (" + (int) data.getPieValue() + ")"));

            numberCustomerByMonthAndType.setData(pieChartData);
        }
    }

    private void updateCustomerByCountryChart() {
        ObservableList<PieChart.Data> pieChartData = CustomerDAO.getCustomerCountByCountry();
        if (pieChartData.isEmpty()) {
            numberCustomerByCountry.setTitle("No customers by country data available");
            numberCustomerByCountry.setData(FXCollections.observableArrayList(new PieChart.Data("No Data", 1)));
        } else {
            numberCustomerByCountry.setTitle("Number of Customers by Country");

            numberCustomerByCountry.setData(pieChartData);
        }
    }

    private void initializeMonthComboBox() {
        monthComboBox.setItems(FXCollections.observableArrayList(Month.values()));
        monthComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Month month) {
                return month != null ? month.name() : "";
            }

            @Override
            public Month fromString(String string) {
                return Month.valueOf(string);
            }
        });
    }

    private void initializeContactScheduleComboBox() {
        ObservableList<Contacts> contacts = ContactsDAO.getContactsList();
        contactScheduleComboBox.setItems(contacts);

        if (!contacts.isEmpty()) {
            Contacts firstContact = contacts.get(0);
            contactScheduleComboBox.setValue(firstContact);
            updateTableView(firstContact);
        }
    }

    private void loadAppointmentData() {
        ObservableList<Appointments> appointmentsList = AppointmentsDAO.getAppointmentsList();
        contactAppointmentsTableView.setItems(appointmentsList);
    }

    private void updateTableView(Contacts selectedContact) {
        ObservableList<Appointments> filteredAppointments = AppointmentsDAO.getAppointmentsByContactId(selectedContact.getContactId());
        contactAppointmentsTableView.setItems(filteredAppointments);
    }

    private void exitButtonAction(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsForm.fxml"));
            Stage stage = (Stage) exitButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
