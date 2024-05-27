package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import model.Appointments;
import util.TimeUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * This class is the Data Access Object class for the appointments table.
 */
public class AppointmentsDAO {

    /**
     * This method creates a list of all appointments from the appointments table in the database.
     */
    public static ObservableList<Appointments> getAppointmentsList() {
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, a.Type, a.Start, a.End, " +
                "a.Create_Date, a.Created_By, a.Last_Update, a.Last_Updated_By, a.Customer_ID, a.User_ID, " +
                "a.Contact_ID, c.Contact_Name " +
                "FROM client_schedule.appointments a " +
                "JOIN client_schedule.contacts c ON a.Contact_ID = c.Contact_ID " +
                "ORDER BY a.Appointment_ID";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = TimeUtil.timestampToLocal(rs.getTimestamp("Start"));
                LocalDateTime end = TimeUtil.timestampToLocal(rs.getTimestamp("End"));
                LocalDateTime createDate = TimeUtil.timestampToLocal(rs.getTimestamp("Create_Date"));
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = TimeUtil.timestampToLocal(rs.getTimestamp("Last_Update"));
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end,
                        createDate, createdBy, lastUpdate, lastUpdateBy, customerId, userId, contactId, contactName);
                appointmentsList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentsList;
    }

    /**
     * This method creates a list from the database SQL command SELECT.
     * This gets the appointments based on the Customer_ID
     * and joins the contacts table using the foreign key Contact_ID
     * @return returns the appointmentsByCustomerIdList used to find appointments by customer IDs
     */
    public static ObservableList<Appointments> getAppointmentsByCustomerId(int customerId) {
        ObservableList<Appointments> appointmentsByCustomerIdList = FXCollections.observableArrayList();
        String sql = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, a.Type, a.Start, a.End, " +
                "a.Create_Date, a.Created_By, a.Last_Update, a.Last_Updated_By, a.Customer_ID, a.User_ID, " +
                "a.Contact_ID, c.Contact_Name " +
                "FROM client_schedule.appointments a " +
                "JOIN client_schedule.contacts c ON a.Contact_ID = c.Contact_ID " +
                "WHERE a.Customer_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = TimeUtil.timestampToLocal(rs.getTimestamp("Start"));
                LocalDateTime end = TimeUtil.timestampToLocal(rs.getTimestamp("End"));
                LocalDateTime createDate = TimeUtil.timestampToLocal(rs.getTimestamp("Create_Date"));
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = TimeUtil.timestampToLocal(rs.getTimestamp("Last_Update"));
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end,
                        createDate, createdBy, lastUpdate, lastUpdateBy, customerId, userId, contactId, contactName);
                appointmentsByCustomerIdList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentsByCustomerIdList;
    }

    /**
     * This method inserts appointment objects into the database from the SQL INSERT INTO statement.
     * This is used to add appointments to the database and convert to UTC.
     */
    public static void addAppointment(Appointments appointments, Timestamp startUTC, Timestamp endUTC) throws SQLException {
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, " +
                "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, appointments.getAppointmentId());
            ps.setString(2, appointments.getTitle());
            ps.setString(3, appointments.getDescription());
            ps.setString(4, appointments.getLocation());
            ps.setString(5, appointments.getType());
            ps.setTimestamp(6, startUTC);
            ps.setTimestamp(7, endUTC);
            ps.setTimestamp(8, Timestamp.valueOf(appointments.getCreateDate()));
            ps.setString(9, appointments.getCreatedBy());
            ps.setTimestamp(10, Timestamp.valueOf(appointments.getLastUpdate()));
            ps.setString(11, appointments.getLastUpdateBy());
            ps.setInt(12, appointments.getCustomerId());
            ps.setInt(13, appointments.getUserId());
            ps.setInt(14, appointments.getContactId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method updates appointment objects into the database from the SQL UPDATE - SET statement.
     * This is used to update appointments to the database based on the AppointmentID.
     * @param appointments appointment object
     * @param startUTC start time converted to UTC using Timestamp for SQL
     * @param endUTC end time converted to UTC using Timestamp for SQL
     */
    public static void updateAppointment(Appointments appointments, Timestamp startUTC, Timestamp endUTC) {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, appointments.getTitle());
            ps.setString(2, appointments.getDescription());
            ps.setString(3, appointments.getLocation());
            ps.setString(4, appointments.getType());
            ps.setTimestamp(5, startUTC);
            ps.setTimestamp(6, endUTC);
            ps.setTimestamp(7, Timestamp.valueOf(appointments.getLastUpdate()));
            ps.setString(8, appointments.getLastUpdateBy());
            ps.setInt(9, appointments.getCustomerId());
            ps.setInt(10, appointments.getUserId());
            ps.setInt(11, appointments.getContactId());
            ps.setInt(12, appointments.getAppointmentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method deletes appointments from the database using the SQL DELETE statement.
     * Deletes apopintment data based on Appointment_ID.
     * @param appointmentId appointmentID that get selected by the user from the tableview on AppointmentsForm.
     */
    public static void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method gets the next appointment ID in order for the application to increment appointment IDs when creating new appointments.
     * @return returns the next appointment ID or 1 if no appointments exist.
     */
    public static int getNextAppointmentId() {
        String sql = "SELECT Appointment_ID FROM appointments ORDER BY Appointment_ID";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int expectedId = 1;
            while (rs.next()) {
                int currentId = rs.getInt("Appointment_ID");
                if (currentId != expectedId) {
                    return expectedId;
                }
                expectedId++;
            }
            return expectedId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * This method is to find appointment times for the Login form check.
     * This creates a list of appointments that will start within the next 15 minutes local time.
     * @param currentTime checks the current time to reference against times for appointments in database.
     * @return returns list of appointments starting within in next 15 minutes local time if any.
     */
    public static ObservableList<Appointments> getAppointmentsWithin15Minutes(LocalDateTime currentTime) {
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, a.Type, a.Start, a.End, " +
                "a.Create_Date, a.Created_By, a.Last_Update, a.Last_Updated_By, a.Customer_ID, a.User_ID, " +
                "a.Contact_ID, c.Contact_Name " +
                "FROM client_schedule.appointments a " +
                "JOIN client_schedule.contacts c ON a.Contact_ID = c.Contact_ID";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = TimeUtil.timestampToLocal(rs.getTimestamp("Start"));
                LocalDateTime end = TimeUtil.timestampToLocal(rs.getTimestamp("End"));
                LocalDateTime createDate = TimeUtil.timestampToLocal(rs.getTimestamp("Create_Date"));
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = TimeUtil.timestampToLocal(rs.getTimestamp("Last_Update"));
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end,
                        createDate, createdBy, lastUpdate, lastUpdateBy, customerId, userId, contactId, contactName);

                // Check if the appointment start time is within the next 15 minutes
                if (ChronoUnit.MINUTES.between(currentTime, start) >= 0 && ChronoUnit.MINUTES.between(currentTime, start) <= 15) {
                    appointmentsList.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentsList;
    }

    /**
     * This method get appointments by month for one of the piecharts in the Reports form.
     * @param month shows the appointments based on month selected in comboBox.
     * @return list of appointments based on selected month if any and grouped by type
     */
    public static ObservableList<PieChart.Data> getAppointmentsByTypeForMonth(int month) {
        ObservableList<PieChart.Data> appointmentListByTypeForMonth = FXCollections.observableArrayList();
        String sql = "SELECT Type, COUNT(*) as Count FROM appointments " +
                "WHERE MONTH(Start) = ? GROUP BY Type";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, month);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type");
                int count = rs.getInt("Count");
                appointmentListByTypeForMonth.add(new PieChart.Data(type, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentListByTypeForMonth;
    }

    /**
     * This method gets a list of appointments by contactID to use for Reports form.
     * Based on which contact is selected, this will return a list of appointments that match the Contact_ID.
     * @param contactId selected contact from combobox in Reports form.
     * @return returns a list of appointments based on contact id.
     */
    public static ObservableList<Appointments> getAppointmentsByContactId(int contactId) {
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, a.Type, a.Start, a.End, " +
                "a.Create_Date, a.Created_By, a.Last_Update, a.Last_Updated_By, a.Customer_ID, a.User_ID, " +
                "a.Contact_ID, c.Contact_Name " +
                "FROM client_schedule.appointments a " +
                "JOIN client_schedule.contacts c ON a.Contact_ID = c.Contact_ID " +
                "WHERE a.Contact_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, contactId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = TimeUtil.timestampToLocal(rs.getTimestamp("Start"));
                LocalDateTime end = TimeUtil.timestampToLocal(rs.getTimestamp("End"));
                LocalDateTime createDate = TimeUtil.timestampToLocal(rs.getTimestamp("Create_Date"));
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = TimeUtil.timestampToLocal(rs.getTimestamp("Last_Update"));
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                String contactName = rs.getString("Contact_Name");

                Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end,
                        createDate, createdBy, lastUpdate, lastUpdateBy, customerId, userId, contactId, contactName);
                appointmentsList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentsList;
    }

}
