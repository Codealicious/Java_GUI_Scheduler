package com.qam2.utils;

import com.qam2.controller.UserController;
import com.qam2.model.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides static methods to authenticate a user and retrieve user information.
 * Provides static methods to authenticate a user, get current username and id, look-up a user id by username,
 * look-up a username by id, logout current user, and get a list of all users.
 * @author Alex Hanson
 */
public abstract class UserManager {

    private static User currentUser;
    private static final ArrayList<User> users;
    private static final Map<Integer, User> userIDDictionary;
    private static final Map<String, User> userNameDictionary;

    static {
        users = UserController.readAll();

        userIDDictionary = users.stream()
                              .collect(Collectors.toMap(User::getUserID, u -> u));

        userNameDictionary = users.stream()
                .collect(Collectors.toMap(User::getUserName, u -> u));
    }

    /**
     * @param name Username to authenticate.
     * @param pswd Password to authenticate.
     * @return True if username and password match a valid user, false otherwise.
     */
    public static boolean authenticate(String name, String pswd) {

        User user = UserController.read(name);

        if(user != null && user.getPassword().compareTo(pswd) == 0) {
            currentUser = user;
            return true;
        }
        return false;
    }

    /**
     * Logout current user.
     */
    public static void logout() { currentUser = null; }

    /**
     * @return The current User's username.
     */
    public static String getCurrentUserName() {
        return currentUser != null ? currentUser.getUserName() : "";
    }

    /**
     * @return The current User's ID.
     */
    public static int getCurrentUserID() {
        return currentUser != null ? currentUser.getUserID() : -1;
    }

    /**
     * Uses Stream API and method reference to filter User names out of an ArrayList of User objects.
     * The intermediate stream operation map() takes an implementation of the functional interface Function&lt;T, R&gt;
     * as a parameter. A method reference User::getUserName was used to implement the single abstract method R apply(T).
     * The parameter type T was deduced by the stream to be a User object, while return type R is the return type of
     * getUserName() which is String.
     * The collect terminal operation on the stream uses method references to create an ArrayList and provide
     * methods for adding each item of the stream to ArrayList as well as combine results from possible parallel
     * operations on the same stream.
     * @return List of all usernames.
     */
    public static ArrayList<String> getUserNameList() {
        return new ArrayList<>(
                users.stream()
                        .map(User::getUserName)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll)
        );
    }

    /**
     * @param name The username for which to find an ID.
     * @return The ID for a given username or -1.
     */
    public static int getUserID(String name) {
        var u = userNameDictionary.get(name);
        return u != null ? u.getUserID() : -1;
    }

    /**
     * @param id The ID for which to find a username.
     * @return The username for a given ID or empty String.
     */
    public static String getUserName(int id) {
        var u = userIDDictionary.get(id);
        return u != null ?  u.getUserName() : ""; }
}
