package com.qam2.model;

import com.qam2.utils.CountriesAndDivisions;

/**
 * Immutable class representing a Division record.
 * @author Alex Hanson
 */
public final class Division {

    private final int divisionID;
    private final String division;
    private final int countryID;

    /**
     * Creates an obj representing a Division record. Only database record columns of concern are: Division_ID, Division, Country_ID.
     * @param divisionID The Division ID.
     * @param division The Division's name.
     * @param countryID The Country ID where the Division is located.
     */
    public Division(int divisionID, String division, int  countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    /**
     * @return The Division ID.
     */
    public int getDivisionID() { return divisionID; }

    /**
     * @return The name of the Division.
     */
    public String getDivision() { return division; }

    /**
     * @return The Country ID where the Division is located.
     */
    public int getCountryID() { return countryID; }

    /**
     * @return The name of the Country where the Division is located.
     */
    public String getCountry() { return CountriesAndDivisions.getCountryName(countryID); }

}
