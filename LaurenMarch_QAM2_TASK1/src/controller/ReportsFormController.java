package controller;

import helper.ContactsDAO;
import helper.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import helper.AppointmentsDAO;
import model.Appointments;
import model.Contacts;

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
        makeColumnsAdjustable(contactAppointmentsTableView);
        initializeMonthComboBox();
        initializeContactScheduleComboBox();
        updatePieChart(currentMonth);
        updateCustomerByCountryChart();
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
    }

    private void loadAppointmentData() {
        ObservableList<Appointments> appointmentsList = AppointmentsDAO.getAppointmentsList();
        contactAppointmentsTableView.setItems(appointmentsList);

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

    private void updateTableView(Contacts selectedContact) {
        ObservableList<Appointments> filteredAppointments = AppointmentsDAO.getAppointmentsByContactId(selectedContact.getContactId());
        contactAppointmentsTableView.setItems(filteredAppointments);
    }

}
