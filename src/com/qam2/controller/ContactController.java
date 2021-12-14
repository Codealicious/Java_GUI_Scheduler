package com.qam2.controller;

import com.qam2.db.DBConnectionManager;
import com.qam2.model.Contact;

import java.sql.*;
import java.util.ArrayList;

/**
 * Provides Contact record database functionality.
 * All methods are static, class not intended to be instantiated.
 * Facilitates reading all Contact records. Contact records are read-only.
 * @author Alex Hanson
 */
public abstract class ContactController {

    private final static Connection con = DBConnectionManager.getInstance().getConnection();

    /**
     * Reads all Contact records from the database.
     * NOTE: ArrayList could be empty. 1) There are no contacts. 2) There was an exception executing database query.
     * Caller must verify contents of returned ArrayList.
     * @return An ArrayList containing all Contact records.
     */
    public static ArrayList<Contact> readAll() {

        var contacts = new ArrayList<Contact>();

        if(con != null) {
            try(
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery("SELECT Contact_ID, Contact_Name FROM contacts ORDER BY Contact_Name ASC");
            ) {
                while(results.next()) {
                    var contact = new Contact(
                            results.getInt("Contact_ID"),
                            results.getString("Contact_Name"));

                    contacts.add(contact);
                }
                return contacts;
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return null;
    }
}
