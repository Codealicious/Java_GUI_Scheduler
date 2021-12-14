package com.qam2.utils;

import com.qam2.controller.ContactController;
import com.qam2.model.Contact;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides static functions to get Contact data.
 * @author Alex Hanson
 */
public abstract class ContactManager {

    private static final ArrayList<Contact> contacts;
    private static final Map<String, Contact> contactNameDictionary;
    private static final Map<Integer, Contact> contactIDDictionary;

    static {
        contacts = ContactController.readAll();
        contactNameDictionary = contacts.stream().collect(Collectors.toMap(Contact::getContactName, c -> c));
        contactIDDictionary = contacts.stream().collect(Collectors.toMap(Contact::getContactID, c -> c));
    }

    /**
     * Uses Stream API and method references to filter Contact names out of an ArrayList of Contacts.
     * The intermediate stream operation map() takes an implementation of the functional interface Function&lt;T, R&gt;
     * as a parameter. A method reference Contact::getContactName was used to implement the single abstract method R apply(T).
     * The parameter type T was deduced by the stream to be a Contact object, while return type R is the return type of
     * getContactName() which is String.
     * The collect terminal operation on the stream uses method references to create an ArrayList and provide
     * methods for adding each item of the stream to ArrayList as well as combine results from possible parallel
     * operations on the same stream.
     * @return A list of all Contact names.
     */
    public static ArrayList<String> getContactList() {
        return contacts.stream().map(Contact::getContactName)
                                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * @param id Contact ID.
     * @return Contact name if Contact exists, empty String otherwise.
     */
    public static String getContactName(int id) {

        var c = contactIDDictionary.get(id);
        return c != null ? c.getContactName() : "";
    }

    /**
     * @param name Contact name.
     * @return Contact ID if Contact exists, -1 otherwise.
     */
    public static int getContactID(String name) {

        var c = contactNameDictionary.get(name);
        return c != null ? c.getContactID() : -1;
    }
}
