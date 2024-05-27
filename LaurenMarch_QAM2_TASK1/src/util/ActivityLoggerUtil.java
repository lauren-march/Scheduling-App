package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is to construct activity log objects used for logging.
 */
public class ActivityLoggerUtil {
    private static final String LOG_FILE = "login_activity.txt";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * This method creates a log file and writes new entries based on logins.
     * @param username parameter for username text field.
     * @param success parameter for determining a successful login attempt.
     */
    public static void log(String username, boolean success) {
        String status = success ? "SUCCESS" : "FAILURE";
        String timestamp = LocalDateTime.now().format(dtf);
        String logEntry = String.format("User: %s | Login Status: %s | Timestamp: %s", username, status, timestamp);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
