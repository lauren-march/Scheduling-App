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
    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        // Load countries into ComboBox
        ObservableList<Countries> countries = CountryDAO.getAllCountries();
        countryComboBox.setItems(countries);

        // Add listener to countryComboBox to load relevant divisions
        countryComboBox.setOnAction(event -> loadDivisions());

        // Initialize form for adding a new customer
        initializeFormForAdd();
    }

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

    @FXML
    private void handleCancelButtonAction() {
        navigateToCustomerForm();
    }

    private void navigateToCustomerForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerForm.fxml"));
            Parent root = loader.load();

            CustomerFormController controller = loader.getController();
            controller.loadCustomerData();  // Reload data after navigating back

            Stage stage = (Stage) addCustomerButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeFormForAdd() {
        int nextCustomerId = CustomerDAO.getNextCustomerId();
        customerIdTextField.setText(String.valueOf(nextCustomerId));
        customerIdTextField.setDisable(true);
    }

    private void loadDivisions() {
        Countries selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            ObservableList<FirstLevelDivisions> divisions = FirstLevelDivisionsDAO.getDivisionsByCountryId(selectedCountry.getCountryId());
            firstLevelDivisionComboBox.setItems(divisions);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
