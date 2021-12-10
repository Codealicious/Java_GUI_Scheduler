package com.qam2.model;

/**
 * Immutable class representing a User record.
 * @author Alex Hanson
 */
public final class User {

    private final int userID;
    private final String userName;
    private final String password;

    /**
     * Creates an obj representing a User record. Only database record columns of concern are: User_ID, Username, Password.
     * @param userID User ID.
     * @param userName Username.
     * @param password User's password.
     */
    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }

    /**
     * @return The User's ID.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @return The username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return The User's password.
     */
    public String getPassword() {
        return password;
    }

}
