package com.groupproject.smartweather.Utils;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

// Write/read the user settings to/from SharedPreferences, which is a persistent storage.
public class Preferences {
    // The default city if the input city name is empty and the GPS is not available either.
    public final static String DEFAULT_CITY = "San Jose, CA";
    private final static String SHARED_PREFERENCES_NAME = "WeatherSharedPref";
    private final static String LOCATION_KEY = "preferred_location";
    private final static String METRIC_KEY = "is_metric";
    private final static String DISTANCE_KEY = "distance";
    private final static String USER_ID_KEY = "user_id";

    // Helper function to access the SharedPreferences.
    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Get the user input location.
     *
     * @param context
     * @return the location.
     */
    public static String getPreferredLocation(Context context) {
        return getPreference(context).getString(LOCATION_KEY, "");
    }

    /**
     * Set the user inut location.
     *
     * @param context
     * @param value
     */
    public static void setPreferredLocation(Context context, String value) {
        getPreference(context).edit().putString(LOCATION_KEY, value).commit();
    }

    /**
     * Check if the user has selected metric.
     *
     * @param context
     * @return true if metric display should be used
     */
    public static boolean getIsMetric(Context context) {
        return getPreference(context).getBoolean(METRIC_KEY, false);
    }

    /**
     * Set the user's selection.
     *
     * @param context
     * @param value
     */
    public static void setIsMetric(Context context, boolean value) {
        getPreference(context).edit().putBoolean(METRIC_KEY, value).commit();
    }

    /**
     * Save the distance between the user location and the city location (in meter).
     * This value will be used to calculate geographically weighted average of the sentiment.
     *
     * @param context
     * @param value
     */
    public static void setDistance(Context context, float value) {
        getPreference(context).edit().putFloat(DISTANCE_KEY, value).commit();
    }

    /**
     * Get the distance between the user location and the city location (in meter).
     *
     * @param context
     * @return
     */
    public static float getDistance(Context context) {
        return getPreference(context).getFloat(DISTANCE_KEY, -1);
    }

    /**
     * Get the unique user id, which will be used to identify the user in weather rating.
     *
     * @param context
     * @return the unique user id.
     */
    public static String getMyUserId(Context context) {
        SharedPreferences pref = getPreference(context);
        String id = pref.getString(USER_ID_KEY, null);
        if (id == null) {
            // Generate a random id for this user and persist it in SharedPreferences.
            id = UUID.randomUUID().toString();
            pref.edit().putString(USER_ID_KEY, id).commit();
        }
        return id;
    }
}