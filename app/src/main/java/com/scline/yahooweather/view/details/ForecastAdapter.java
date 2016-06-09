package com.scline.yahooweather.view.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.scline.yahooweather.R;
import com.scline.yahooweather.util.Globals;
import com.scline.yahooweather.util.WebManager;
import com.scline.yahooweather.yahoo.YahooWeatherIcons;
import com.scline.yahooweather.yahoo.YahooWeatherRSS;
import com.scline.yahooweather.yahoo.data.DayForecast;

import java.util.ArrayList;

/**
 * Created by scline on 6/8/16.
 */
public class ForecastAdapter extends BaseAdapter {

    final Context context;
    final ArrayList<DayForecast> forecasts;

    public ForecastAdapter(@NonNull Context context, @NonNull ArrayList<DayForecast> forecasts) {
        this.context = context;
        this.forecasts = forecasts;
    }

    @Override
    public int getCount() {
        return forecasts.size();
    }

    @Override
    public Object getItem(int position) {
        return forecasts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DayForecast forecast = (DayForecast) getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_forecast, parent, false);
        }

        TextView dayText = (TextView) convertView.findViewById(R.id.row_forecast_dayText);
        dayText.setText(String.format("%s %s", forecast.day, forecast.date));

        WebManager webMan = WebManager.instance(context);
        NetworkImageView icon = (NetworkImageView) convertView.findViewById(R.id.row_forecast_icon);
        icon.setImageUrl(
                YahooWeatherRSS.buildIconURL(forecast.code),
                new ImageLoader(webMan.getRequestQueue(), YahooWeatherIcons.instance()));

        TextView highText = (TextView) convertView.findViewById(R.id.row_forecast_highText);
        String highs = forecast.high + Globals.DEGREE;
        highText.setText(highs);

        TextView lowText = (TextView) convertView.findViewById(R.id.row_forecast_lowText);
        String lows = forecast.low + Globals.DEGREE;
        lowText.setText(lows);

        return convertView;
    }
}
