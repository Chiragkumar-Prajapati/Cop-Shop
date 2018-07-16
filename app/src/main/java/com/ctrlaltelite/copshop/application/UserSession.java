package com.ctrlaltelite.copshop.application;


import android.content.SharedPreferences;

import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;

public class UserSession implements IUserSession {
    private final String TYPE_BUYER = "buyer";
    private final String TYPE_SELLER = "seller";
    private final String KEY_EMAIL = "email";
    private final String KEY_ID = "userID";
    private final String KEY_TYPE = "userType";
    private SharedPreferences currentUserSession;
    private SharedPreferences.Editor editor;

    public UserSession() {
        currentUserSession = CopShopApp.getAppContext().getSharedPreferences("currentUser", 0);
        editor = currentUserSession.edit();
    }

    public boolean userLoggedIn() {
        boolean result = false;
        String loggedInId = getUserID();

        System.out.println("Logged in id:" + loggedInId);
        System.out.println("Logged in type:" + getUserType());

        if (null != loggedInId) {
            // Verify this user exists in DB
            if (loggedInUserIsBuyer()) {
                result = null != CopShopHub.getBuyerModel().fetch(loggedInId);
            } else if (loggedInUserIsSeller()) {
                result = null !=CopShopHub.getSellerModel().fetch(loggedInId);
            }
        }

        return result;
    }

    public boolean loginUser(AccountObject user) {
        if (user.getEmail() != null && user.getId() != null && !user.getId().equals("")) {
            setUserEmail(user.getEmail());
            setUserID(user.getId());
            if (user instanceof BuyerAccountObject)
                setUserType(TYPE_BUYER);
            else if (user instanceof SellerAccountObject)
                setUserType(TYPE_SELLER);
            return true;
        } else {
            return false;
        }
    }

    public void logoutUser(){
        setUserEmail(null);
        setUserID(null);
        setUserType(null);
    }

    public boolean loggedInUserIsBuyer() {
        String userType = getUserType();
        return userType.equals(TYPE_BUYER);
    }

    public boolean loggedInUserIsSeller() {
        String userType = getUserType();
        return userType.equals(TYPE_SELLER);
    }

    public String getUserEmail() {
        String email = currentUserSession.getString(KEY_EMAIL, "-1");
            if (email.equals("-1"))
                email = null;
            return email;
    }

    public String getUserID() {
        String userID = currentUserSession.getString(KEY_ID, "-1");
        if (userID.equals("-1"))
            userID = null;
        return userID;
    }

    public String getUserType() {
        String type = currentUserSession.getString(KEY_TYPE, "-1");
        if (type.equals("-1"))
            type = null;
        return type;
    }

    public void setUserEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    private void setUserID(String id) {
        editor.putString(KEY_ID, id);
        editor.apply();
    }

    private void setUserType(String type) {
        // "buyer" or "seller"
        editor.putString(KEY_TYPE, type);
        editor.apply();
    }

}
