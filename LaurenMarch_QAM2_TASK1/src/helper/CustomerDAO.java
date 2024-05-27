package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import model.Customer;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * This class is the Data Access Object class for the contacts table.
 */
public class CustomerDAO {

    /**
     * This method creates a list of all customers from the customer table in the database.
     */
    public static ObservableList<Customer> getCustomerList() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Create_Date, " +
                "customers.Last_Update, customers.Created_By, customers.Last_Updated_By, customers.Postal_Code, " +
                "customers.Phone, customers.Division_ID, first_level_divisions.Division, first_level_divisions.Country_ID, " +
                "countries.Country FROM customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID ORDER BY customers.Customer_ID";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String customerPostalCode = rs.getString("Postal_Code");
                String customerPhone = rs.getString("Phone");
                String createdBy = rs.getString("Created_By");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int customerDivisionId = rs.getInt("Division_ID");
                String customerDivisionName = rs.getString("Division");
                int customerCountryId = rs.getInt("Country_ID");
                String customerCountryName = rs.getString("Country");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();

                Customer customer = new Customer(customerId, customerName, customerAddress, customerPostalCode, customerPhone,
                        createdBy, lastUpdatedBy, customerDivisionId, customerDivisionName, customerCountryId, customerCountryName, createDate, lastUpdate);
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    /**
     * This method creates a list of customer IDs.
     * @return returns list based on Contact_ID
     */
    public static ObservableList<Integer> getCustomerIdList() {
        ObservableList<Integer> customerIdList = FXCollections.observableArrayList();
        String sql = "SELECT customers.Customer_ID FROM customers ORDER BY customers.Customer_ID";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");

                customerIdList.add(customerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerIdList;
    }

    /**
     * This method inserts customer objects into the database from the SQL INSERT INTO statement.
     * This is used to add customer to the database.
     */
    public static void addCustomer(Customer customer, LocalDateTime createdDate, LocalDateTime lastUpdated) throws SQLException {
        String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Last_Update, Created_By, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customer.getCustomerId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPostalCode());
            ps.setString(5, customer.getPhoneNumber());
            ps.setTimestamp(6, Timestamp.valueOf(createdDate));
            ps.setTimestamp(7, Timestamp.valueOf(lastUpdated));
            ps.setString(8, customer.getCreatedBy());
            ps.setString(9, customer.getLastUpdatedBy());
            ps.setInt(10, customer.getDivisionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method updates customer objects into the database from the SQL UPDATE - SET statement.
     * This is used to update customer records to the database based on the Customer_ID.
     */
    public static void updateCustomer(Customer customer, LocalDateTime lastUpdated) {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Updated_By = ?, Last_Update = ?, Division_ID = ? WHERE Customer_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhoneNumber());
            ps.setString(5, customer.getLastUpdatedBy());
            ps.setTimestamp(6, Timestamp.valueOf(lastUpdated));
            ps.setInt(7, customer.getDivisionId());
            ps.setInt(8, customer.getCustomerId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method deletes customers from the database using the SQL DELETE statement.
     * Deletes customer data based on Customer_ID.
     * @param customerId customerId that gets selected by the user from the tableview on AppointmentsForm.
     */
    public static void deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method gets the next customer ID in order for the application to increment customer IDs when creating new customers.
     * @return returns the next customer ID or 1 if no customers exist.
     */
    public static int getNextCustomerId() {
        String sql = "SELECT Customer_ID FROM customers ORDER BY Customer_ID";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int expectedId = 1;
            while (rs.next()) {
                int currentId = rs.getInt("Customer_ID");
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
     * This method is to populate the piechart in the Reports form with number of customers based on their country.
     * @return returns the number of customers based on their country.
     */
    public static ObservableList<PieChart.Data> getCustomerCountByCountry() {
        ObservableList<PieChart.Data> customerCountByCountry = FXCollections.observableArrayList();
        String sql = "SELECT countries.Country, COUNT(customers.Customer_ID) as CustomerCount " +
                "FROM customers " +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID " +
                "GROUP BY countries.Country";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String country = rs.getString("Country");
                int count = rs.getInt("CustomerCount");
                customerCountByCountry.add(new PieChart.Data(country + " (" + count + ")", count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerCountByCountry;
    }

}
