package model;

import java.time.LocalDateTime;

/**
 * This class constructs first level division objects
 */
public class FirstLevelDivisions {

    private int divisionId;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryId;

    /**
     * Constructor for first level division objects
     * @param divisionId divisionId attribute
     * @param division division attribute
     * @param createDate createDate attribute
     * @param createdBy createdBy attribute
     * @param lastUpdate lastUpdate attribute
     * @param lastUpdatedBy lastUpdatedBy attribute
     * @param countryId countryId attribute
     */
    public FirstLevelDivisions(int divisionId, String division, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate,
                               String lastUpdatedBy, int countryId){

        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    // Getters and setters
    public int getDivisionId() {

        return divisionId;
    }

    public void setDivisionId(int divisionId) {

        this.divisionId = divisionId;
    }

    public String getDivision() {

        return division;
    }

    public void setDivision(String division) {

        this.division = division;
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

    public int getCountryId() {

        return countryId;
    }

    public void setCountryId(int countryId) {

        this.countryId = countryId;
    }

    /**
     * toString conversion to handle memory address appearing in lieu of human-readable data
     * @return returns division
     */
    @Override
    public String toString() {
        return division;
    }
}
