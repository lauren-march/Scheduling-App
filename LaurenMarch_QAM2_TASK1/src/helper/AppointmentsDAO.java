package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.*;
import java.time.LocalDateTime;

public class AppointmentsDAO {

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
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
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
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
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

    public static void addAppointment(Appointments appointments, LocalDateTime createDate, LocalDateTime lastUpdated) throws SQLException {
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, " +
                "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, appointments.getAppointmentId());
            ps.setString(2, appointments.getTitle());
            ps.setString(3, appointments.getDescription());
            ps.setString(4, appointments.getLocation());
            ps.setString(5, appointments.getType());
            ps.setTimestamp(6, Timestamp.valueOf(appointments.getStart()));
            ps.setTimestamp(7, Timestamp.valueOf(appointments.getEnd()));
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

    public static void updateAppointment(Appointments appointments, LocalDateTime lastUpdated) {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, appointments.getTitle());
            ps.setString(2, appointments.getDescription());
            ps.setString(3, appointments.getLocation());
            ps.setString(4, appointments.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointments.getStart()));
            ps.setTimestamp(6, Timestamp.valueOf(appointments.getEnd()));
            ps.setTimestamp(7, Timestamp.valueOf(lastUpdated));
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

    public static void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

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
}
