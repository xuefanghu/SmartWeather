package com.groupproject.smartweather.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public final class SWDateUtils {
    public static final DateTimeFormatter DAY_OF_WEEK_FORMAT = DateTimeFormatter.ofPattern("EEEE");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEE, MMM dd");

    /**
     * Given a day, returns the name for that day.
     *   E.g "today", "tomorrow", "Wednesday".
     * @param today The date of today
     * @param forecastDate The forecast date
     *
     * @return the string day of the week
     */
    public static String getDayName(LocalDate today, LocalDate forecastDate) {
        // If the date is today, return the localized version of "Today"
        long diff = forecastDate.toEpochDay() - today.toEpochDay();
        if (diff == 0) {
            return "Today";
        } else if (diff == 1) {
            return "Tomorrow";
        } else if (diff < 7) {
            // If the day is within a week, the format is the day of the week (e.g "Wednesday").
            return forecastDate.format(DAY_OF_WEEK_FORMAT);
        } else {
            // Otherwise show the date.
            return forecastDate.format(DATE_FORMAT);
        }
    }
}