package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * This class is the Data Access Object class for the country table.
 */
public class CountryDAO {

    /**
     * This method creates a list of all countries in the countries table in the database.
     * @return returns countriesList
     */
    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> countriesList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM countries";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                Countries c = new Countries(countryId, country, createDate, createdBy, lastUpdate, lastUpdatedBy);
                countriesList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countriesList;
    }

    /**
     * This method creates a list of countries filtered based on Country_ID.
     * @param countryId selected country ID will create a list based on this parameter.
     * @return returns list based on Country_ID
     */
    public static Countries getCountryById(int countryId) {
        String sql = "SELECT * FROM countries WHERE Country_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String country = rs.getString("Country");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                return new Countries(countryId, country, createDate, createdBy, lastUpdate, lastUpdatedBy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
