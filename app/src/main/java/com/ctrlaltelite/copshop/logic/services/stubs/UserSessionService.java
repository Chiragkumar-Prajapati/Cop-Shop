package com.ctrlaltelite.copshop.logic.services.stubs;


import com.ctrlaltelite.copshop.logic.services.IUserSessionService;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionService implements IUserSessionService {

    private Context context;
    private SharedPreferences currentUserSession;
    private SharedPreferences.Editor editor;

    public UserSessionService(){
        currentUserSession = context.getSharedPreferences("currentUser", 0);
        editor = currentUserSession.edit();
    }

    public boolean userLoggedIn(){
        boolean loggedIn = true;
        String check = currentUserSession.getString("userID", "-1");

        if(check.equals("-1"))
            loggedIn = false;

        return loggedIn;
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
        String type = currentUserSession.getString("type", "-1");
        if (type.equals("-1"))
            type = null;
        return type;
    }

    public void setUserEmail(String email){
        editor.putString("email", email);
        editor.commit();
    }
    public void setUserID(String id){
        editor.putString("userID", id);
        editor.commit();
    }
    public void setUserType(String type){
        editor.putString("userType", type);
        editor.commit();
    }

}
