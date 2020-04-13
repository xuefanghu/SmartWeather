package com.groupproject.smartweather.Utils;


import android.content.Context;
import android.content.SharedPreferences;

// Write/read the user settings to/from SharedPreferences, which is a persistent storage.
public class Preferences {
    private final static String SHARED_PREFERENCES_NAME = "WeatherSharedPref";
    private final static String LOCATION_KEY = "preferred_location";
    private final static String METRIC_KEY = "is_metric";
    // The default city if the input city name is empty and the GPS is not available either.
    public final static String DEFAULT_CITY = "San Jose, CA";

    // Helper function to access the SharedPreferences.
    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Get the user input location.
     * @return the location.
     */
    public static String getPreferredLocation(Context context) {
        return getPreference(context).getString(LOCATION_KEY, "");
    }

    /**
     * Set the user inut location.
     * @param value
     */
    public static void setPreferredLocation(Context context, String value) {
        getPreference(context).edit().putString(LOCATION_KEY, value).commit();
    }

    /**
     * Check if the user has selected metric.
     *
     * @return true if metric display should be used
     */
    public static boolean getIsMetric(Context context) {
        return getPreference(context).getBoolean(METRIC_KEY, false);
    }

    /**
     * Set the user's selection.
     * @param value
     */
    public static void setIsMetric(Context context, boolean value) {
        getPreference(context).edit().putBoolean(METRIC_KEY, value).commit();
    }
}