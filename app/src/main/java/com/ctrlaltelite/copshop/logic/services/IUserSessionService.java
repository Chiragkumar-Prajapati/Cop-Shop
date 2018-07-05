package com.ctrlaltelite.copshop.logic.services;

public interface IUserSessionService {

    boolean userLoggedIn();

    String getUserEmail();
    String getUserID();
    String getUserType();

    void setUserEmail(String email);
    void setUserID(String id);
    void setUserType(String type);

}
