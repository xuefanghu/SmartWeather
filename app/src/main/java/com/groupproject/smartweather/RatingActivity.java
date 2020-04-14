package com.groupproject.smartweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupproject.smartweather.Utils.Preferences;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {
    private DatabaseReference firebaseRef;
    private String cityName;

    public static class RatingData implements Serializable {
        // The weather rating from this user.
        public double rating;
        // The distance between the user GPS location and the city to be rated.
        public double distance;
        // The timestamp (epoch time) when this rating happens.
        public long   timestamp_sec;
        public RatingData(double rating, double distance, long timestamp_sec) {
            this.rating = rating;
            this.distance = distance;
            this.timestamp_sec = timestamp_sec;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Get the city name passed from MainActivity.
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Serializable obj =  bundle.getSerializable("cityName");
        if (obj==null) {
            return;
        }
        cityName = (String)obj;
        ((TextView)findViewById(R.id.city_name)).setText(cityName);

        // Initialize the FireBase database reference.
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    // listener to submit the user rating to firebase with proper key structure.
    public void onClickSubmitRating(View v) {
        float weatherRating = ((RatingBar)findViewById(R.id.my_rating_bar)).getRating();
        if (weatherRating<=0) {
            showMessage("Invalid weather rating. Please try again.");
            return;
        }
        // The json nested key path, where the top level is the city name and the second level is
        // the unique user_id.
        // This stucture ensures that each user has at most one rating per city.
        String firebaseKey = cityName + "/" + Preferences.getMyUserId(this);
        double distance = Preferences.getDistance(this);
        /* Comment it out so we can test using emulator which doesn't have GPS reading.
        TODO: uncomment this block once use physical device with GPS for testing.
        if (distance < 0) {
             showMessage("Location not available. Please enable GPS.");
             return;
        }*/
        Map<String, Object> updates = new HashMap<>();
        updates.put(firebaseKey, new RatingData(weatherRating,
                distance,System.currentTimeMillis()/1000));
        firebaseRef.updateChildren(updates);
        showMessage("The weather rating has been submitted.");
    }

    /**
     * This method will show the message using Toast.
     *
     * @param msg The message to display.
     */
    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
