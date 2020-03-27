package com.groupproject.smartweather.Utils;

import android.content.Context;

import com.groupproject.smartweather.R;


public final class WeatherUtils {
    /**
     * This method will convert a temperature from Celsius to Fahrenheit.
     *
     * @param tempInC Temperature in degrees Celsius(°C)
     * @return Temperature in degrees Fahrenheit (°F)
     */
    private static double celsiusToFahrenheit(double tempInC) {
        return (tempInC * 1.8) + 32;
    }

    /**
     * This method will perform temperature format conversion.
     *
     * @param context     Android Context to access preferences and resources
     * @param temperature Temperature in degrees Celsius (°C)
     * @return Formatted temperature String
     */
    public static String formatTemperature(Context context, double temperature) {
        int temperatureFormatResId = R.string.format_temperature_celsius;

        if (!Preferences.isMetric(context)) {
            temperature = celsiusToFahrenheit(temperature);
            temperatureFormatResId = R.string.format_temperature_fahrenheit;
        }
        // no show of decimal points of the temperature
        return String.format(context.getString(temperatureFormatResId), temperature);
    }

    /**
     * This method will format the temperatures display: HIGH °C / LOW °C
     *
     * @param context Android Context to access preferences and resources
     * @param high    High temperature for a day
     * @param low     Low temperature for a day
     * @return String HIGH °C / LOW °C
     */
    public static String formatHighLow(Context context, double high, double low) {
        String formattedHigh = formatTemperature(context, high);
        String formattedLow = formatTemperature(context, low);
        return formattedHigh + " / " + formattedLow;
    }
}





