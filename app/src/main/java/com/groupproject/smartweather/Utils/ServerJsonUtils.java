package com.groupproject.smartweather.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility functions to handle Weatherbit JSON data.
 */
public final class ServerJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     *
     * @param forecastJsonStr JSON response from server
     * @return List of DailyWeatherInfo representing weather data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<DailyWeatherInfo> getSimpleWeatherStringsFromJson(String forecastJsonStr)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */
        final String SW_DATA = "data";
        final String SW_CITY_NAME = "city_name";

        /* Temperature info is under "main" for the free 5-day forecast API */
        //   final String SW_MAIN = "main";

        /* High and low temperature for the day */
        final String SW_HIGH = "high_temp";
        final String SW_LOW = "low_temp";

        final String SW_WEATHER = "weather";
        final String SW_DESCRIPTION = "description";
        final String SW_ICON_ID = "icon";
        final String SW_TIMESTAMP = "ts";
        // Display the date/time in Pacific Time.
        final ZoneId ZONE_ID = ZoneId.of("America/Los_Angeles");

        /* DailyWeatherInfo list to hold each day's weather info */
        List<DailyWeatherInfo> parsedWeatherData = new LinkedList<>();

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(SW_DATA);
        String cityName = forecastJson.getString(SW_CITY_NAME);

        LocalDate today = ZonedDateTime.now(ZONE_ID).toLocalDate();
        for (int i = 0; i < weatherArray.length(); i++) {
            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            long ts = dayForecast.getLong(SW_TIMESTAMP);
            LocalDate forecastDate = ZonedDateTime.ofInstant(Instant.ofEpochSecond(ts), ZONE_ID)
                    .toLocalDate();
            if (forecastDate.isBefore(today)) {
                continue;
            }

            DailyWeatherInfo weatherInfo = new DailyWeatherInfo();
            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather icon ID.
             */
            JSONObject weatherObject =
                    dayForecast.getJSONObject(SW_WEATHER);
            weatherInfo.weatherDesc = weatherObject.getString(SW_DESCRIPTION);
            weatherInfo.weatherIconID = weatherObject.getString(SW_ICON_ID);
            weatherInfo.highTemp = dayForecast.getDouble(SW_HIGH);
            weatherInfo.lowTemp = dayForecast.getDouble(SW_LOW);
            weatherInfo.cityName = cityName;
            weatherInfo.dateDisplayStr = SWDateUtils.getDayName(today, forecastDate);

            parsedWeatherData.add(weatherInfo);
        }

        return parsedWeatherData;
    }

}



