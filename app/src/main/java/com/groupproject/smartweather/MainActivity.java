package com.groupproject.smartweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groupproject.smartweather.Utils.DailyWeatherInfo;
import com.groupproject.smartweather.Utils.NetworkUtils;
import com.groupproject.smartweather.Utils.Preferences;
import com.groupproject.smartweather.Utils.ServerJsonUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ListItemAdapter.ListItemAdapterOnClickHandler {
    private TextView cityNameView;
    private RecyclerView swRecyclerView;
    private ListItemAdapter swListItemAdapter;
    private TextView errorMessageShow;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNameView = findViewById(R.id.city_name);
        swRecyclerView = findViewById(R.id.recyclerview_forecast);
        errorMessageShow = findViewById(R.id.sw_error_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        swRecyclerView.setLayoutManager(layoutManager);
        swRecyclerView.setHasFixedSize(true);

        swListItemAdapter = new ListItemAdapter(this, this);
        swRecyclerView.setAdapter(swListItemAdapter);

        loadingIndicator = findViewById(R.id.sw_loading_indicator);

        loadWeatherDataWithPermissionRequest();
    }

    /**
     * This method gets the weather information in the background based on the user input
     * location or the current GPS location.
     *
     * @param currentLocation The current location from the device GPS.
     */
    private void loadWeatherData(Location currentLocation) {
        showWeatherDataView();
        new FetchWeatherTask().execute(currentLocation);
    }

    /**
     * Show a new Activity of weather details for the day clicked.
     *
     * @param dayWeather
     */
    public void onClick(DailyWeatherInfo dayWeather) {
        // TODO: create a new intent for the weather details.

    }

    /**
     * This method will make the View for the weather data visible and hide the error message.
     */
    private void showWeatherDataView() {
        errorMessageShow.setVisibility(View.INVISIBLE);
        swRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather view.
     *
     * @param msg The error message to display.
     */
    private void showErrorMessage(String msg) {
        swRecyclerView.setVisibility(View.INVISIBLE);
        errorMessageShow.setText(msg);
        errorMessageShow.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            swListItemAdapter.setWeatherData(null);
            loadWeatherDataWithPermissionRequest();
            return true;
        }

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchWeatherTask extends AsyncTask<Location, Void, List<DailyWeatherInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<DailyWeatherInfo> doInBackground(Location... params) {
            if (params.length <1) {
                return null;
            }
            URL weatherRequestUrl = NetworkUtils.buildUrl(
                    Preferences.getPreferredLocation(), params[0]);
            try {
                String jsonWeatherResponse = NetworkUtils
                        .getDataFromHttp(weatherRequestUrl);
                List<DailyWeatherInfo> simpleJsonWeatherData = ServerJsonUtils
                        .getSimpleWeatherStringsFromJson(jsonWeatherResponse);
                Log.e("doInBackground", "good! " + jsonWeatherResponse);
                return simpleJsonWeatherData;
            } catch (Exception e) {
                Log.e("doInBackground", "exception");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<DailyWeatherInfo> weatherData) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showWeatherDataView();
                swListItemAdapter.setWeatherData(weatherData);
                if (weatherData.size()>0) {
                    cityNameView.setText(weatherData.get(0).cityName);
                }
            } else {
                showErrorMessage("Error. Please try again by clicking REFRESH.");
            }
        }
    }

    // Get user GPS location
    private static final int LOCATION_REQUEST = 457;
    public void loadWeatherDataWithPermissionRequest() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // Request user permission to access the GPS location.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST);
        } else {
            loadWeatherDataWithPermissionCheck();
        }
    }

    void loadWeatherDataWithPermissionCheck() {
        boolean hasPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
        if (hasPermission) {
            LocationManager locationManager = (LocationManager) getSystemService(
                    Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null || !Preferences.getPreferredLocation().isEmpty()) {
                loadWeatherData(location);
            } else {
                showErrorMessage(
                        "GPS location not available. Please enter location in SETTINGS.");
            }
        }else {
            showErrorMessage("Location permission required. Please try again.");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == LOCATION_REQUEST) {
            loadWeatherDataWithPermissionCheck();
        }
    }
}
