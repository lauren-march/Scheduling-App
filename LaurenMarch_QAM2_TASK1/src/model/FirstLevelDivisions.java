package model;

import java.time.LocalDateTime;

public class FirstLevelDivisions {

    private int divisionId;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryId;

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

    @Override
    public String toString() {
        return division;
    }
}
