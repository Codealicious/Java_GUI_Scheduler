package com.qam2.controller;

import com.qam2.db.DBConnectionManager;
import com.qam2.model.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Provides User record database functionality.
 * All methods are static, class not intended to be instantiated.
 * Facilitates read operations on User records, User records are read-only.
 * @author Alex Hanson
 */
public abstract class UserController {

    private final static Connection con = DBConnectionManager.getInstance().getConnection();

    /**
     * Reads a single User from the database.
     * Provides password field for authentication purposes.
     * @param name The username of the User record to read.
     * @return If read is successful a reference to a User, null otherwise.
     */
    public static User read(String name) {

        if(con != null) {
            try(
                    PreparedStatement stmt = con.prepareStatement("SELECT User_ID, User_Name, Password FROM users WHERE User_Name = ?");
            ){

                stmt.setString(1, name);
                ResultSet result = stmt.executeQuery();

                if(result.next()) {

                    return  new User(
                            result.getInt("User_ID"),
                            result.getString("User_Name"),
                            result.getString("Password"));
                }
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No Connection to Database...");
        }
        return null;
    }

    /**
     * Reads all User records from the database.
     * Password fields are left out.
     * NOTE: ArrayList could be empty. 1) There are no users. 2) There was an exception executing database query.
     * Caller must verify contents of returned ArrayList.
     * @return An ArrayList containing all User records.
     */
    public static ArrayList<User> readAll() {

        if(con != null) {

            var users = new ArrayList<User>();

            try(
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery("SELECT User_ID, User_Name FROM users ORDER BY User_Name ASC");
                    ){

                while(results.next()) {
                    var user = new User(
                            results.getInt("User_ID"),
                            results.getString("User_Name"),
                            ""
                    );

                    users.add(user);
                }

                return users;
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No Connection to Database...");
        }
        return null;
    }
}
