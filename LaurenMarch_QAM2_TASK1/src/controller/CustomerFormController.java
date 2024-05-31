package controller;

import helper.CustomerDAO;
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
import util.UserInterfaceUtil;

import java.io.IOException;

/**
 * This class handles the functionality and UI elements of the CustomerForm.
 */
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
    private TableColumn<Customer, Integer> divisionColumn;
    @FXML
    private TableColumn<Customer, String> countryColumn;
    @FXML
    private TableColumn<Customer, String> createDateColumn;
    @FXML
    private TableColumn<Customer, String> createdByColumn;
    @FXML
    private TableColumn<Customer, String> lastUpdateColumn;
    @FXML
    private TableColumn<Customer, String> lastUpdatedByColumn;

    @FXML
    private Button appointmentsFormButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button updateCustomerButton;

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
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));

        loadCustomerData();

        UserInterfaceUtil.adjuster.adjustColumns(customerTableView);
    }

    /**
     * This method handles the Add button.
     * Calls the helper function loadAddCustomerForm().
     */
    @FXML
    private void handleAddButton() {

        loadAddCustomerForm();
    }

    /**
     * This method handles the Update button.
     * Calls the helper function loadUpdateCustomerForm().
     */
    @FXML
    private void handleUpdateButton() {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            loadUpdateCustomerForm(selectedCustomer);
        } else {
            showAlert("Error", "No customer selected. Please select a customer to update.");
        }
    }

    /**
     * This method handles the Delete button.
     * Calls the helper method deleteButtonAction().
     */
    @FXML
    private void handleDeleteButton() {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            deleteButtonAction(selectedCustomer);
        } else {
            showAlert("Error", "No customer selected. Please select a customer to delete.");
        }
    }

    /**
     * This method handles the onAction for the Appointments button.
     * Calls helper function loadAppointmentForm()
     */
    @FXML
    private void handleAppointmentsButton(){
        loadAppointmentsForm();
    }

    /**
     * This helper method loads data from the database into the Customer tableview.
     */
    public void loadCustomerData() {
        ObservableList<Customer> customerList = CustomerDAO.getCustomerList();
        customerTableView.setItems(customerList);
    }

    /**
     * This helper method loads the AppointmentsForm when the Appointments button is clicked by the user.
     */
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

    /**
     * This helper method loads the AddCustomerForm when the Add button is clicked by the user.
     */
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

    /**
     * This helper method loads the UpdateCustomerForm when the Update button is clicked by the user.
     */
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

    /**
     * This helper method will delete a selected customer when the Delete button is clicked.
     * Calls hasAppointments method to check if customer has any appointments. If the customer does have appointments,
     * an error window will appear informing the user that the customer has appointments and cannot be deleted.
     * If the customer does not have appointments, the user will be prompted to make sure they want to delete
     * the selected customer with a pop-up warning window.
     * @param selectedCustomer selected customer from tableview.
     */
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

                if (!hasAppointments(selectedCustomer.getCustomerId())) {
                    CustomerDAO.deleteCustomer(selectedCustomer.getCustomerId());
                    loadCustomerData();

                } else {
                    showAlert("Error", "This customer has existing appointments and cannot be deleted.");
                }
            }
        });
    }

    /**
     * This helper method checks to see if a customer has any appointments in the database based on customerId.
     * @param customerId find appointments based on foreign key of customerId
     * @return this returns true or false whether or not there are appointments with that customerId
     */
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
