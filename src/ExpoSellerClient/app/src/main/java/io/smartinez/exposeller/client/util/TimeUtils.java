package io.smartinez.exposeller.client.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class to manage quickly the date objects
 */
public class TimeUtils {
    /**
     * Method to remove the hour and minutes from date object
     *
     * @param date The date object
     * @return The date object without time
     */
    public static Date removeTimeFromDate(Date date) {
        // Get a new calendar instance
        Calendar cal = Calendar.getInstance();

        // Set the date object
        cal.setTime(date);

        // Remove the hour and minute
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // Fix the time zone
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Return a date object
        return cal.getTime();
    }

    /**
     * Method to set the end of day in object date
     *
     * @param date The date object
     * @return The date object modified
     */
    public static Date endOfDay(Date date) {
        // Get a calendar instance
        Calendar cal = Calendar.getInstance();

        // Set the date object
        cal.setTime(date);

        // Remove the hour and minute
        cal.set(Calendar.HOUR_OF_DAY,  23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        // Fix the time zone
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Return a date object
        return cal.getTime();
    }

    /**
     * Method to change to correct timezone
     *
     * @param date The date object
     * @return The date object modified
     */
    public static Date fixTimezone(Date date) {
        // Get a calendar instance
        Calendar cal = Calendar.getInstance();

        // Set the date object
        cal.setTime(date);

        // Fix the time zone
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Return a date object
        return cal.getTime();
    }
}
