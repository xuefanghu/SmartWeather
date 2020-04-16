package com.groupproject.smartweather;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.smartweather.Utils.Preferences;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {
    // The decay rate of the weather rating in terms of age (in second).
    private final static int HALF_SCORE_AGE_SEC = 3600*24*7;
    // The decay rate of the weather rating in terms of distance (in meter).
    private final static int HALF_SCORE_DISTANCE_METER = 10000;
    // The parameter to control the decay function curve.
    private final static double SCORE_DECAY_POWER = 2.0;

    private DatabaseReference firebaseRef;
    private String cityName;
    String firebaseKey;

    public static class RatingData implements Serializable {
        // The weather rating from this user.
        public float rating;
        // The distance between the user GPS location and the city to be rated.
        public double distance;
        // The timestamp (epoch time) when this rating happens.
        public long   timestamp_sec;

        public RatingData() {
        }
        public RatingData(float rating, double distance, long timestamp_sec) {
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
        ValueEventListener fbMyRatingListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RatingData ratingData = dataSnapshot.getValue(RatingData.class);
                String buttonText;
                if (ratingData!=null) {
                    ((RatingBar)findViewById(R.id.my_rating_bar)).setRating(ratingData.rating);
                    // User has submitted rating for this location before.
                    buttonText = "Change My Rating";
                } else {
                    // User hasn't submitted rating for this location before.
                    buttonText = "Submit My Rating";
                }
                ((Button)findViewById(R.id.submit_my_rating)).setText(buttonText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseKey = cityName + "/" + Preferences.getMyUserId(this);
        firebaseRef.child(firebaseKey).addValueEventListener(fbMyRatingListener);

        ValueEventListener fbAllRatingListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<RatingData> ratings = new LinkedList<>();
                long ratingCount =dataSnapshot.getChildrenCount();
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    ratings.add(item.getValue(RatingData.class));
                }
                float weightedRating = calculateWeightedRating(ratings);
                ((RatingBar)findViewById(R.id.avg_rating_bar)).setRating(weightedRating);
                ((TextView)findViewById(R.id.avg_rating_count)).setText(
                        String.format("(Rated by %d people)", ratingCount)
                );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseRef.child(cityName).addValueEventListener(fbAllRatingListener);
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

    private static float calculateWeightedRating(List<RatingData> ratings) {
        double totalWeight = 0;
        double totalRating = 0;
        long nowSec = System.currentTimeMillis()/1000;
        for (RatingData rating: ratings) {
            long age_sec = nowSec - rating.timestamp_sec;
            double dist = rating.distance;
            // Possible timestamp skew on devices.
            if (age_sec < 0) {
                age_sec = 0;
            }
            // If some users submitted review with no GPS location available, we set a default
            // distance which result in distance weight of 0.5.
            if (dist < 0) {
                dist = HALF_SCORE_DISTANCE_METER;
            }
            double ageWeight = squashHalf(age_sec, HALF_SCORE_AGE_SEC, SCORE_DECAY_POWER);
            double distWeight = squashHalf(dist, HALF_SCORE_DISTANCE_METER, SCORE_DECAY_POWER);
            // combine the decay of the rating age and the rating distance.
            double weight = ageWeight * distWeight;
            totalWeight += weight;
            totalRating += weight * rating.rating;
        }
        if (totalWeight == 0) {
            return 0;
        } else {
            return (float) (totalRating / totalWeight);
        }
    }

    /** The Squash Half scoring function, will be used for both age decay and distance decay.
     *
     * @param x the input variable
     * @param half controls the scale when x = half the resulting score drops to 0.5
     * @param power controls how sharp the score decays.
     * @return the score between [0, 1]
     */
    private static double squashHalf(double x, double half, double power) {
        if (x < 0 || half < 0 ) {
            return 0;
        }
        double up = Math.pow(x, power);
        double hp = Math.pow(half, power);
        if (up+hp > 0) {
            return hp / (up + hp);
        } else {
            return 0;
        }
    }
}
