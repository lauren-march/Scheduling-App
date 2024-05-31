package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * This class constructs appointment objects
 */
public class Appointments {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;
    private int customerId;
    private int userId;
    private int contactId;
    private transient String contactName;

    /**
     * This is a constructor WITH the contactName for use for Add/Update appointment forms
     * @param appointmentId appointmentId attribute
     * @param title title attribute
     * @param description description attribute
     * @param location location attribute
     * @param type type attribute
     * @param start start attribute
     * @param end end attribute
     * @param customerId customerId attribute
     * @param userId userId attribute
     * @param contactId contactId attribute
     * @param contactName contactName attribute
     */
    public Appointments(int appointmentId, String title, String description, String location, String type,
                        LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId,
                        String contactName) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * This is a constructor WITHOUT the contactName for use for Appointment form
     * @param appointmentId appointmentId attribute
     * @param title title attribute
     * @param description description attribute
     * @param location location attribute
     * @param type type attribute
     * @param start start attribute
     * @param end end attribute
     * @param createDate createDate attribute
     * @param createdBy createdBy attribute
     * @param lastUpdate lastUpdate attribute
     * @param lastUpdateBy lastUpdatedBy attribute
     * @param customerId customerId attribute
     * @param userId userId attribute
     * @param contactId contactId attribute
     */
    public Appointments(int appointmentId, String title, String description, String location, String type,
                        LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy,
                        LocalDateTime lastUpdate, String lastUpdateBy, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    // Getters and setters
    public int getAppointmentId() {

        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {

        this.appointmentId = appointmentId;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {

        return location;
    }

    public void setLocation(String location) {

        this.location = location;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {

        this.start = start;
    }

    public LocalDateTime getEnd() {

        return end;
    }

    public void setEnd(LocalDateTime end) {

        this.end = end;
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

    public String getLastUpdateBy() {

        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {

        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {

        this.customerId = customerId;
    }

    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }

    public int getContactId() {

        return contactId;
    }

    public void setContactId(int contactId) {

        this.contactId = contactId;
    }

    public String getContactName() {

        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * toString conversion to handle memory address appearing in lieu of human-readable data
     * @return returns contactName
     */
    @Override
    public String toString() {
        return "Appointments{" +
                "appointmentId=" + appointmentId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", createDate=" + createDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", lastUpdateBy='" + lastUpdateBy + '\'' +
                ", customerId=" + customerId +
                ", userId=" + userId +
                ", contactId=" + contactId +
                ", contactName=" + contactName +
                '}';
    }
}
