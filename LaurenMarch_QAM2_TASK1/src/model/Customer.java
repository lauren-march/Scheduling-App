package model;

import java.time.LocalDateTime;

/**
 * This class constructs customer objects
 */
public class Customer {
    private int customerId;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String createdBy;
    private String lastUpdatedBy;
    private int divisionId;
    private String divisionName;
    private int countryId;
    private String countryName;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;

    /** Constructor for customer object
     *
     * @param customerId customerId attribute
     * @param name name attribute
     * @param address address
     * @param postalCode postalCode attribute
     * @param phoneNumber phoneNumber attribute
     * @param createdBy createdBy attribute
     * @param lastUpdatedBy lastUpdatedBy attribute
     * @param divisionId divisionId attribute
     * @param divisionName divisionName attribute
     * @param countryId countryId attribute
     * @param countryName countryName attribute
     * @param createDate createDate attribute
     * @param lastUpdate lastUpdate attribute
     */
    public Customer(int customerId, String name, String address, String postalCode, String phoneNumber,
                    String createdBy, String lastUpdatedBy, int divisionId, String divisionName, int countryId, String countryName,
                    LocalDateTime createDate, LocalDateTime lastUpdate) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
        this.countryName = countryName;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
