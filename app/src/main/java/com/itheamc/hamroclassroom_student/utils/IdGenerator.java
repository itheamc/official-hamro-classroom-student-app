package com.itheamc.hamroclassroom_student.utils;

import java.util.Calendar;
import java.util.Locale;

public class IdGenerator {
    /*
   Function to generate id
    */
    public static String generateId() {
        return Calendar
                .getInstance(Locale.ENGLISH)
                .getTime()
                .toString()
                .replace(" ", "")
                .replace(":", "")
                .replace("+", "");
    }
}
