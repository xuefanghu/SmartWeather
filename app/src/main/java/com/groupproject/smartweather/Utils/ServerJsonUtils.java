package com.groupproject.smartweather.Utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Utility functions to handle Weatherbit JSON data.
 */
public final class ServerJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     *
     * @param forecastJsonStr JSON response from server
     * @return Array of Strings describing weather data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */
        final String SW_DATA = "data";

        /* Temperature info is under "main" for the free 5-day forecast API */
        //   final String SW_MAIN = "main";

        /* High and low temperature for the day */
        final String SW_HIGH = "high_temp";
        final String SW_LOW = "low_temp";

        final String SW_WEATHER = "weather";
        final String SW_DESCRIPTION = "description";
        final String SW_TIMESTAMP = "ts";
        // Display the date/time in Pacific Time.
        final ZoneId ZONE_ID = ZoneId.of("America/Los_Angeles");

        /* String array to hold each day's weather String */
        String[] parsedWeatherData = null;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(SW_DATA);

        parsedWeatherData = new String[weatherArray.length()];

        LocalDate today = ZonedDateTime.now(ZONE_ID).toLocalDate();

        for (int i = 0; i < weatherArray.length(); i++) {
            String highAndLow;
            double high;
            double low;
            String description;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            long ts = dayForecast.getLong(SW_TIMESTAMP);
            LocalDate forecastDate = ZonedDateTime.ofInstant(Instant.ofEpochSecond(ts), ZONE_ID)
                    .toLocalDate();
            if (forecastDate.isBefore(today)) {
                continue;
            }
            String dateStr = SWDateUtils.getDayName(today, forecastDate);

            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather code.
             */
            JSONObject weatherObject =
                    dayForecast.getJSONObject(SW_WEATHER);
            description = weatherObject.getString(SW_DESCRIPTION);

            high = dayForecast.getDouble(SW_HIGH);
            low = dayForecast.getDouble(SW_LOW);
            highAndLow = WeatherUtils.formatHighLow(context, high, low);

            parsedWeatherData[i] = dateStr + " - " + description + " - " + highAndLow;
        }

        return parsedWeatherData;
    }

}



