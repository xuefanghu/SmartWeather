package com.groupproject.smartweather.Utils;

import android.location.Location;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * Used to call the online weather server (Weatherbit).
 */
public final class NetworkUtils {
    final static String COUNTRY_PARAM = "country";
    final static String LAT_PARAM = "lat";
    final static String LNG_PARAM = "lon";
    final static String CITY_PARAM = "city";
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "days";
    final static String APIKEY_PARAM = "key";
    final static String COUNTRY_VALUE = "US";
    final static String APIKEY_VALUE = "8b25f989a3524c91a2674c2fd8f4cd09";
    // Use the 16-day forecast API from Weatherbit.
    private static final String FORECAST_BASE_URL = "https://api.weatherbit.io/v2.0/forecast/daily";
    // The units we want our API to return
    private static final String units = "metric";
    // The number of days we want our API to return
    private static final int numDays = 15;

    /**
     * Builds the URL used to communicate with the weather server using a location.
     *
     * @param locationStr The location string entered by the user.
     * @param currentLocation The current location from the device GPS.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String locationStr, Location currentLocation) {
        // return the URL used to query Weatherbit's API
        Uri.Builder builder = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .appendQueryParameter(APIKEY_PARAM, APIKEY_VALUE);
        if (locationStr.length() > 0) {
            // Case of city name from the user input.
            builder.appendQueryParameter(CITY_PARAM, locationStr);
        } else if (currentLocation != null){
            // Case of lat/lng from the device GPS.
            builder.appendQueryParameter(LAT_PARAM, Double.toString(currentLocation.getLatitude()))
                    .appendQueryParameter(LNG_PARAM, Double.toString(currentLocation.getLongitude()));
        } else {
            // No location available.
            builder.appendQueryParameter(CITY_PARAM, Preferences.DEFAULT_CITY);
        }
        try {
            return new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getDataFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}