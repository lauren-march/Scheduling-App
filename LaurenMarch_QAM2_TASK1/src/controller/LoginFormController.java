package controller;

import helper.AppointmentsDAO;
import helper.JDBC;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointments;
import util.ActivityLoggerUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class LoginFormController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Label englishLabel;
    @FXML
    private Label frenchLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;

    @FXML
    private Button loginButton;

    private ResourceBundle bundle;
    public static String currentUser;

    @FXML
    public void initialize() {
        // Set up resource bundle for localization
        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        reloadText();

        // Set initial date and time
        updateDateTime();

        // Schedule a timer to update the date and time every second
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updateDateTime());
            }
        }, 0, 1000);

        // Set initial styles and event handlers for language selection
        setLanguageStyles();
        englishLabel.setOnMouseClicked(this::setEnglish);
        frenchLabel.setOnMouseClicked(this::setFrench);
        englishLabel.setOnMouseEntered(event -> englishLabel.setStyle("-fx-text-fill: cyan; -fx-cursor: hand;"));
        frenchLabel.setOnMouseEntered(event -> frenchLabel.setStyle("-fx-text-fill: cyan; -fx-cursor: hand;"));
        englishLabel.setOnMouseExited(event -> setLanguageStyles());
        frenchLabel.setOnMouseExited(event -> setLanguageStyles());

        // Add text field length validation
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 25) {
                errorLabel.setText("Username cannot exceed 25 characters");
                usernameField.setText(oldValue);
            } else {
                errorLabel.setText("");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                errorLabel.setText("Password cannot exceed 30 characters");
                passwordField.setText(oldValue);
            } else {
                errorLabel.setText("");
            }
        });
    }

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean loginSuccess = authenticate(username, password);
        ActivityLoggerUtil.log(username, loginSuccess);

        if (loginSuccess) {
            errorLabel.setText("");
            checkForUpcomingAppointments(); // Check for upcoming appointments
            loadAppointmentsForm();
        } else {
            errorLabel.setText(bundle.getString("login.error"));
        }
    }

    @FXML
    private void handleExitButtonAction(){
        Platform.exit();
    }

    private boolean authenticate(String username, String password) {
        Connection connection = JDBC.connection;
        String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentUser = username;
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    private void checkForUpcomingAppointments() {
        LocalDateTime currentTime = LocalDateTime.now();
        ObservableList<Appointments> upcomingAppointments = AppointmentsDAO.getAppointmentsWithin15Minutes(currentTime);

        if (upcomingAppointments.isEmpty()) {
            showAlert("No Upcoming Appointments", "There are no upcoming appointments within the next 15 minutes.");
        } else {
            StringBuilder alertContent = new StringBuilder("You have the following appointments within the next 15 minutes:\n");
            for (Appointments appointment : upcomingAppointments) {
                alertContent.append("Appointment ID: ").append(appointment.getAppointmentId())
                        .append(", Date: ").append(appointment.getStart().toLocalDate())
                        .append(", Time: ").append(appointment.getStart().toLocalTime()).append("\n");
            }
            showAlert("Upcoming Appointments", alertContent.toString());
        }
    }

    private void loadAppointmentsForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsForm.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateDateTime() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a z");
        dateTimeLabel.setText(now.format(formatter));
    }

    private void setLanguageStyles() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            englishLabel.setStyle("-fx-text-fill: cyan;");
            frenchLabel.setStyle("-fx-text-fill: black;");
        } else {
            englishLabel.setStyle("-fx-text-fill: black;");
            frenchLabel.setStyle("-fx-text-fill: cyan;");
        }
    }

    private void setEnglish(MouseEvent event) {
        Locale.setDefault(new Locale("en", "US"));
        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        reloadText();
        setLanguageStyles();
    }

    private void setFrench(MouseEvent event) {
        Locale.setDefault(new Locale("fr", "FR"));
        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        reloadText();
        setLanguageStyles();
    }

    private void adjustTextFieldWidth() {
        double newWidth = usernameLabel.getWidth() + 20; // 20 is just an additional padding factor
        usernameField.setPrefWidth(newWidth > 200 ? newWidth : 200); // Ensuring a minimum width of 200
    }

    private void reloadText() {
        // Reload all the text that needs to be localized
        titleLabel.setText(bundle.getString("title.login"));
        subtitleLabel.setText(bundle.getString("subtitle.login"));
        usernameLabel.setText(bundle.getString("login.username"));
        passwordLabel.setText(bundle.getString("login.password"));
        loginButton.setText(bundle.getString("login.button"));
        errorLabel.setText(""); // Clear error message when changing language
        adjustTextFieldWidth();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
