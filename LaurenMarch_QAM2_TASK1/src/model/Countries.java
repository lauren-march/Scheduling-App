package model;

import java.time.LocalDateTime;

/**
 * This class constructs country objects
 */
public class Countries {

    private int countryId;
    private String country;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    /**
     * This is a constructor for country objects
     * @param countryId countryId attribute
     * @param country country attribute
     * @param createDate createDate attribute
     * @param createdBy createdBy attribute
     * @param lastUpdate lastUpdate attribute
     * @param lastUpdatedBy lastUpdatedBy attribute
     */
    public Countries(int countryId, String country, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy){
        this.countryId = countryId;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

   // Getters and setters
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * toString conversion to handle memory address appearing in lieu of human-readable data
     * @return country attribute
     */
    @Override
    public String toString() {
        return country;
    }
}
