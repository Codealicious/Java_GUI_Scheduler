package com.qam2.model;

/**
 * Immutable class representing a Contact record.
 * @author Alex Hanson
 */
public final class Contact {

    private final int contactID;
    private final String contactName;

    /**
     * Creates an object representing a Contact record. Only database record columns of concern are contact name and ID.
     * @param contactID Contact ID.
     * @param contactName Contact name.
     */
    public Contact(int contactID, String contactName) {
        this.contactID = contactID;
        this.contactName = contactName;
    }

    /**
     * @return The Contact's ID.
     */
    public int getContactID() { return contactID; }

    /**
     * @return The Contact's name.
     */
    public String getContactName() { return contactName; }
}
