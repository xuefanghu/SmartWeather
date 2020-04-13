package com.groupproject.smartweather;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.groupproject.smartweather.Utils.Preferences;
import com.groupproject.smartweather.Utils.TopUSCities;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);

        // Render the city input component with user's previous input pre-filled.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, TopUSCities.CITY_LIST);
        AutoCompleteTextView textView = findViewById(R.id.city_input);
        textView.setAdapter(adapter);
        textView.setText(Preferences.getPreferredLocation(this));

        // Render the Celsius/Fahrenheit selection component with user's previous choice pre-filled.
        int checked_id;
        if (Preferences.getIsMetric(this)) {
            checked_id = R.id.c_radio_button;
        } else {
            checked_id = R.id.f_radio_button;
        }
        ((RadioButton) findViewById(checked_id)).setChecked(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save the input location (which has higher priority than the GPS location).
        String locationStr = ((AutoCompleteTextView)findViewById(R.id.city_input))
                .getEditableText().toString().trim();
        Preferences.setPreferredLocation(this, locationStr);

        // Save the choice of C or F.
        boolean isMetric = ((RadioButton) findViewById(R.id.c_radio_button)).isChecked();
        Preferences.setIsMetric(this, isMetric);
    }
}
