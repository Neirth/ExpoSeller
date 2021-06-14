/*
 * Copyright 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.neirth.exposeller.client.util;

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
