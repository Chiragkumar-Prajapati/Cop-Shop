package com.ctrlaltelite.copshop.application;


import android.content.SharedPreferences;

public class UserSession implements IUserSession {

    private SharedPreferences currentUserSession;
    private SharedPreferences.Editor editor;

    public UserSession(){
        currentUserSession = CopShopApp.getAppContext().getSharedPreferences("currentUser", 0);
        editor = currentUserSession.edit();
    }

    public boolean userLoggedIn(){
        String loggedInId = getUserID();
        String loggedInType = getUserType();
        boolean result = null != loggedInId;

        System.out.println("Logged in id:" + loggedInId);
        System.out.println("Logged in type:" + loggedInType);

        if (null != loggedInId) {
            // Verify this user exists in DB
        }

        return result;
    }

    public String getUserEmail(){
        String email = currentUserSession.getString("email", "-1");
            if (email.equals("-1"))
                email = null;
            return email;
    }
    public String getUserID(){
        String userID = currentUserSession.getString("userID", "-1");
        if (userID.equals("-1"))
            userID = null;
        return userID;
    }
    public String getUserType(){
        String type = currentUserSession.getString("userType", "-1");
        if (type.equals("-1"))
            type = null;
        return type;
    }

    public void setUserEmail(String email){
        editor.putString("email", email);
        editor.apply();
    }
    public void setUserID(String id){
        editor.putString("userID", id);
        editor.apply();
    }
    public void setUserType(String type){
        // "buyer" or "seller"
        editor.putString("userType", type);
        editor.apply();
    }

}
