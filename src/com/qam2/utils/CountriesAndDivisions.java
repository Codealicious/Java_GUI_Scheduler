package com.qam2.utils;

import com.qam2.controller.CountryController;
import com.qam2.controller.DivisionController;
import com.qam2.model.Country;
import com.qam2.model.Division;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides static functions to retrieve information about Countries and First Level Divisions.
 * @author Alex Hanson
 */
public abstract class CountriesAndDivisions {

    private final static ArrayList<Division> divisions;
    private final static ArrayList<Country> countries;
    private final static Map<Integer, Country> countryIDDictionary;
    private final static Map<Integer, Division> divisionIDDictionary;
    private final static Map<String, Division> divisionNameDictionary;


    static {

         divisions = DivisionController.readAll();
         countries = CountryController.readAll();

         countryIDDictionary = countries.stream().collect(Collectors.toMap(Country::getCountryID, c -> c));

         divisionIDDictionary = divisions.stream().collect(Collectors.toMap(Division::getDivisionID, d -> d));
         divisionNameDictionary = divisions.stream().collect(Collectors.toMap(Division::getDivision, d -> d));
    }

    /**
     * @return A list of Strings for all countries.
     */
    public static ArrayList<String> getCountryList() {
        return countries.stream().map(Country::getCountryName)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    }

    /**
     * @param country Name of a Country.
     * @return A list of Strings for all associated first level divisions.
     */
    public static ArrayList<String> getDivisionsForCountry(String country) {
        return divisions.stream()
                .filter(d -> d.getCountry().equals(country))
                .map(Division::getDivision)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * @param division Name of a division.
     * @return The divisions ID or -1.
     */
    public static int getDivisionID(String division) {
        var d = divisionNameDictionary.get(division);
        return d != null ? d.getDivisionID(): -1;
    }

    /**
     * @param id Division ID.
     * @return Name of division or empty string.
     */
    public static String getDivisionName(int id) {
        var d = divisionIDDictionary.get(id);
        return d != null ? d.getDivision() : "";
    }

    /**
     * @param id Country ID.
     * @return The country name or empty string.
     */
    public static String getCountryName(int id) {
        var c = countryIDDictionary.get(id);
        return c != null ? c.getCountryName() : "";
    }

    /**
     * @param id Division ID.
     * @return Country ID for a given division ID.
     */
    public static int getCountryIDForDivision(int id) {
        var d = divisionIDDictionary.get(id);
        return d != null ? d.getCountryID() : -1;
    }

}
