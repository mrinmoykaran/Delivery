package com.codeeratech.freshziiedelivery.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.codeeratech.freshziiedelivery.Activity.LogInActivity;

import java.util.HashMap;

import static com.codeeratech.freshziiedelivery.Config.BaseURL.IS_LOGIN;
import static com.codeeratech.freshziiedelivery.Config.BaseURL.KEY_ID;
import static com.codeeratech.freshziiedelivery.Config.BaseURL.KEY_NAME;
import static com.codeeratech.freshziiedelivery.Config.BaseURL.KEY_PASSWORD;
import static com.codeeratech.freshziiedelivery.Config.BaseURL.PREFS_NAME;
import static com.codeeratech.freshziiedelivery.Config.BaseURL.PREFS_NAME2;
import static com.codeeratech.freshziiedelivery.Config.BaseURL.USER_CURRENCY;
import static com.codeeratech.freshziiedelivery.Config.BaseURL.USER_CURRENCY_CNTRY;


public class Session_management {

    SharedPreferences prefs;
    SharedPreferences prefs2;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;
    Context context;
    int PRIVATE_MODE = 0;

    public Session_management(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();
        prefs2 = context.getSharedPreferences(PREFS_NAME2, PRIVATE_MODE);
        editor2 = prefs2.edit();
    }

    //Store Data
    public void createLoginSession(String id, String name, String password) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, LogInActivity.class);
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
    }

    //Store And Use Data

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, prefs.getString(KEY_ID, null));
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        return user;
    }


    public void logoutSession() {
        editor.clear();
        editor.commit();
        Intent logout = new Intent(context, LogInActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logout);
    }

    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

    public String getCurrency() {
        return prefs.getString(USER_CURRENCY, "");
    }

    public String getCurrencyCountry() {
        return prefs.getString(USER_CURRENCY_CNTRY, "");
    }

    public void setCurrency(String name, String currency) {
        prefs.edit().putString(USER_CURRENCY, currency).apply();
        prefs.edit().putString(USER_CURRENCY_CNTRY, name).apply();
    }

}
