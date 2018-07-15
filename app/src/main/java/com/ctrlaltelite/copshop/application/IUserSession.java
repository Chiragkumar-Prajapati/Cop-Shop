package com.ctrlaltelite.copshop.application;

import com.ctrlaltelite.copshop.objects.AccountObject;

public interface IUserSession {

    /* Check if there is currently a user logged in*/
    boolean userLoggedIn();

    //getters
    String getUserEmail();
    String getUserID();
    String getUserType();

    /**
     * @param user    The current user getting logged in to app
     */
    boolean loginUser(AccountObject user);

    /**
     * Log out the user by removing all current session infomation
     */
    void logoutUser();

    /**
     * @return True if logged in user is buyer, false in all other cases
     */
    boolean loggedInUserIsBuyer();

    /**
     * @return True if logged in user is seller, false in all other cases
     */
    boolean loggedInUserIsSeller();

    /**
     * @param email The email of the current user logged in to app
     */
    void setUserEmail(String email);

}
