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
     * @param temperature Temperature in degrees Celsius (°C)
     * @return Formatted temperature String
     */
    public static String formatTemperature(double temperature) {
        if (!Preferences.isMetric()) {
            return String.format("%.0fF", celsiusToFahrenheit(temperature));
        } else {
            return String.format("%.0fC", temperature);
        }
    }
}





