package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivisions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This class is the Data Access Object class for the first level divisions table.
 */
public class FirstLevelDivisionsDAO {

    /**
     * This method creates a list of divisions filtered based on Country_ID.
     * @param countryId selected country ID
     * @return returns divisionsList
     */
    public static ObservableList<FirstLevelDivisions> getDivisionsByCountryId(int countryId) {
        ObservableList<FirstLevelDivisions> divisionsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int country_Id = rs.getInt("Country_ID");

                FirstLevelDivisions fld = new FirstLevelDivisions(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, country_Id);
                divisionsList.add(fld);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionsList;
    }

    /**
     * This method gets divisions filtered based on Division_ID.
     * @param divisionId parameter to filter by.
     * @return returns new division if one exists, or null if it does not
     */
    public static FirstLevelDivisions getDivisionById(int divisionId) {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, divisionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String division = rs.getString("Division");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryId = rs.getInt("Country_ID");

                return new FirstLevelDivisions(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
