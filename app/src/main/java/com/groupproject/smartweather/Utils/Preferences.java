package com.groupproject.smartweather.Utils;

import android.content.Context;


public class Preferences {
    private static final String DEFAULT_CITY = "San Jose";

    public static String getPreferredWeatherCity(Context context) {
        // TODO: implement the city selection UI and update the value properly.
        return Preferences.DEFAULT_CITY;
    }

    /**
     * Returns true if the user has selected metric.
     *
     * @param context Context used to get the value
     * @return true If metric display should be used
     */
    public static boolean isMetric(Context context) {
        // TODO: implement the metric selection UI and update the value properly.
        return false;
    }


}