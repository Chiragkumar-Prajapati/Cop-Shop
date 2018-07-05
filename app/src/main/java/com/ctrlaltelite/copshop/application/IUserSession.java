package com.ctrlaltelite.copshop.application;

public interface IUserSession {

    /* Check if there is currently a user logged in*/
    boolean userLoggedIn();

    //getters
    String getUserEmail();
    String getUserID();
    String getUserType();

    /**
     *
     * @param email The email of the current user logged in to app
     */
    void setUserEmail(String email);

    /**
     *
     * @param id The user ID of the current user logged in to app
     */
    void setUserID(String id);

    /**
     *
     * @param type The type of user currently logged in to app
     */
    void setUserType(String type);

}
