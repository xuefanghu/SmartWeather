package com.groupproject.smartweather;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.groupproject.smartweather.Utils.Preferences;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        int checked_id;
        if (Preferences.getIsMetric()) {
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

    // Set the user's choice of C or F on the preference page.
    public void onRadioButtonClicked(View view) {
        // Is the button checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.c_radio_button:
                    Preferences.setIsMetric(checked);
                    break;
            case R.id.f_radio_button:
                Preferences.setIsMetric(!checked);
                    break;
        }
    }
}
