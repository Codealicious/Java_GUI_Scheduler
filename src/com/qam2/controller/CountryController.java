package com.qam2.controller;

import com.qam2.db.DBConnectionManager;
import com.qam2.model.Country;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class providing Country record database functionality.
 * All methods are static, class not intended to be instantiated.
 * Facilitates reading all Country records. Country records are read-only.
 * @author Alex Hanson
 */
public abstract class CountryController {

    private final static Connection con = DBConnectionManager.getInstance().getConnection();

    /**
     * Reads all Country records from the database.
     * NOTE: ArrayList could be empty. 1) There are no countries. 2) There was an exception executing database query.
     * Caller must verify contents of returned ArrayList.
     * @return An ArrayList containing all Country records.
     */
    public static ArrayList<Country> readAll() {

        var countries = new ArrayList<Country>();

        if(con != null) {
            try(
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery("SELECT Country_ID, Country FROM countries ORDER BY Country ASC");
            ) {
                while(results.next()) {
                    var country = new Country(
                            results.getInt("Country_ID"),
                            results.getString("Country"));

                    countries.add(country);
                }
                return countries;
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return null;
    }
}
