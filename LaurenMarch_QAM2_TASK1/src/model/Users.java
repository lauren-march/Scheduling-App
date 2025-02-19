package model;

import java.time.LocalDateTime;

/**
 * This class constructs user objects
 */
public class Users {

    private int userId;
    private String userName;
    private String password;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    /**
     * This is a constructor for user objects
     * @param userId userId attribute
     * @param userName userName attribute
     * @param password password attribute
     * @param createDate createDate attribute
     * @param createdBy createdBy attribute
     * @param lastUpdate lastUpdate attribute
     * @param lastUpdatedBy lastUpdatedBy attribute
     */
    public Users(int userId, String userName, String password, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy){

        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    // Getters and setters
    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
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
     * This method converts userId to a String
     * @return returns userId to String
     */
    @Override
    public String toString() {
        return String.valueOf(userId);
    }
}
