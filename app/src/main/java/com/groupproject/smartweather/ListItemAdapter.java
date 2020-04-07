package com.groupproject.smartweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.groupproject.smartweather.Utils.DailyWeatherInfo;
import com.groupproject.smartweather.Utils.Preferences;
import com.groupproject.smartweather.Utils.WeatherUtils;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemAdapterViewHolder> {

    private final ListItemAdapterOnClickHandler itemClickHandler;
    private List<DailyWeatherInfo> weatherData;

    public ListItemAdapter(ListItemAdapterOnClickHandler clickHandler) {
        itemClickHandler = clickHandler;
    }

    @Override
    public ListItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        // layout id of the list item
        int listItemId = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(listItemId, viewGroup, false);
        return new ListItemAdapterViewHolder(view);
    }

    /**
     * Called by the RecyclerView to show the weather data at a particular position.
     */
    @Override
    public void onBindViewHolder(ListItemAdapterViewHolder listItemAdapterViewHolder, int pos) {
        DailyWeatherInfo info = weatherData.get(pos);
        String format;
        if (Preferences.isMetric()) {
            format = "%s - %s - %s/%s";
        } else {
            format = "%s - %s - %s/%s";
        }
        String weatherOfTheDay = String.format(format, info.dateDisplayStr, info.weatherDesc,
                WeatherUtils.formatTemperature(info.highTemp),
                WeatherUtils.formatTemperature(info.lowTemp));
        listItemAdapterViewHolder.weatherTextView.setText(weatherOfTheDay);
    }

    @Override
    public int getItemCount() {
        if (weatherData == null) {
            return 0;
        }
        return weatherData.size();
    }

    public void setWeatherData(List<DailyWeatherInfo> weatherData) {
        this.weatherData = weatherData;
        notifyDataSetChanged();
    }

    public interface ListItemAdapterOnClickHandler {
        void onClick(DailyWeatherInfo dayWeather);
    }

    public class ListItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView weatherTextView;

        public ListItemAdapterViewHolder(View view) {
            super(view);
            weatherTextView = view.findViewById(R.id.sw_weather_data);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int adapterPos = getAdapterPosition();
            DailyWeatherInfo dayWeather = weatherData.get(adapterPos);
            itemClickHandler.onClick(dayWeather);
        }
    }
}