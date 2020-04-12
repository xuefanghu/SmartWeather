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

        // Below are all the json fields we are interested in.
        final String SW_CITY_NAME = "city_name";
        final String SW_TIME_ZONE = "timezone";
        /* Weather information. Each day's forecast info is an element of the "data" array */
        final String SW_DATA = "data";
        final String SW_HIGH = "high_temp";
        final String SW_LOW = "low_temp";
        final String SW_WEATHER = "weather";
        final String SW_DESCRIPTION = "description";
        final String SW_ICON_ID = "icon";
        final String SW_TIMESTAMP = "ts";
        final String SW_SUNRISE_TS = "sunrise_ts";
        final String SW_SUNSET_TS = "sunset_ts";
        final String SW_CLOUD_COVERAGE = "clouds";
        final String SW_RELATIVE_HUMIDITY = "rh";
        final String SW_WIND_DIR = "wind_cdir";
        final String SW_WIND_SPEED = "wind_spd";
        final String SW_PRECIPITATION = "precip";
        final String SW_PRESSURE = "pres";
        final String SW_VISIBILITY = "vis";
        final String SW_UV = "uv";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(SW_DATA);
        String cityName = forecastJson.getString(SW_CITY_NAME);
        String timeZone = forecastJson.getString(SW_TIME_ZONE);
        // Display the date/time in local time.
        final ZoneId ZONE_ID = ZoneId.of(timeZone);
        LocalDate today = ZonedDateTime.now(ZONE_ID).toLocalDate();

        /* DailyWeatherInfo list to hold each day's weather info */
        List<DailyWeatherInfo> parsedWeatherData = new LinkedList<>();
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
            weatherInfo.cityName = cityName;
            weatherInfo.timeZone = timeZone;
            weatherInfo.dateDisplayStr = SWDateUtils.getDayName(today, forecastDate);
            weatherInfo.highTemp = dayForecast.getDouble(SW_HIGH);
            weatherInfo.lowTemp = dayForecast.getDouble(SW_LOW);
            weatherInfo.sunriseTs = dayForecast.getLong(SW_SUNRISE_TS);
            weatherInfo.sunsetTs = dayForecast.getLong(SW_SUNSET_TS);
            weatherInfo.cloudCoverage =dayForecast.getInt(SW_CLOUD_COVERAGE);
            weatherInfo.relativeHumidity =dayForecast.getInt(SW_RELATIVE_HUMIDITY);
            weatherInfo.windDir = dayForecast.getString(SW_WIND_DIR);
            weatherInfo.windSpeed = dayForecast.getDouble(SW_WIND_SPEED);
            weatherInfo.precipitation = dayForecast.getDouble(SW_PRECIPITATION);
            weatherInfo.lowTemp = dayForecast.getDouble(SW_LOW);
            weatherInfo.lowTemp = dayForecast.getDouble(SW_LOW);
            weatherInfo.pressure = dayForecast.getDouble(SW_PRESSURE);
            weatherInfo.visibility = dayForecast.getDouble(SW_VISIBILITY);
            weatherInfo.uv = dayForecast.getDouble(SW_UV);
            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather icon ID.
             */
            JSONObject weatherObject = dayForecast.getJSONObject(SW_WEATHER);
            weatherInfo.weatherDesc = weatherObject.getString(SW_DESCRIPTION);
            weatherInfo.weatherIconID = weatherObject.getString(SW_ICON_ID);

            parsedWeatherData.add(weatherInfo);
        }

        return parsedWeatherData;
    }

}



