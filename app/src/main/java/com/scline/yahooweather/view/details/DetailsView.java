package com.scline.yahooweather.view.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scline.yahooweather.R;
import com.scline.yahooweather.util.Globals;
import com.scline.yahooweather.yahoo.data.Location;

/**
 * Created by scline on 6/8/16.
 */
public class DetailsView extends FrameLayout {
    private Location location;

    TextView titleText;
    TextView subTitleText;
    TextView temperatureText;
    ListView forecastListView;
    ForecastAdapter forecastAdapter;

    public DetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.layout_details, this);

        titleText = (TextView)this.findViewById(R.id.layout_details_titleText);
        subTitleText = (TextView)this.findViewById(R.id.layout_details_subTitleText);
        temperatureText = (TextView)this.findViewById(R.id.layout_details_temperatureText);
        forecastListView = (ListView)this.findViewById(R.id.layout_details_forecastList);
    }

    public void update() {
        if(location == null) return;

        forecastAdapter = new ForecastAdapter(getContext(), location.getForecast());
        forecastListView.setAdapter(forecastAdapter);

        titleText.setText(location.getCity());
        subTitleText.setText(location.getCondition().text);
        String temp = location.getCondition().temp + Globals.DEGREE;
        temperatureText.setText(temp);
    }

    public void setLocation(@NonNull Location location) {
        this.location = location;
        update();
    }
}
