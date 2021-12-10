package com.qam2.model;

/**
 * Immutable class representing a single Country record.
 * @author Alex Hanson
 */
public final class Country {

    private final int countryID;
    private final String countryName;

    /**
     * Creates an object representing a Country database record. Only database record columns of concern are country name and ID.
     * @param countryID The Country's ID.
     * @param countryName The Country's name.
     */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * @return The Country's ID.
     */
    public int getCountryID() { return countryID; }

    /**
     * @return The Country's name.
     */
    public String getCountryName() { return countryName; }
}
