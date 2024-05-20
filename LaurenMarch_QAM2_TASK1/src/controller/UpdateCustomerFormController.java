package controller;

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

import java.io.IOException;
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

    @FXML
    private void handleUpdateButtonAction() {
        String name = customerNameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        FirstLevelDivisions selectedDivision = firstLevelDivisionComboBox.getSelectionModel().getSelectedItem();

        if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phoneNumber.isEmpty() || selectedDivision == null) {
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

        // Navigate back to CustomerForm
        navigateToCustomerForm();
    }

    @FXML
    private void handleCancelButtonAction() {
        navigateToCustomerForm();
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
