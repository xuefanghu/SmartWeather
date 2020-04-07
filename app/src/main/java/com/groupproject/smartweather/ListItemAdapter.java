package com.groupproject.smartweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemAdapterViewHolder> {

    private String[] weatherData;

    private final ListItemAdapterOnClickHandler itemClickHandler;

    public interface ListItemAdapterOnClickHandler{
        void onClick(String dayWeather);
    }

    public ListItemAdapter(ListItemAdapterOnClickHandler clickHandler){
        itemClickHandler = clickHandler;
    }


    public class ListItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView weatherTextView;

        public ListItemAdapterViewHolder(View view){
            super(view);
            weatherTextView = view.findViewById(R.id.sw_weather_data);
            view.setOnClickListener(this);
        }

        public void onClick(View view){
            int adapterPos = getAdapterPosition();
            String dayWeather = weatherData[adapterPos];
            itemClickHandler.onClick(dayWeather);
        }
    }

    @Override
    public ListItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
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
        String weatherOfTheDay = weatherData[pos];
        listItemAdapterViewHolder.weatherTextView.setText(weatherOfTheDay);
    }

    @Override
    public int getItemCount() {
        if (weatherData == null) {
            return 0;
        }
        return weatherData.length;
    }

    public void setWeatherData(String[] weatherData) {
        this.weatherData = weatherData;
        notifyDataSetChanged();
    }
}