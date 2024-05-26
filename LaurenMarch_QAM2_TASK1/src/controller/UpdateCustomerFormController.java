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

    @FXML
    public void initialize() {
        // Load countries into ComboBox
        ObservableList<Countries> countries = CountryDAO.getAllCountries();
        countryComboBox.setItems(countries);

        // Add listener to countryComboBox to load relevant divisions
        countryComboBox.setOnAction(event -> loadDivisions());

        // Initialize form for updating an existing customer if a customer is selected
        if (selectedCustomer != null) {
            initializeFormForUpdate();
        }
    }

    @FXML
    private void handleUpdateButtonAction() {
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

    @FXML
    private void handleCancelButtonAction() {
        navigateToCustomerForm();
    }

    private void loadDivisions() {
        Countries selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            ObservableList<FirstLevelDivisions> divisions = FirstLevelDivisionsDAO.getDivisionsByCountryId(selectedCountry.getCountryId());
            firstLevelDivisionComboBox.setItems(divisions);
        }
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        if (this.selectedCustomer != null) {
            initializeFormForUpdate();
        }
    }

    private void initializeFormForUpdate() {
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
