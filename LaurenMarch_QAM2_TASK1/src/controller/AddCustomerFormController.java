package controller;

import exception.ValidationException;
import helper.CustomerDAO;
import helper.FirstLevelDivisionsDAO;
import helper.CountryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import model.FirstLevelDivisions;
import model.Countries;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This class handles the functionality and UI elements of the AddCustomerForm.
 */
public class AddCustomerFormController {

    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private ComboBox<Countries> countryComboBox;
    @FXML
    private ComboBox<FirstLevelDivisions> firstLevelDivisionComboBox;
    @FXML
    private Button addCustomerButton;

    /**
     * This is the initialize method and is automatically called by JavaFx when this form loads.
     * It adds data to UI elements for AddCustomerForm.
     */
    @FXML
    public void initialize() {
        // Load countries into ComboBox
        ObservableList<Countries> countries = CountryDAO.getAllCountries();
        countryComboBox.setItems(countries);

        // Add listener to countryComboBox to load relevant divisions
        countryComboBox.setOnAction(event -> loadDivisions());

        // Initialize form for adding a new customer
        initializeFormForCustomerId();
    }

    /**
     * This method handles the functionality of the Add button on the AddCustomerForm.
     * First it gets the values entered in the corresponding fields and stores them to corresponding variables.
     * Textfields are checked with ValidationUtil methods to make sure they are not blank.
     * Then it runs a check to make sure that there isn't null fields for comboboxes.
     * Lastly it converts the localtime to UTC and creates a new customer object
     * that gets saved to the customer table in the database with the CustomerDAO.addCustomer() method.
     */
    @FXML
    private void handleAddButtonAction() {
        try {
            String name = ValidationUtil.validateName(customerNameTextField.getText());
            String address = ValidationUtil.validateAddress(addressTextField.getText());
            String postalCode = ValidationUtil.validatePostalCode(postalCodeTextField.getText(),
                    countryComboBox.getSelectionModel().getSelectedItem().getCountry());
            String phoneNumber = ValidationUtil.validatePhoneNumber(phoneNumberTextField.getText());
            FirstLevelDivisions selectedDivision = firstLevelDivisionComboBox.getSelectionModel().getSelectedItem();

            if (selectedDivision == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            LocalDateTime now = LocalDateTime.now();
            String currentUser = LoginFormController.currentUser;

            Customer newCustomer = new Customer(
                    Integer.parseInt(customerIdTextField.getText()),
                    name, address, postalCode, phoneNumber,
                    currentUser, currentUser,
                    selectedDivision.getDivisionId(),
                    selectedDivision.getDivision(),
                    selectedDivision.getCountryId(),
                    countryComboBox.getSelectionModel().getSelectedItem().getCountry(),
                    now, now
            );

            try {
                CustomerDAO.addCustomer(newCustomer, now, now);
                navigateToCustomerForm();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage());
        }
    }

    /**
     * This method handles the onAction for the cancel button that navigates back to the CustomerForm by calling the helper function navigateToCustomerForm.
     */
    @FXML
    private void handleCancelButtonAction() {

        navigateToCustomerForm();
    }

    /**
     * This helper method sets the customerIdTextField to an increment of the existing customerIds +1 and disables the textfield from being editable.
     */
    private void initializeFormForCustomerId() {
        int nextCustomerId = CustomerDAO.getNextCustomerId();
        customerIdTextField.setText(String.valueOf(nextCustomerId));
        customerIdTextField.setDisable(true);
    }

    /**
     * This helper method allows the divisions to only load after a country is selected from its combobox.
     * First selectedCountry variable is assigned to the user selected item from the countryComboBox.
     * If the selectedCountry has been selected (!= null) then the list of divisions is created from a FirstLevelDivisionDAO method.
     * The firstLevelDivisionComboBox is then populated with that list the corresponds to the selected country.
     */
    private void loadDivisions() {
        Countries selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            ObservableList<FirstLevelDivisions> divisions = FirstLevelDivisionsDAO.getDivisionsByCountryId(selectedCountry.getCountryId());
            firstLevelDivisionComboBox.setItems(divisions);
        }
    }

    /**
     * This method allows the user to navigate back to the CustomerForm after clicking Add or Cancel button.
     */
    private void navigateToCustomerForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerForm.fxml"));
            Parent root = loader.load();

            CustomerFormController controller = loader.getController();
            controller.loadCustomerData();

            Stage stage = (Stage) addCustomerButton.getScene().getWindow();
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
