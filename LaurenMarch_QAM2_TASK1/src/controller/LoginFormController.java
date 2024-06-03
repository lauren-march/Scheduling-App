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

/**
 * This class handles the functionality and UI elements of the LoginForm.
 */
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

    /**
     * This is the initialize method and is automatically called by JavaFx when this form loads.
     * It handles the UI elements, time display, and localization for the LoginForm.
     */
    @FXML
    public void initialize() {
        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        reloadText();

        updateDateTime();

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updateDateTime());
            }
        }, 0, 1000);

        setLanguageStyles();
        englishLabel.setOnMouseClicked(this::setEnglish);
        frenchLabel.setOnMouseClicked(this::setFrench);
        englishLabel.setOnMouseEntered(event -> englishLabel.setStyle("-fx-text-fill: cyan; -fx-cursor: hand;"));
        frenchLabel.setOnMouseEntered(event -> frenchLabel.setStyle("-fx-text-fill: cyan; -fx-cursor: hand;"));
        englishLabel.setOnMouseExited(event -> setLanguageStyles());
        frenchLabel.setOnMouseExited(event -> setLanguageStyles());
    }

    /**
     * This method handles the Login button.
     * Calls the ActivityLoggerUtil.log() function to create a log file.
     * If login is successful after calls authenticate() method, then calls checkForUpcomingAppointments() and
     * loadAppointmentsForm() or gives the user a login error.
     */
    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean loginSuccess = authenticate(username, password);
        ActivityLoggerUtil.log(username, loginSuccess);

        if (loginSuccess) {
            errorLabel.setText("");
            checkForUpcomingAppointments();
            loadAppointmentsForm();
        } else {
            errorLabel.setText(bundle.getString("login.error"));
        }
    }

    /**
     * This method handles the Exit button and calls the Java built in method Platform.exit() which closes the application.
     */
    @FXML
    private void handleExitButton(){
        Platform.exit();
    }

    /**
     * This helper method authenticates the login credentials.
     * This creates a connection to the database to the users table and checks to see all users.
     * If a user does not exist or the username and password do not match to the corresponding user, an error message will populate.
     * @param username user entered username
     * @param password user entered password
     * @return returns true if authenticated, returns false if username and password do not match username and password combo in database.
     */
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

    /**
     * This helper method checks to see if there are any upcoming appointments in the next 15 minutes from the local date/time.
     * The database is checked to see any relevant appointments.
     * If there are appointments, a dialogue box will pop up and display the appointment information.
     * If there are no appointments, a dialogue box will pop up and state there are no upcoming appointments within 15 minutes.
     */
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

    /**
     * This helper method will load the AppointmentsForm when the Login button is clicked by the user.
     * (Pending authentication in other parts of the code)
     */
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

    /**
     * This helper method creates the ZonedDateTime object 'now' and formats the
     */
    private void updateDateTime() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a z");
        dateTimeLabel.setText(now.format(formatter));
    }

    /**
     * This helper method displays English or French in cyan depending on which text is selected.
     */
    private void setLanguageStyles() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            englishLabel.setStyle("-fx-text-fill: cyan;");
            frenchLabel.setStyle("-fx-text-fill: black;");
        } else {
            englishLabel.setStyle("-fx-text-fill: black;");
            frenchLabel.setStyle("-fx-text-fill: cyan;");
        }
    }

    /**
     * This method ensure all the text is in English based on the resource bundle for messages_en.properties.
     * @param event mouse click event of text.
     */
    private void setEnglish(MouseEvent event) {
        Locale.setDefault(new Locale("en", "US"));
        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        reloadText();
        setLanguageStyles();
    }

    /**
     * This method ensure all the text is in English based on the resource bundle for messages_fr.properties.
     * @param event mouse click event of text.
     */
    private void setFrench(MouseEvent event) {
        Locale.setDefault(new Locale("fr", "FR"));
        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        reloadText();
        setLanguageStyles();
    }

    /**
     * This method alters the UI display of the textfield for a cleaner UI look and feel.
     */
    private void adjustTextFieldWidth() {
        double newWidth = usernameLabel.getWidth() + 20;
        usernameField.setPrefWidth(newWidth > 200 ? newWidth : 200);
    }

    /**
     * This method reloads all the text when English or French is selected.
     * Calls adjustTextFieldWidth() to ensure text is not cutoff when switching to French.
     */
    private void reloadText() {
        titleLabel.setText(bundle.getString("title.login"));
        subtitleLabel.setText(bundle.getString("subtitle.login"));
        usernameLabel.setText(bundle.getString("login.username"));
        passwordLabel.setText(bundle.getString("login.password"));
        loginButton.setText(bundle.getString("login.button"));
        errorLabel.setText("");
        adjustTextFieldWidth();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
