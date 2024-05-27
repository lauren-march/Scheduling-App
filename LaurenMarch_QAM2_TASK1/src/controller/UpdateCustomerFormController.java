package controller;

import exception.ValidationException;
import helper.CustomerDAO;
import helper.FirstLevelDivisionsDAO;
import helper.CountryDAO;
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
import java.time.LocalDateTime;

/**
 * This class handles the functionality and UI elements of the UpdateCustomerForm.
 */
public class UpdateCustomerFormController {

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
    private Button updateCustomerButton;
    @FXML
    private Button cancelButton;

    private Customer selectedCustomer;

    /**
     * This is the initialize method and is automatically called by JavaFx when this form loads.
     * It adds data to UI elements for updateCustomerForm.
     */
    @FXML
    public void initialize() {
        // Load countries into ComboBox
        ObservableList<Countries> countries = CountryDAO.getAllCountries();
        countryComboBox.setItems(countries);

        // Add listener to countryComboBox to load relevant divisions
        countryComboBox.setOnAction(event -> loadDivisions());

        }

    /**
     * This method handles the functionality of the Update button on the UpdateCustomerForm.
     * First it gets the values entered in the corresponding fields and stores them to corresponding variables.
     * Textfields are checked with ValidationUtil methods to make sure they are not blank.
     * Then it runs a check to make sure that there isn't null fields for comboboxes.
     * Then it runs checks for validation lambdas.
     * I chose to use lambdas for ValidateUtil.validateTime, validateOverlappingAppointments, and businessHoursValidator
     * since these can be used in various places in the application (reusable) and it saved about 11-12 lines of code for each
     * which makes my code more readable and concise.
     * Lastly it creates a new customer object with the updates that gets updated to the customer table in the
     * database with the CustomerDAO.updateCustomer() method that uses SQL UPDATE statement.
     */
    @FXML
    private void handleUpdateCustomerButton() {
        try {
            String name = ValidationUtil.validateName(customerNameTextField.getText());
            String address = ValidationUtil.validateAddress(addressTextField.getText(),
                    countryComboBox.getSelectionModel().getSelectedItem().getCountry());
            String postalCode = ValidationUtil.validatePostalCode(postalCodeTextField.getText(),
                    countryComboBox.getSelectionModel().getSelectedItem().getCountry());
            String phoneNumber = ValidationUtil.validatePhoneNumber(phoneNumberTextField.getText(),
                    countryComboBox.getSelectionModel().getSelectedItem().getCountry());
            FirstLevelDivisions selectedDivision = firstLevelDivisionComboBox.getSelectionModel().getSelectedItem();

            if (selectedDivision == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            LocalDateTime now = LocalDateTime.now();
            selectedCustomer.setName(name);
            selectedCustomer.setAddress(address);
            selectedCustomer.setPostalCode(postalCode);
            selectedCustomer.setPhoneNumber(phoneNumber);
            selectedCustomer.setDivisionId(selectedDivision.getDivisionId());
            selectedCustomer.setLastUpdate(now);


            CustomerDAO.updateCustomer(selectedCustomer, now);
            navigateToCustomerForm();

        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage());
        }
    }

    /**
     * This method handles the onAction for the cancel button that navigates back to the AppointmentsForm by calling the helper function navigateToAppointmentsForm.
     */
    @FXML
    private void handleCancelButtonAction() {
        navigateToCustomerForm();
    }

    /**
     * This helper method sets the selected customer to the user selected customer from the CustomerForm.
     * @param selectedCustomer this is the user selected customer from the CustomerForm
     */
    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        initializeFormForSelectedCustomer();
    }

    /**
     * This helper method loads the data from CustomerForm into the corresponding UI fields.
     * This information is loaded from the selectedCustomer.
     */
    private void initializeFormForSelectedCustomer() {
        if (selectedCustomer != null) {
            customerIdTextField.setText(String.valueOf(selectedCustomer.getCustomerId()));
            customerNameTextField.setText(selectedCustomer.getName());
            addressTextField.setText(selectedCustomer.getAddress());
            postalCodeTextField.setText(selectedCustomer.getPostalCode());
            phoneNumberTextField.setText(selectedCustomer.getPhoneNumber());

            Countries selectedCountry = CountryDAO.getCountryById(selectedCustomer.getCountryId());
            countryComboBox.setValue(selectedCountry);
            loadDivisions();

            FirstLevelDivisions selectedDivision = FirstLevelDivisionsDAO.getDivisionById(selectedCustomer.getDivisionId());
            firstLevelDivisionComboBox.setValue(selectedDivision);

            customerIdTextField.setDisable(true);
        }
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
            Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
            Stage stage = (Stage) updateCustomerButton.getScene().getWindow();
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
