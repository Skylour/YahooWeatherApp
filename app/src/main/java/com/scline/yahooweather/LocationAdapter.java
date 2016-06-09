package com.scline.yahooweather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.scline.yahooweather.util.Globals;
import com.scline.yahooweather.util.WebManager;
import com.scline.yahooweather.yahoo.YahooWeatherIcons;
import com.scline.yahooweather.yahoo.YahooWeatherRSS;
import com.scline.yahooweather.yahoo.data.Location;

import java.util.ArrayList;

/**
 * Created by scline on 6/8/16.
 */
public class LocationAdapter extends BaseAdapter {

    final private Context context;
    final private ArrayList<Location> locations;

    public LocationAdapter(@NonNull Context context, @NonNull ArrayList<Location> locations) {
        this.context = context;
        this.locations = locations;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Location data = (Location)getItem(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_location, parent, false);
        }

        ProgressBar spinner = (ProgressBar)convertView.findViewById(R.id.row_location_progress);

        if(data.isLoaded()) {
            TextView title = (TextView)convertView.findViewById(R.id.row_location_titleText);
            title.setText(data.getCity());

            TextView temperatureText = (TextView)convertView.findViewById(R.id.row_location_temperatureText);
            String temp = data.getCondition().temp + Globals.DEGREE;
            temperatureText.setText(temp);

            ImageButton deleteButton = (ImageButton)convertView.findViewById(R.id.row_location_delete);
            deleteButton.setOnClickListener(new OnDeleteClickListener(position));

            WebManager webMan = WebManager.instance(context);

            NetworkImageView icon = (NetworkImageView) convertView.findViewById(R.id.row_location_icon);
            icon.setImageUrl(
                    YahooWeatherRSS.buildIconURL(data.getCondition().code),
                    new ImageLoader(webMan.getRequestQueue(), YahooWeatherIcons.instance()));


            convertView.setOnClickListener(new OnRowClickListener(position));

            spinner.setVisibility(View.GONE);
        } else {
            spinner.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class OnRowClickListener implements View.OnClickListener {

        final int position;

        public OnRowClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Location data = locations.get(position);
            if(context instanceof LocationsActivity) {
                ((LocationsActivity)context).displayDetails(data);
            }
        }
    }

    private class OnDeleteClickListener implements View.OnClickListener {

        final private int position;

        public OnDeleteClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            final Location data = locations.get(position);
            Context context = LocationAdapter.this.context;

            String message = context.getResources().getString(R.string.alert_delete_message) + " " + data.getCity();

            AlertDialog.Builder builder = new AlertDialog.Builder(LocationAdapter.this.context);
            builder.setTitle(R.string.alert_delete_title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            locations.remove(position);
                            LocationAdapter.this.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }
}
