package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is the Data Access Object class for the contacts table.
 */
public class ContactsDAO {

    /**
     * This method creates a list of all contacts from the contacts table in the database.
     * @return returns contactsList
     */
    public static ObservableList<Contacts> getContactsList() {
        ObservableList<Contacts> contactsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM contacts";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                Contacts contacts = new Contacts(contactId, contactName, email);
                contactsList.add(contacts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactsList;
    }

    /**
     * This method creates a list of contacts filtered based on Contact_ID.
     * @param contactId selected contact ID will create a list based on this parameter
     * @return returns list based on Contact_ID
     */
    public static Contacts getContactById(int contactId) {
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, contactId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                return new Contacts(contactId, contactName, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
