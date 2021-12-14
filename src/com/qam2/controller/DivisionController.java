package com.qam2.controller;

import com.qam2.db.DBConnectionManager;
import com.qam2.model.Division;

import java.sql.*;
import java.util.ArrayList;

/**
 * Provides Division record database functionality.
 * All methods are static, class not intended to be instantiated.
 * Facilitates reading all Division records, Division records are read-only
 * @author Alex Hanson
 */
public abstract class DivisionController {

    private final static Connection con = DBConnectionManager.getInstance().getConnection();

    /**
     * Reads all Division records from the database.
     * NOTE: ArrayList could be empty. 1) There are no divisions. 2) There was an exception executing database query.
     * Caller must verify contents of returned ArrayList.
     * @return An ArrayList containing all Division records.
     */
    public static ArrayList<Division> readAll() {

        var divisions = new ArrayList<Division>();

        if(con != null) {
            try(
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery("SELECT Division_ID, Division, Country_ID "+
                                                               "FROM first_level_divisions "+
                                                               "ORDER BY Division ASC");
            ) {
                while(results.next()) {
                    var division = new Division(
                            results.getInt("Division_ID"),
                            results.getString("Division"),
                            results.getInt("Country_ID")
                            );

                    divisions.add(division);
                }
                return divisions;
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return null;
    }
}
