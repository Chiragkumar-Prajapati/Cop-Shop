package com.ctrlaltelite.copshop.logic.services.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtility {
    /**
     * Convert a given date string to a calendar date object
     * @param date String containing the date form fields text data (Format: DD/MM/YEAR HH:MM)
     * @return Calendar object containing the date string data
     */
    public static Calendar convertToDateObj(String date) {

        //initialize cal date to invalid time
        Calendar cal = Calendar.getInstance(Locale.CANADA);
        cal.add(Calendar.DATE, -1);

        if (date != null && !date.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.CANADA);
            try {
                Date dateObj = sdf.parse(date);
                cal = Calendar.getInstance();
                cal.setLenient(false);
                cal.setTime(dateObj);
                try {
                    cal.getTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return cal;
    }
}
