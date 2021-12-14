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
     * Uses Stream API and a method reference to filter Country names out of an ArrayList of Country objects.
     * The intermediate stream operation map() takes an implementation of the functional interface Function&lt;T, R&gt;
     * as a parameter. A method reference Country::getCountryName was used to implement the single abstract method R apply(T).
     * The parameter type T was deduced by the stream to be a Country object, while return type R is the return type of
     * getCountryName() which is String.
     * The collect terminal operation on the stream uses method references to create an ArrayList and provide
     * methods for adding each item of the stream to ArrayList as well as combine results from possible parallel
     * operations on the same stream.
     * @return A list of Strings for all countries.
     */
    public static ArrayList<String> getCountryList() {
        return countries.stream().map(Country::getCountryName)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    }

    /**
     * Uses Stream API and lambda expression to filter List of Divisions by a given Country name.
     * The intermediate stream operation filter() takes an implementation of the functional interface Predicate&lt;T&gt;
     * as a parameter. A lambda expression was used to implement Predicate&lt;T&gt; anonymously by providing
     * an implementation of the single abstract method public boolean test(T t1, T t2).
     * A Method reference was used to filter First-Level Division names out of an ArrayList of Division objects.
     * The intermediate stream operation map() takes an implementation of the functional interface Function&lt;T, R&gt;
     * as a parameter. A method reference Division::getDivision was used to implement the single abstract method R apply(T).
     * The parameter type T was deduced by the stream to be a Division object, while return type R is the return type of
     * getDivision() which is String.
     * The collect terminal operation on the stream uses method references to create an ArrayList and provide
     * methods for adding each item of the stream to ArrayList as well as combine results from possible parallel
     * operations on the same stream.
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
