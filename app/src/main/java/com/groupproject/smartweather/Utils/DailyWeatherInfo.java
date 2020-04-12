package com.groupproject.smartweather.Utils;


import java.io.Serializable;

/**
 * Data structure to store the weather info of a day, such as the high/low temperatures, wind speed,
 * etc. We will use it to power both the multi-day list view and the single-day detailed view.
 */
public class DailyWeatherInfo implements Serializable {
    // Weather text such as "Scattered clouds" etc.
    public String weatherDesc;

    // Used to render the weather icon.
    // The IDs will be the file name in the icon set downloaded from the Weatherbit website:
    // https://www.weatherbit.io/static/exports/icons.tar.gz
    public String weatherIconID;

    public double highTemp;

    public double lowTemp;

    // Used to display the city name given the device GPS lat/lng.
    public String cityName;

    // User-friendly date string such as "Today", "Tomorrow" and day of the week string.
    public String dateDisplayStr;

    public long sunriseTs;
    public long sunsetTs;
    // Cloud coverage (%).
    public int cloudCoverage;
    // Relative humidity (%)
    public int relativeHumidity;
    // wind direction like "NW" etc.
    public String windDir;
    // Wind speed (in M/S).
    public double windSpeed;
    // Accumulated liquid equivalent precipitation (in MM).
    public double precipitation;
    // Pressure (in MB)
    public double pressure;
    // Visibility (in KM)
    public double visibility;
    // UV Index (0-11+).
    public double uv;
    public String timeZone;

}
