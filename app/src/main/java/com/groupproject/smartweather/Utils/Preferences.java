package com.groupproject.smartweather.Utils;


public class Preferences {
    private static boolean isMetric = false;
    // The default city if the input city name is empty and the GPS is not available either.
    public final static String DEFAULT_CITY = "San Jose, CA";
    // TODO: default to GPS location once ready.
    private static Location location = new Location(DEFAULT_CITY);

    // Data structure to hold the weather location chosen by the user.
    public static class Location {
        // The location in string format.
        public String locationStr;

        // The location in lat/lng format.
        public double lat;
        public double lng;

        public Location(String locationStr) {
            this.locationStr = locationStr;
        }

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
            this.locationStr = "";
        }
    }

    /**
     * Get the user input location.
     * @return the location.
     */
    public static Location getPreferredLocation() {
        return location;
    }

    /**
     * Set the user inut location.
     * @param value
     */
    public static void setPreferredLocation(Location value) {
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