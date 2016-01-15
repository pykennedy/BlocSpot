package com.example.peter.blocspot.geofencing;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHandler {

    private static final String PREFS = "prefs";

    public static void setNotifyIsOn(Context context, boolean bool) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("notifyIsOn", bool);
        editor.apply();
    }

    public static boolean getNotifyIsOn(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return preferences.getBoolean("notifyIsOn", true);
    }

}