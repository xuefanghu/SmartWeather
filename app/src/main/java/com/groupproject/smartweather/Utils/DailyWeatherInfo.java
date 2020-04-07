package com.groupproject.smartweather.Utils;


/**
 * Data structure to store the weather info of a day, such as the high/low temperatures, wind speed,
 * etc. We will use it to power both the multi-day list view and the single-day detailed view.
 */
public class DailyWeatherInfo {
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
}
