package com.itheamc.hamroclassroom_student.utils;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class IdGenerator {
    /*
   Function to generate id
    */
    public static String generateRandomId() {
        UUID uuid = UUID.randomUUID();
        String uuid_str = uuid.toString();
        String[] split_str = uuid_str.split("-");

        StringBuilder newUUId = new StringBuilder();
        for (String s : split_str) {
            int rand = new Random().nextInt(5);
            if (rand == 1 || rand == 3 || rand == 4) newUUId.append(s.toUpperCase(Locale.ROOT));
            else newUUId.append(s.toLowerCase(Locale.ROOT));
        }

        int rand = new Random().nextInt(10);
        String _id = newUUId.toString();
        _id = _id.substring(rand, rand+20);
        return _id;

    }

    public static String generateIdWithDate() {
        return Calendar
                .getInstance(Locale.ENGLISH)
                .getTime()
                .toString()
                .replace(" ", "")
                .replace(":", "")
                .replace("+", "");
    }
}
