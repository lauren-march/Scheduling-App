package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactsDAO {

    public static ObservableList<Contacts> getContactsList() {
        ObservableList<Contacts> contactsList = FXCollections.observableArrayList();
        String sql = "Select * From contacts";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String password = rs.getString("Email");

                Contacts users = new Contacts(contactId, contactName, password);
                contactsList.add(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactsList;
    }

}
