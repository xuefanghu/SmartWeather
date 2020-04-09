package com.groupproject.smartweather.Utils;

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
     * @param low Low temperature in degrees Celsius (°C)
     * @param high High temperature in degrees Celsius (°C)
     * @return Formatted temperature String
     */
    public static String formatTemperature(double low, double high) {
        if (!Preferences.getIsMetric()) {
            return String.format("%.0f/%.0f°F", celsiusToFahrenheit(low), celsiusToFahrenheit(high));
        } else {
            return String.format("%.0f/%.0f°C", low, high);
        }
    }
}





