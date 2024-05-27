package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is the Data Access Object class for the users table.
 */
public class UsersDAO {

    /**
     * This method creates a list of user Ids.
     * @return returns userIdList
     */
    public static ObservableList<Integer> getUserIdList() {
        ObservableList<Integer> userIdList = FXCollections.observableArrayList();
        String sql = "SELECT User_ID FROM users ORDER BY User_ID";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");

                userIdList.add(userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userIdList;
    }
}
