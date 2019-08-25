package com.senjacreative.kiwarichat.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    public static final String SESSION_NAME = "KiwariUser";
    public static final String NAME = "userNama";
    public static final String EMAIL = "userEmail";
    public static final String PASSWORD = "userPassword";
    public static final String AVATAR = "userAvatar";
    public static final String LOGGEDIN = "userLogin";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Session(Context context){
        sharedPreferences = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveStringSession(String keySP, String value){
        editor.putString(keySP, value);
        editor.commit();
    }

    public void saveBooleanSession(String keySP, Boolean value){
        editor.putBoolean(keySP, value);
        editor.commit();
    }

    public String getName(){
        return sharedPreferences.getString(NAME, "");
    }

    public String getEmail(){
        return sharedPreferences.getString(EMAIL, "");
    }

    public String getAvatar(){
        return sharedPreferences.getString(AVATAR, "");
    }

    public String getPassword(){
        return sharedPreferences.getString(PASSWORD, "");
    }

    public Boolean getLoggedin(){
        return sharedPreferences.getBoolean(LOGGEDIN, false);
    }
}
