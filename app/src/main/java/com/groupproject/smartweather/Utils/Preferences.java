package com.groupproject.smartweather.Utils;


// Save the user settings from the SETTINGS page such as preferred location etc.
public class Preferences {
    private static boolean isMetric = false;
    // The default city if the input city name is empty and the GPS is not available either.
    public final static String DEFAULT_CITY = "San Jose, CA";
    // The user input location.
    private static String location = "";

    /**
     * Get the user input location.
     * @return the location.
     */
    public static String getPreferredLocation() {
        return location;
    }

    /**
     * Set the user inut location.
     * @param value
     */
    public static void setPreferredLocation(String value) {
        location = value;
    }

    /**
     * Check if the user has selected metric.
     *
     * @return true if metric display should be used
     */
    public static boolean getIsMetric() {
        return isMetric;
    }

    /**
     * Set the user's selection.
     * @param value
     */
    public static void setIsMetric(boolean value) {
        isMetric = value;
    }
}