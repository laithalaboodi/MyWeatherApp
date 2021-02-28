package com.laith.preference;

import android.app.Activity;
import android.content.SharedPreferences;

public class LocationPreference {
    SharedPreferences prefs;


    public LocationPreference(Activity activity){
        prefs = activity.getSharedPreferences("setting", Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return prefs.getString("city", "New York");
    }

    public void setCity(String city){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("city", city).apply();
    }

}
