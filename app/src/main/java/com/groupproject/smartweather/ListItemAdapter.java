package com.groupproject.smartweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.groupproject.smartweather.Utils.DailyWeatherInfo;
import com.groupproject.smartweather.Utils.Preferences;
import com.groupproject.smartweather.Utils.WeatherUtils;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemAdapterViewHolder> {

    private final ListItemAdapterOnClickHandler itemClickHandler;
    private List<DailyWeatherInfo> weatherData;
    private Context context;

    public ListItemAdapter(Context context, ListItemAdapterOnClickHandler clickHandler) {
        this.context = context;
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
        listItemAdapterViewHolder.dateView.setText(info.dateDisplayStr);
        listItemAdapterViewHolder.descView.setText(info.weatherDesc);
        listItemAdapterViewHolder.tempView.setText(WeatherUtils.formatTemperature(info.lowTemp,
                info.highTemp, Preferences.getIsMetric(context)));
        int resourceImage = context.getResources().getIdentifier(info.weatherIconID, "drawable",
                context.getPackageName());
        listItemAdapterViewHolder.iconView.setImageResource(resourceImage);
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
        public final TextView dateView;
        public final ImageView iconView;
        public final TextView descView;
        public final TextView tempView;

        public ListItemAdapterViewHolder(View view) {
            super(view);
            dateView = view.findViewById(R.id.weather_date);
            iconView = view.findViewById(R.id.weather_icon);
            descView = view.findViewById(R.id.weather_desc);
            tempView = view.findViewById(R.id.high_low_temp);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int adapterPos = getAdapterPosition();
            DailyWeatherInfo dayWeather = weatherData.get(adapterPos);
            itemClickHandler.onClick(dayWeather);
        }
    }
}