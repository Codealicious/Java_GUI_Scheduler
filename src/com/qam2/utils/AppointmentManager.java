package com.qam2.utils;

import com.qam2.controller.AppointmentController;
import com.qam2.model.Appointment;
import com.qam2.model.Customer;
import com.qam2.utils.time.TimeUtil;
import com.qam2.utils.time.TimeZone;

import java.time.*;
import java.util.ArrayList;

/**
 * Singleton class providing an in memory collection of all Appointments.
 * Provides CRUD operations keeping in memory collection in sync with database.
 * Provides static functions to help facilitate appointment creation and update without overbooking a user, customer, or contact.
 * Provides instance functions to determine whether a Customer has any Appointments and whether or not a logged-in user has an upcoming appointment.
 * Provides instance functions to get Appointments for current: year, month, week, day, customer, user, contact.
 * @author Alex Hanson
 */
public class AppointmentManager {

    public static final ZonedDateTime BUSINESS_HR_OPEN_EST;
    public static final ZonedDateTime BUSINESS_HR_CLOSE_EST;
    public static final ZonedDateTime BUSINESS_HR_OPEN_LOCAL;
    public static final ZonedDateTime BUSINESS_HR_CLOSE_LOCAL;
    private static final int MAX_APPOINTMENTS;
    private static final int APPOINTMENT_REMINDER;
    private static final ArrayList<String> ALL_TIMES;
    private static AppointmentManager instance;

    private final ArrayList<Appointment> appointments;

    static {

        LocalDate todayEST = LocalDate.now(TimeZone.EST.getID());
        LocalTime openEST = LocalTime.of(8,0);
        LocalTime closeEST = LocalTime.of(22,0);

        BUSINESS_HR_OPEN_EST = ZonedDateTime.of(LocalDateTime.of(todayEST, openEST), TimeZone.EST.getID());
        BUSINESS_HR_CLOSE_EST = ZonedDateTime.of(LocalDateTime.of(todayEST, closeEST), TimeZone.EST.getID());

        BUSINESS_HR_OPEN_LOCAL = TimeUtil.convertTime(BUSINESS_HR_OPEN_EST, TimeZone.LOCAL);
        BUSINESS_HR_CLOSE_LOCAL = TimeUtil.convertTime(BUSINESS_HR_CLOSE_EST, TimeZone.LOCAL);

        MAX_APPOINTMENTS = ((BUSINESS_HR_CLOSE_EST.getHour() - BUSINESS_HR_OPEN_EST.getHour()) * 4) - 1;
        APPOINTMENT_REMINDER = 15;
        ALL_TIMES = buildTimesList();
    }

    private AppointmentManager() {
        appointments = AppointmentController.readAll();
    }

    private static ArrayList<String> buildTimesList() {

        LocalTime zt = BUSINESS_HR_OPEN_LOCAL.toLocalTime();
        var times = new ArrayList<String>();
        times.add(TimeUtil.toTimeString(zt));

        for(int i = 0, index = 1; i < MAX_APPOINTMENTS; i++, index++) {

            zt = zt.plusMinutes(15);

            if(zt.equals(LocalTime.MIDNIGHT))
                index = 0;

            times.add(index, TimeUtil.toTimeString(zt));

        }

        return times;
    }


    private static boolean timeDifference(LocalTime start) {

        LocalTime lt = LocalTime.now();
        int startHour = start.getHour();
        int startMinute = start.getMinute();
        int currentHour = lt.getHour();
        int currentMinute = lt.getMinute();

        if(startHour == currentHour) {

            int difference = startMinute - currentMinute;

            return difference > 0 && difference <= APPOINTMENT_REMINDER;

        }else if(startHour == (currentHour + 1) && startMinute == 0) {

            return (60 - currentMinute) <= APPOINTMENT_REMINDER;

        }

        return false;
    }

    private static boolean checkYear(Appointment a, int year) {
        return a.getLocalStartDate().getYear() == year;
    }

    private static boolean checkMonth(Appointment a, int month) {
        return a.getLocalStartDate().getMonthValue() == month;
    }

    private static void removeScheduledTimes(ArrayList<Appointment> appointments, ArrayList<String> availableTimes, Appointment appt) {

        for(Appointment a : appointments) {

            LocalTime lt = a.getLocalStartTime();

            String start = a.getLocalStartString();
            String end = a.getLocalEndString();

            if(!a.equals(appt)) {
                while (start.compareTo(end) != 0) {
                    availableTimes.remove(start);
                    lt = lt.plusMinutes(15);
                    start = TimeUtil.toTimeString(lt);
                }
            }
        }
    }

    /**
     * @return Single instance of AppointmentManager.
     */
    public static AppointmentManager getInstance() { return instance == null ? (instance = new AppointmentManager()) : instance; }

    /**
     * Gets all Appointments for given year value.
     * Uses Stream API and lambda expression to filter list of Appointments into a list of those for the given year value.
     * The intermediate stream operation filter() takes an implementation of the functional interface Predicate&lt;T&gt;
     * as a parameter. A lambda expression was used to implement Predicate&lt;T&gt; anonymously by providing
     * an implementation of the single abstract method public boolean test(T t1, T t2).
     * @param appts List of Appointments to filter.
     * @param year Year by which to filter list of Appointments.
     * @return ArrayList of Appointments for the given year value.
     */
    public static ArrayList<Appointment> filterAppointmentsByYear(ArrayList<Appointment> appts, int year) {
        return appts.stream()
                .filter(a -> checkYear(a, year))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Gets all Appointments for a given month value.
     * @param appts List of Appointments to filter.
     * @param month Month by which to filter list of Appointments.
     * @return ArrayList of Appointments for the given month value.
     */
    public static ArrayList<Appointment> filterAppointmentsByMonth(ArrayList<Appointment> appts, int month) {
        return appts.stream()
                .filter(a -> checkMonth(a, month))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Gets all Appointments for a given week.
     * @param appts List of Appointments to filter.
     * @param lt The date by which the week is determined, The last Sunday from the given date
     * @return ArrayList of Appointments for a given week.
     */
    public static ArrayList<Appointment> filterAppointmentsByWeek(ArrayList<Appointment> appts, LocalDate lt) {

        var apptsByWeek = new ArrayList<Appointment>();

        if(lt.getDayOfWeek().compareTo(DayOfWeek.SUNDAY) != 0) {

            while(lt.getDayOfWeek().compareTo(DayOfWeek.MONDAY) != 0) {
                lt = lt.minusDays(1);
            }

            lt = lt.minusDays(1);
        }

        for(int i = 0; i < 7; i++) {
            apptsByWeek.addAll(filterAppointmentsByDate(appts, lt));
            lt = lt.plusDays(1);
        }

        return apptsByWeek;
    }

    /**
     * Gets all Appointments for a given date.
     * @param appts List of Appointments to filter.
     * @param date The date by which to filter the list.
     * @return ArrayList of Appointments for given date.
     */
    public static ArrayList<Appointment> filterAppointmentsByDate(ArrayList<Appointment> appts, LocalDate date) {
        return appts.stream()
                .filter(a -> a.getLocalStartDate().compareTo(date) == 0)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get all available Appointment start times for a given set of: customer, contact, user, and date.
     * If an Appointment reference is provided, gets all available times to which the appointment can be changed.
     * @param customerName Name of customer.
     * @param contactName Name of company contact.
     * @param userName Name of user.
     * @param date The Appointment date.
     * @param appt If not null, the Appointment for which all possible times to change to will be calculated.
     * @return ArrayList of Strings representing all possible start times for a new or given Appointment.
     */
    public static ArrayList<String> getAvailableStartTimes(String customerName, String contactName, String userName, LocalDate date, Appointment appt) {

        var availableTimes = new ArrayList<>(ALL_TIMES);

        var c_appts = AppointmentManager.getInstance().getAppointmentsForCustomer(CustomerManager.getInstance().getCustomerID(customerName));
        var u_appts = AppointmentManager.getInstance().getAppointmentsForUser(UserManager.getUserID(userName));
        var ct_appts = AppointmentManager.getInstance().getAppointmentsForContact(ContactManager.getContactID(contactName));

        if(c_appts != null) {
            c_appts = filterAppointmentsByDate(c_appts, date);
            removeScheduledTimes(c_appts, availableTimes, appt);
        }

        if(u_appts != null) {
            u_appts = filterAppointmentsByDate(u_appts, date);
            removeScheduledTimes(u_appts, availableTimes, appt);
        }

        if(ct_appts != null) {
            ct_appts = filterAppointmentsByDate(ct_appts, date);
            removeScheduledTimes(ct_appts, availableTimes, appt);
        }

        return availableTimes;
    }

    /**
     * Provides an ArrayList of Strings representing the possible end times for a selected start time.
     * @param availableTimes The list of times from which the given start time was selected and possible end times are to be determined.
     * @param start A start time selected from the given list of available times.
     * @return ArrayList of Strings representing all possible end times for given start time.
     */
    public static ArrayList<String> getAvailableEndTimes(ArrayList<String> availableTimes, String start) {

        ArrayList<String> endTimes = new ArrayList<>();

        var lt = TimeUtil.parseLocalTime(start);

        String endTime;

        do {
            lt = lt.plusMinutes(15);
            endTime = TimeUtil.toTimeString(lt);
            endTimes.add(endTime);
        }while(availableTimes.contains(endTime));

        return endTimes;
    }

    /**
     * Adds an Appointment to the Manager's list of Appointments and to the database.
     * @param appointment The Appointment to add.
     * @return True if Appointment was successfully added, false otherwise.
     */
    public boolean add(Appointment appointment) {

        if(appointment != null) {
            var a = AppointmentController.add(appointment);
            if(a != null) {
                appointments.add(a);
                return true;
            }
        }
        return false;
    }

    /**
     * Updates an Appointment in the Manger's list of Appointments and the database.
     * @param appointment The Appointment to update.
     * @return True if the Appointment was successfully updated, false otherwise.
     */
    public boolean update(Appointment appointment) {

        if(appointment != null) {
            var index = appointments.indexOf(appointment);
            if(index > 0 && AppointmentController.update(appointment)) {
                appointments.set(index, new Appointment(appointment));
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a single Appointment from the Manager's list of Appointments and the database.
     * @param appointment The Appointment to delete.
     * @return True if the Appointment was successfully deleted, false otherwise.
     */
    public boolean delete(Appointment appointment) {

        if(appointment != null && AppointmentController.delete(appointment)) {
            appointments.remove(appointment);
            return true;
        }
        return false;
    }

    /**
     * Deletes all Appointments for a given Customer from the Manager's list and the database.
     * Precondition: Customer must have Appointments, consider using hasAppointments() before calling this method.
     * @param c The Customer for whom Appointments are to be deleted.
     * @return True if the Customer's Appointments were successfully deleted, false otherwise.
     */
    public boolean deleteAllForCustomer(Customer c) {

        if(c != null && AppointmentController.deleteAllFor(c)) {

            ArrayList<Appointment> appts = getAppointmentsForCustomer(c.getCustomerID());

            for(Appointment a : appts)
                appointments.remove(a);

            return true;
        }
        return false;
    }

    /**
     * @return A copy of the Manager's list of all appointments.
     */
    public ArrayList<Appointment> getAppointments() { return new ArrayList<>(appointments); }

    /**
     * Gets all Appointments for a given Customer ID.
     * @param id Customer ID.
     * @return ArrayList of Appointments for the given Customer.
     */
    public ArrayList<Appointment> getAppointmentsForCustomer(int id) {
        return appointments.stream()
                .filter(a -> a.getCustomerID() == id)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Gets all Appointments for a given User ID.
     * @param id User ID.
     * @return ArrayList of Appointments for the given User.
     */
    public ArrayList<Appointment> getAppointmentsForUser(int id) {
        return appointments.stream()
                .filter(a -> a.getUserID() == id)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Gets all Appointments for a given Contact ID.
     * @param id Contact ID.
     * @return ArrayList of Appointments for the given Contact.
     */
    public ArrayList<Appointment> getAppointmentsForContact(int id) {
        return appointments.stream()
                .filter(a -> a.getContactID() == id)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * @return ArrayList of all Appointments for the current year.
     */
    public ArrayList<Appointment> getAppointmentsForCurrentYear() {
        return filterAppointmentsByYear(appointments, LocalDate.now().getYear());
    }

    /**
     * @return ArrayList of all Appointments for the current month.
     */
    public ArrayList<Appointment> getAppointmentsForCurrentMonth() {
        return filterAppointmentsByMonth(appointments, LocalDate.now().getMonthValue());
    }

    /**
     * @return ArrayList of all Appointments for the current week.
     */
    public ArrayList<Appointment> getAppointmentsForCurrentWeek() {
        return filterAppointmentsByWeek(appointments, LocalDate.now());
    }

    /**
     * @return ArrayList of all Appointments for the current day.
     */
    public ArrayList<Appointment> getAppointmentsForCurrentDay() {
        return filterAppointmentsByDate(appointments, LocalDate.now());
    }

    /**
     * Determines whether a given Customer has Appointments.
     * @param c The Customer.
     * @return True if the Customer has Appointments, false otherwise.
     */
    public boolean hasAppointments(Customer c) {

        var c_appts = getAppointmentsForCustomer(c.getCustomerID());

        if(c_appts != null)
            return c_appts.size() > 0;

        return false;
    }

    /**
     * Determines whether the logged in user has an upcoming Appointment based on Appointment reminder value.
     * @return True if the logged-in User has an appointment within the time range specified by reminder value, false otherwise.
     */
    public Appointment upcomingAppointment() {

        int id = UserManager.getCurrentUserID();

        ArrayList<Appointment> appts = getAppointmentsForCurrentDay().stream()
                                                .filter(a -> a.getUserID() == id)
                                                .filter(a -> timeDifference(a.getLocalStartTime()))
                                                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        return appts.size() > 0 ? appts.get(0) : null;
    }
}
