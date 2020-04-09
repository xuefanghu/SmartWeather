package com.groupproject.smartweather.Utils;

import android.content.Context;


public class Preferences {
    private static final String DEFAULT_CITY = "San Jose";
    private static boolean isMetric = false;

    public static String getPreferredWeatherCity(Context context) {
        // TODO: implement the city selection UI and update the value properly.
        return Preferences.DEFAULT_CITY;
    }

    /**
     * Check if the user has selected metric.
     *
     * @return true If metric display should be used
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