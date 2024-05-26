package controller;

import helper.CustomerDAO;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import model.Appointments;
import helper.AppointmentsDAO;

import java.io.IOException;

public class CustomerFormController {

    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> createDateColumn;
    @FXML
    private TableColumn<Customer, String> createdByColumn;
    @FXML
    private TableColumn<Customer, String> lastUpdateColumn;
    @FXML
    private TableColumn<Customer, String> lastUpdatedByColumn;
    @FXML
    private TableColumn<Customer, Integer> divisionIdColumn;
    @FXML
    private Button appointmentsFormButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button updateCustomerButton;

    @FXML
    public void initialize() {
        // Initialize customer table columns
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        // Load data
        loadCustomerData();

        // Make columns adjustable
        makeColumnsAdjustable(customerTableView);
    }

    @FXML
    private void handleLoadAppointmentsFormButton(){
        loadAppointmentsForm();
    }

    @FXML
    private void handleLoadAddCustomerForm() {

        loadAddCustomerForm();
    }

    @FXML
    private void handleLoadUpdateCustomerForm() {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            loadUpdateCustomerForm(selectedCustomer);
        } else {
            showAlert("Error", "No customer selected. Please select a customer to update.");
        }
    }

    @FXML
    private void handleDeleteButtonAction() {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            deleteButtonAction(selectedCustomer);
        } else {
            showAlert("Error", "No customer selected. Please select a customer to delete.");
        }
    }

    public void loadCustomerData() {
        ObservableList<Customer> customerList = CustomerDAO.getCustomerList();
        customerTableView.setItems(customerList);
    }

    private void makeColumnsAdjustable(TableView<?> table) {
        table.getColumns().forEach(column -> {
            column.setPrefWidth(column.getWidth());
            column.setMinWidth(130);  // Set a minimum width for better appearance
        });

        table.getItems().addListener((ListChangeListener<Object>) change -> {
            table.getColumns().forEach(column -> {
                column.setPrefWidth(column.getWidth());
                column.setMinWidth(130);  // Set a minimum width for better appearance
            });
        });
    }

    private void loadAppointmentsForm(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsForm.fxml"));
            Stage stage = (Stage) appointmentsFormButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAddCustomerForm () {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AddCustomerForm.fxml"));
            Stage stage = (Stage) addCustomerButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUpdateCustomerForm(Customer selectedCustomer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateCustomerForm.fxml"));
            Parent root = loader.load();

            UpdateCustomerFormController controller = loader.getController();
            controller.setSelectedCustomer(selectedCustomer);

            Stage stage = (Stage) updateCustomerButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteButtonAction(Customer selectedCustomer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Customer");
        alert.setContentText("Are you sure you want to delete this customer?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

        alert.showAndWait().ifPresent(type -> {
            if (type == buttonTypeYes) {
                // Check if the customer has any appointments
                if (!hasAppointments(selectedCustomer.getCustomerId())) {
                    // Delete the customer from the database
                    CustomerDAO.deleteCustomer(selectedCustomer.getCustomerId());
                    // Refresh the table view
                    loadCustomerData();
                } else {
                    // Show error message
                    showAlert("Error", "This customer has existing appointments and cannot be deleted.");
                }
            }
        });
    }

    // Helper method to check if a customer has any appointments
    private boolean hasAppointments(int customerId) {
        ObservableList<Appointments> appointments = AppointmentsDAO.getAppointmentsByCustomerId(customerId);
        return !appointments.isEmpty();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
