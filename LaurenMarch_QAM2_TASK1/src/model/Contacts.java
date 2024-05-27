package model;

/**
 * This class constructs contact objects
 */
public class Contacts {

    private int contactId;
    private String contactName;
    private String email;

    /**
     * This is a constructor for contact objects
     * @param contactId contactId attribute
     * @param contactName contactName attribute
     * @param email email attribute
     */
    public Contacts(int contactId, String contactName, String email){
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    // Getters and setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * toString conversion to handle memory address appearing in lieu of human-readable data
     * @return returns all attributes converted to strings
     */
    @Override
    public String toString() {
        return contactName;
    }

}
