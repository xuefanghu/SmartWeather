package com.groupproject.smartweather;

import android.content.Context;
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

import com.groupproject.smartweather.Utils.NetworkUtils;
import com.groupproject.smartweather.Utils.Preferences;
import com.groupproject.smartweather.Utils.ServerJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements ListItemAdapter.ListItemAdapterOnClickHandler {

    private RecyclerView swRecyclerView;

    private ListItemAdapter swListItemAdapter;

    private TextView errorMessageShow;

    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        swRecyclerView = findViewById(R.id.recyclerview_forecast);
        errorMessageShow = findViewById(R.id.sw_error_message);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        swRecyclerView.setLayoutManager(layoutManager);
        swRecyclerView.setHasFixedSize(true);

        swListItemAdapter = new ListItemAdapter(this);
        swRecyclerView.setAdapter(swListItemAdapter);

        loadingIndicator = findViewById(R.id.sw_loading_indicator);

        loadWeatherData();
    }

    /**
     * This method will get the weather information of user's preferred city location
     * Get the weather data in the background.
     */
    private void loadWeatherData() {

        showWeatherDataView();

        String city = Preferences.getPreferredWeatherCity(this);
        new FetchWeatherTask().execute(city);
    }

    /**
     * Show a new Activity of weather details for the day clicked.
     * @param dayWeather
     */
    public void onClick(String dayWeather){
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
     */
    private void showErrorMessage() {
        swRecyclerView.setVisibility(View.INVISIBLE);
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
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            // if no zip code
            if (params.length == 0) {
                Log.e("doInBackground", "no zip code");
                return null;
            }

            String city = params[0];
            Log.e("doInBackground location", city);
            URL weatherRequestUrl = NetworkUtils.buildUrl(city);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getDataFromHttp(weatherRequestUrl);

                String[] simpleJsonWeatherData = ServerJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
                Log.e("doInBackground", "good! " + jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                Log.e("doInBackground", "exception");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showWeatherDataView();
                swListItemAdapter.setWeatherData(weatherData);
            } else {
                showErrorMessage();
            }
        }
    }
}
