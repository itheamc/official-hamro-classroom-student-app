package com.itheamc.hamroclassroom_student.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class NotifyUtils {
    // Function to show Toast message
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Function to show snack Bar
    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    // Function to log debug
    public static void logDebug(String TAG, String message) {
        Log.d(TAG, "logDebug: -- " + message);
    }

    // Function to log debug
    public static void logError(String TAG, String function, Exception e) {
        Log.e(TAG, function + ": -- ", e.getCause());
    }
}
