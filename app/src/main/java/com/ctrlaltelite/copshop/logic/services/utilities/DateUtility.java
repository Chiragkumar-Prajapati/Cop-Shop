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

        //initialize cal as invalid
        Calendar cal = null;

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
                    cal = null; //date obj is invalid
                }
            } catch (ParseException e) {
                cal = null; //date obj is invalid
            }
        }
        return cal;
    }
}
