package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import model.Contacts;
import model.Customer;
import model.Users;

import java.awt.*;
import java.time.LocalTime;
import java.util.Locale;

public class AddAppointmentFormController {

    @FXML
    private TextField appointmentIdTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private ComboBox<Contacts> contactsComboBox;
    @FXML
    private TextField typeTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private LocalTime startTime;
    @FXML
    private LocalTime endTime;
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private ComboBox<Users> usersComboBox;

    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button cancelAddAppointmentButton;




}
