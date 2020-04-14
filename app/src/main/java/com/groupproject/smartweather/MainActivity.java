package com.groupproject.smartweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groupproject.smartweather.Utils.DailyWeatherInfo;
import com.groupproject.smartweather.Utils.NetworkUtils;
import com.groupproject.smartweather.Utils.Preferences;
import com.groupproject.smartweather.Utils.ServerJsonUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView cityNameView;
    private RecyclerView swRecyclerView;
    private ListItemAdapter swListItemAdapter;
    private ProgressBar loadingIndicator;
    private TextView weatherQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNameView = findViewById(R.id.city_name);
        swRecyclerView = findViewById(R.id.recyclerview_forecast);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        swRecyclerView.setLayoutManager(layoutManager);
        swRecyclerView.setHasFixedSize(true);

        swListItemAdapter = new ListItemAdapter(this,
                new ListItemAdapter.ListItemAdapterOnClickHandler() {
            /**
             * Show a new Activity of weather details for the day clicked.
             *
             * @param dayWeather
             */
            @Override
            public void onClick(DailyWeatherInfo dayWeather) {
                Intent intent = new Intent(MainActivity.this, DailyDetailActivity.class);
                intent.putExtra("dayWeather", dayWeather);
                startActivity(intent);
            }
        });
        swRecyclerView.setAdapter(swListItemAdapter);

        loadingIndicator = findViewById(R.id.sw_loading_indicator);
        weatherQuestion = findViewById(R.id.weather_question);

        loadWeatherDataWithPermissionRequest();
    }

    /**
     * This method gets the weather information in the background based on the user input
     * location or the current GPS location.
     *
     * @param currentLocation The current location from the device GPS.
     */
    private void loadWeatherData(Location currentLocation) {
        new FetchWeatherTask(this).execute(currentLocation);
    }

    /**
     * Show a new Activity for weather rating.
     */
    public void onClickRating(View v) {
        Intent intent = new Intent(this, RatingActivity.class);
        intent.putExtra("cityName", this.cityNameView.getText().toString());
        startActivity(intent);
    }

    /**
     * This method will show the error message using Toast.
     *
     * @param msg The error message to display.
     */
    private void showErrorMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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
        private final Context context;

        public FetchWeatherTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
            weatherQuestion.setVisibility(View.INVISIBLE);
        }

        @Override
        protected List<DailyWeatherInfo> doInBackground(Location... params) {
            if (params.length <1) {
                return null;
            }
            Location currentLocation = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(
                    Preferences.getPreferredLocation(context), currentLocation);
            try {
                String jsonWeatherResponse = NetworkUtils
                        .getDataFromHttp(weatherRequestUrl);
                List<DailyWeatherInfo> simpleJsonWeatherData = ServerJsonUtils
                        .getSimpleWeatherStringsFromJson(jsonWeatherResponse);
                if (currentLocation != null && simpleJsonWeatherData.size()>0) {
                    float[] distance = new float[1];
                    Location.distanceBetween(
                            currentLocation.getLatitude(),
                            currentLocation.getLongitude(),
                            simpleJsonWeatherData.get(0).lat,
                            simpleJsonWeatherData.get(0).lng,
                            distance
                            );
                    Preferences.setDistance(context, distance[0]);
                }
                return simpleJsonWeatherData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<DailyWeatherInfo> weatherData) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            weatherQuestion.setVisibility(View.VISIBLE);
            if (weatherData != null) {
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
            loadWeatherData(location);
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
