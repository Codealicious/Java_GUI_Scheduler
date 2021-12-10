package com.qam2.utils;

import com.qam2.controller.CustomerController;
import com.qam2.model.Customer;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Singleton class providing an in memory collection of all Customers.
 * Provides CRUD operations keeping in memory collection in sync with database.
 * Provides functions to look-up a Customer by ID or name.
 * @author Alex Hanson
 */
public class CustomerManager {

    private static CustomerManager instance;

    private final ArrayList<Customer> customers;
    private final Map<Integer, Customer> customerIDDictionary;
    private final Map<String, Customer> customerNameDictionary;

    private CustomerManager() {

        customers = CustomerController.readAll();

        customerIDDictionary = customers.stream()
                .collect(Collectors.toMap(Customer::getCustomerID, c -> c));

        customerNameDictionary = customers.stream()
                .collect(Collectors.toMap(Customer::getCustomerName, c -> c));
    }

    /**
     * @return The single instance of CustomerManager.
     */
    public static CustomerManager getInstance() { return instance == null ? (instance = new CustomerManager()) : instance; }

    /**
     * @return A list of all Customer objects.
     */
    public ArrayList<Customer> getCustomers() { return new ArrayList<>(customers); }

    /**
     * @return A list of all customer names.
     */
    public ArrayList<String> getCustomerList() {
        return customers.stream().map(Customer::getCustomerName)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * @param name The name to look-up.
     * @return Customer ID or -1.
     */
    public int getCustomerID(String name) {

        var c = customerNameDictionary.get(name);
        return c != null ? c.getCustomerID() : -1;
    }

    /**
     * @param id The ID to look-up.
     * @return Customer name or empty string.
     */
    public String getCustomerName(int id) {

        var c = customerIDDictionary.get(id);
        return c != null ? c.getCustomerName() : "";
    }

    /**
     * @param cust Customer to add.
     * @return True if Customer was successfully added, false otherwise.
     */
    public boolean add(Customer cust) {

        if(cust != null) {
            var c = CustomerController.add(cust);
            if(c != null) {
                customers.add(c);
                customerIDDictionary.put(c.getCustomerID(), c);
                customerNameDictionary.put(c.getCustomerName(), c);
                return true;
            }
        }
        return false;
    }

    /**
     * @param c Customer to update.
     * @return True if Customer was successsfully updated, false otherwise.
     */
    public boolean update(Customer c) {

        var index = customers.indexOf(c);

        if(c != null && index >= 0) {

            if(CustomerController.update(c)) {
                customers.set(index, c);
                var oldName = customerIDDictionary.get(c.getCustomerID()).getCustomerName();
                customerNameDictionary.remove(oldName);
                customerNameDictionary.put(c.getCustomerName(), c);
                customerIDDictionary.put(c.getCustomerID(), c);
                return true;
            }
        }

        return false;
    }

    /**
     * @param c Customer to delete.
     * @return True if the Customer was successfully deleted, false otherwise.
     */
    public boolean delete(Customer c) {

        if(c != null) {
            if(CustomerController.delete(c)) {
                customers.remove(c);
                customerIDDictionary.remove(c.getCustomerID());
                customerNameDictionary.remove(c.getCustomerName());
                return true;
            }
        }
        return false;
    }


}
