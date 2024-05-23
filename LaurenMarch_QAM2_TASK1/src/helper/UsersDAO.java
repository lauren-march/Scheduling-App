package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UsersDAO {

    public static ObservableList<Users> getUsersList() {
        ObservableList<Users> usersList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users ORDER BY User_ID";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                String createdBy = rs.getString("Created_By");
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                Users users = new Users(userId, userName, password, null, createdBy, null, lastUpdatedBy);
                usersList.add(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }

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
