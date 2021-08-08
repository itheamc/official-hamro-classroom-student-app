package com.itheamc.hamroclassroom_student.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OtherUtils {

    public static String showGreeting() {
        int time = Calendar.getInstance(Locale.ENGLISH).get(Calendar.HOUR_OF_DAY);
        String message = "";

        if (time > 3 && time < 12) {
            message = "Good Morning";
        } else if (time > 11 && time < 17) {
            message = "Good Afternoon";
        } else if (time > 16 && time < 20) {
            message = "Good Evening";
        } else {
            message = "Good Night";
        }

        return message;
    }


    /**
     * Function to find the difference between two days
     */
    // Function to calc date diff
    public static long timeDifference(long past_time_in_milliseconds) {
        long recent_time = new Date().getTime();
        long mills_diff = recent_time - past_time_in_milliseconds;
        return mills_diff / 1000;
    }
}
