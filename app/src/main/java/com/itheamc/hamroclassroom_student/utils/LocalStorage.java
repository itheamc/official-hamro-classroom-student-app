package com.itheamc.hamroclassroom_student.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.itheamc.hamroclassroom_student.models.User;

public class LocalStorage {
    private static final String TAG = "LocalStorage";
    private static LocalStorage instance;
    private SharedPreferences sharedPreferences;


    /**
     * ----------------------------------------------------------------------
     * ---------------------- Constructor for Local Storage------------------
     *
     * @param activity -> instance of the calling class activity
     */
    // Constructor
    public LocalStorage(@NonNull Activity activity) {
        this.sharedPreferences = activity.getSharedPreferences("MERO-CLASSROM", Context.MODE_PRIVATE);
    }

    // instance for Local Storage
    public static LocalStorage getInstance(@NonNull Activity activity) {
        return new LocalStorage(activity);
    }


    /**
     * -------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------
     * ----------------------- THESE ARE THE FUNCTIONS FOR LOCAL STORAGE--------------------------
     * -------------------------------------------------------------------------------------------
     */

    // Function to store User ID
    public void storeUserId(String _uid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", _uid);
        editor.apply();
    }

    // Function to get User Id
    public String getUserId() {
        return sharedPreferences.getString("uid", null);
    }

    // Function to checked if user is already registered
    public boolean isLoggedIn() {
        return sharedPreferences.getString("uid", null) != null;
    }
}
