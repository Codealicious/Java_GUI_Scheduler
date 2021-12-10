package com.qam2.controller;

import com.qam2.db.DBConnectionManager;
import com.qam2.model.Customer;
import com.qam2.utils.AppointmentManager;
import com.qam2.utils.time.TimeUtil;
import com.qam2.view.AppointmentView;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class providing Customer record database functionality.
 * All methods are static, class not intended to be instantiated.
 * Facilitates CRUD operations on Customers: read, add, update, and delete.
 * @author Alex Hanson
 */
public abstract class CustomerController {

    private final static Connection con = DBConnectionManager.getInstance().getConnection();

    /**
     * Add a Customer to the database.
     * @param c The Customer to add.
     * @return If add was successful a reference to newly added Customer with auto-generated ID, null otherwise.
     */
    public static Customer add(Customer c) {

        if(con != null && c != null) {

            ResultSet key;

            try (
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO customers " +
                            "(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, " +
                            "Last_Updated_By, Division_ID) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ) {

                stmt.setString(1, c.getCustomerName());
                stmt.setString(2, c.getAddress());
                stmt.setString(3, c.getPostalCode());
                stmt.setString(4, c.getPhone());
                stmt.setString(5, TimeUtil.toDateTimeString(c.getCreateDate()));
                stmt.setString(6, c.getCreatedBy());
                stmt.setString(7, TimeUtil.toDateTimeString(c.getLastUpdate()));
                stmt.setString(8, c.getLastUpdatedBy());
                stmt.setInt(9, c.getDivisionID());

                if(stmt.executeUpdate() == 1) {

                    key = stmt.getGeneratedKeys();
                    key.next();

                    return new Customer(
                            key.getInt(1),
                            c.getCustomerName(),
                            c.getAddress(),
                            c.getPostalCode(),
                            c.getPhone(),
                            c.getCreateDate(),
                            c.getCreatedBy(),
                            c.getLastUpdate(),
                            c.getLastUpdatedBy(),
                            c.getDivisionID()
                    );
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        } else {
            System.err.println("No connection to database....");
        }
        return null;
    }

    /**
     * Reads all Customer records from the database.
     * NOTE: ArrayList could be empty. 1) There are no customers. 2) There was an exception executing database query.
     * Caller must verify contents of returned ArrayList.
     * @return An ArrayList containing all Customer records.
     */
    public static ArrayList<Customer> readAll() {

        var customers = new ArrayList<Customer>();

        if(con != null) {

            try(
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery("SELECT * FROM customers ORDER BY Customer_Name ASC");
            ) {
                while(results.next()) {
                    var cust = new Customer(
                            results.getInt("Customer_ID"),
                            results.getString("Customer_Name"),
                            results.getString("Address"),
                            results.getString("Postal_Code"),
                            results.getString("Phone"),
                            TimeUtil.parseUTCZonedDateTime(results.getTimestamp("Create_Date").toString()),
                            results.getString("Created_By"),
                            TimeUtil.parseUTCZonedDateTime(results.getTimestamp("Last_Update").toString()),
                            results.getString("Last_Updated_By"),
                            results.getInt("Division_ID")
                            );

                    customers.add(cust);
                }
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return customers;
    }

    /**
     * Update a Customer in the database.
     * @param c The Customer to update.
     * @return True if the Customer is successfully updated, false otherwise.
     */
    public static boolean update(Customer c) {

        if(con != null && c != null) {

            try (
                    PreparedStatement stmt = con.prepareStatement("UPDATE customers SET " +
                    "Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                    "Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
                    "WHERE Customer_ID = ?");
            ) {

                stmt.setString(1, c.getCustomerName());
                stmt.setString(2, c.getAddress());
                stmt.setString(3, c.getPostalCode());
                stmt.setString(4, c.getPhone());
                stmt.setString(5, TimeUtil.toDateTimeString(c.getLastUpdate()));
                stmt.setString(6, c.getLastUpdatedBy());
                stmt.setInt(7, c.getDivisionID());
                stmt.setInt(8, c.getCustomerID());

                return stmt.executeUpdate() == 1;
            } catch (SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return false;
    }

    /**
     * Delete a Customer from the database.
     * @param c The Customer to delete.
     * @return True if the Customer is deleted, false otherwise.
     */
    public static boolean delete(Customer c) {

        if(c != null) {

            if (AppointmentManager.getInstance().hasAppointments(c))
                return AppointmentView.getInstance().deleteAllForCustomer(c) && delete(c.getCustomerID());
            else
                return delete(c.getCustomerID());
        }

        return false;
    }

    private static boolean delete(int id) {

        if(con != null) {
            try (
                    PreparedStatement stmt = con.prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");
            ) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() == 1;
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return false;
    }

}
