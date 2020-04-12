package com.groupproject.smartweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupproject.smartweather.Utils.DailyWeatherInfo;
import com.groupproject.smartweather.Utils.WeatherUtils;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DailyDetailActivity extends AppCompatActivity {
    final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("hh:mm a");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Serializable obj =  bundle.getSerializable("dayWeather");
        if (obj==null) {
            return;
        }
        DailyWeatherInfo info= (DailyWeatherInfo)obj;
        ((TextView) findViewById(R.id.weather_date)).setText(info.dateDisplayStr);
        ((TextView) findViewById(R.id.city_name)).setText(info.cityName);
        int resourceImage = getResources().getIdentifier(info.weatherIconID, "drawable",
                getPackageName());
        ((ImageView) findViewById(R.id.weather_icon)).setImageResource(resourceImage);
        ((TextView) findViewById(R.id.weather_desc)).setText(info.weatherDesc);

        // This row shows the high temperature and the low temperature.
        ((TextView) findViewById(R.id.details_high_temp)).setText(
                WeatherUtils.formatTemperature(info.highTemp));
        ((TextView) findViewById(R.id.details_low_temp)).setText(
                WeatherUtils.formatTemperature(info.lowTemp));

        // This row shows the sunrise time and the sunset time.
        ZoneId zoneId = ZoneId.of(info.timeZone);
        ZonedDateTime sunriseTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(info.sunriseTs),
                zoneId);
        ((TextView) findViewById(R.id.details_sunrise)).setText(sunriseTime.format(dtFormatter));
        ZonedDateTime sunsetTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(info.sunsetTs),
                zoneId);
        ((TextView) findViewById(R.id.details_sunset)).setText(sunsetTime.format(dtFormatter));

        // This row shows the cloud coverage and the humidity.
        ((TextView) findViewById(R.id.details_cloud_coverage)).setText(
                String.format("%d%%", info.cloudCoverage));
        ((TextView) findViewById(R.id.details_humidity)).setText(
                String.format("%d%%", info.relativeHumidity));

        // This row shows the precipitation and the pressure.
        ((TextView) findViewById(R.id.details_precipitation)).setText(
                String.format("%.0f MM", info.precipitation));
        ((TextView) findViewById(R.id.details_pressure)).setText(
                String.format("%.0f MB", info.pressure));

        // This row shows the wind direction and the wind speed.
        ((TextView) findViewById(R.id.details_wind_dir)).setText(
                info.windDir);
        ((TextView) findViewById(R.id.details_wind_speed)).setText(
                String.format("%.1f M/S", info.windSpeed));

        // This row shows the visibility and the UV index.
        ((TextView) findViewById(R.id.details_visibility)).setText(
                String.format("%.0f KM", info.visibility));
        ((TextView) findViewById(R.id.details_uv)).setText(
                String.format("%.0f", info.uv));
    }
}
