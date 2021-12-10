package com.qam2.controller;

import com.qam2.db.DBConnectionManager;
import com.qam2.model.Appointment;
import com.qam2.model.Customer;
import com.qam2.utils.time.TimeUtil;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class providing Appointment record database functionality.
 * All methods are static, class not intended to be instantiated.
 * Facilitates CRUD operations on Appointments: read, add, update, and delete.
 * @author Alex Hanson
 */
public abstract class AppointmentController {

    private final static Connection con = DBConnectionManager.getInstance().getConnection();

    /**
     * Add an Appointment to the database.
     * @param a The Appointment to add.
     * @return If add was successful a reference to the newly added Appointment with auto-generated ID, null otherwise.
     */
    public static Appointment add(Appointment a) {

        if(con != null && a != null) {

            ResultSet key;

            try(
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO appointments " +
                            "(Title, Description, Location, Type, Start, End, Create_Date, Created_By, " +
                            "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    ) {

                stmt.setString(1, a.getTitle());
                stmt.setString(2, a.getDescription());
                stmt.setString(3, a.getLocation());
                stmt.setString(4, a.getType());
                stmt.setString(5, TimeUtil.toDateTimeString(a.getStart()));
                stmt.setString(6, TimeUtil.toDateTimeString(a.getEnd()));
                stmt.setString(7, TimeUtil.toDateTimeString(a.getCreateDate()));
                stmt.setString(8, a.getCreatedBy());
                stmt.setString(9, TimeUtil.toDateTimeString(a.getLastUpdate()));
                stmt.setString(10, a.getLastUpdatedBy());
                stmt.setInt(11, a.getCustomerID());
                stmt.setInt(12, a.getUserID());
                stmt.setInt(13, a.getContactID());

                if(stmt.executeUpdate() == 1) {

                    key = stmt.getGeneratedKeys();
                    key.next();

                    return new Appointment(
                            key.getInt(1),
                            a.getTitle(),
                            a.getDescription(),
                            a.getLocation(),
                            a.getType(),
                            a.getStart(),
                            a.getEnd(),
                            a.getCreateDate(),
                            a.getCreatedBy(),
                            a.getLastUpdate(),
                            a.getLastUpdatedBy(),
                            a.getCustomerID(),
                            a.getUserID(),
                            a.getContactID()
                    );
                }
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return null;
    }

    /**
     * Reads all Appointment records from the database.
     * NOTE: ArrayList could be empty. 1) There are no appointments. 2) There was an exception executing database query.
     * Caller must verify contents of returned ArrayList.
     * @return An ArrayList containing all Appointment records.
     */
    public static ArrayList<Appointment> readAll() {

        var appointments = new ArrayList<Appointment>();

        if(con != null) {

            try(
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery("SELECT * FROM appointments")
            ) {
                while(results.next()) {

                    var appt = new Appointment(
                            results.getInt("Appointment_ID"),
                            results.getString("Title"),
                            results.getString("Description"),
                            results.getString("Location"),
                            results.getString("Type"),
                            TimeUtil.parseUTCZonedDateTime(results.getTimestamp("Start").toString()),
                            TimeUtil.parseUTCZonedDateTime(results.getTimestamp("End").toString()),
                            TimeUtil.parseUTCZonedDateTime(results.getTimestamp("Create_Date").toString()),
                            results.getString("Created_By"),
                            TimeUtil.parseUTCZonedDateTime(results.getTimestamp("Last_Update").toString()),
                            results.getString("Last_Updated_By"),
                            results.getInt("Customer_ID"),
                            results.getInt("User_ID"),
                            results.getInt("Contact_ID"));

                    appointments.add(appt);
                }
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return appointments;
    }

    /**
     * Update an Appointment in the database.
     * @param a The Appointment to update.
     * @return True if the Appointment is successfully updated, false otherwise.
     */
    public static boolean update(Appointment a) {

        if(con != null && a != null) {

            try(
                    PreparedStatement stmt = con.prepareStatement("UPDATE appointments SET " +
                            "Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                            "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                            "WHERE Appointment_ID = ?");
                    ) {

                stmt.setString(1, a.getTitle());
                stmt.setString(2, a.getDescription());
                stmt.setString(3, a.getLocation());
                stmt.setString(4, a.getType());
                stmt.setString(5, TimeUtil.toDateTimeString(a.getStart()));
                stmt.setString(6, TimeUtil.toDateTimeString(a.getEnd()));
                stmt.setString(7, TimeUtil.toDateTimeString(a.getLastUpdate()));
                stmt.setString(8, a.getLastUpdatedBy());
                stmt.setInt(9, a.getCustomerID());
                stmt.setInt(10, a.getUserID());
                stmt.setInt(11, a.getContactID());
                stmt.setInt(12, a.getAppointmentID());

                return stmt.executeUpdate() == 1;
            }catch(SQLException e) {
                System.err.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return false;
    }

    /**
     * Deletes a single Appointment from the database.
     * @param a The Appointment to delete.
     * @return True if the Appointment is successfully deleted, false otherwise.
     */
    public static boolean delete(Appointment a) { return delete(a.getAppointmentID(), "DELETE FROM appointments WHERE Appointment_ID = ?"); }

    /**
     * Deletes all the Appointments associated with a Customer.
     * @param c The Customer whose associated Appointments are to be deleted.
     * @return True if the Appointments are deleted, false otherwise.
     */
    public static boolean deleteAllFor(Customer c) { return delete(c.getCustomerID(), "DELETE FROM appointments WHERE Customer_ID = ?"); }

    private static boolean delete(int id, String query) {

        if(con != null) {
            try (
                    PreparedStatement stmt = con.prepareStatement(query);
            ) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            }catch(SQLException e) {
                System.out.println(e);
            }
        }else {
            System.err.println("No connection to database....");
        }
        return false;
    }
}







