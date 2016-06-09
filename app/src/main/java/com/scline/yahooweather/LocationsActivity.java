package com.scline.yahooweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.scline.yahooweather.util.Globals;
import com.scline.yahooweather.util.Utils;
import com.scline.yahooweather.view.details.DetailsView;
import com.scline.yahooweather.yahoo.data.Location;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class LocationsActivity extends Activity implements Location.LocationsLoadedListener {

    private static String[] defaultCities =
            {"Salt Lake City",
                    "Denver",
                    "Pheonix",
                    "San Fransisco, CA",
                    "New York, NY"};

    private HashMap<String, Location> verificationMap = new HashMap<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private LocationAdapter locationAdapter;
    private ListView locationsListView;
    private int dataLoaded = 0;

    private AlertDialog loadFailureAlert;
    private String loadFailureMessage = "";
    private AlertDialog existsAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAlerts();

        Location.addListener(this);

        switch (Utils.getDeviceType(this)) {
            case MOBILE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case TABLET:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }

        loadLocations();

        locationsListView = (ListView) this.findViewById(R.id.activity_main_listView);
        locationAdapter = new LocationAdapter(this, locations);
        locationsListView.setAdapter(locationAdapter);

        requestData();
    }

    private void setupAlerts() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        loadFailureAlert = builder.setTitle(R.string.alert_load_failure_title).create();

        builder = new AlertDialog.Builder(this);
        existsAlert = builder.setTitle(R.string.alert_already_exists_title).create();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        saveLocations();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadLocations() {
        dataLoaded = 0;
        locations.clear();
        verificationMap.clear();

        try {
            FileInputStream in = openFileInput(Globals.LOCATIONS_FILE);
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String[] chunks = sb.toString().split(Globals.FILE_DELIMITER);
            for(String chunk : chunks) {
                if(chunk.isEmpty()) continue;
                Location location = new Location(chunk);
                locations.add(location);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(locations.size() == 0) {
                for (String city : defaultCities) {
                    Location location = new Location(city);
                    locations.add(location);
                }
            }
        }
    }

    private void saveLocations() {
        FileOutputStream out;

        try  {
            out = openFileOutput (Globals.LOCATIONS_FILE,  Context.MODE_PRIVATE);
            String allCities = "";
            for(Location location : locations)
            {
                allCities += location.getID() + Globals.FILE_DELIMITER;
            }
            out.write(allCities.getBytes());
            out.close();
        }  catch(Exception e)  {
            e.printStackTrace();
        }
    }

    private void requestData() {
        dataLoaded = 0;
        verificationMap.clear();

        for (Location location : locations) {
            location.requestData(this);
        }
    }

    private void onAddCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_city, null);
        builder.setView(view)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText text = (EditText) view.findViewById(R.id.editText_dialog_add_city_name);
                        Location newLoc = new Location(text.getText().toString());
                        locations.add(newLoc);
                        newLoc.requestData(LocationsActivity.this);
                        dialog.cancel();
                    }
                }).setTitle(R.string.add_city);

        builder.create().show();
    }

    public void displayDetails(Location location) {
        Utils.DEVICE device = Utils.getDeviceType(this);
        switch (device) {
            case MOBILE:
                DetailsView detailsView = new DetailsView(this, null);
                detailsView.setLocation(location);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(detailsView)
                        .setTitle(R.string.weather_details)
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;
            case TABLET:
                DetailsView view = (DetailsView) this.findViewById(R.id.activity_main_detailsView);
                view.setLocation(location);
                break;
        }
    }

    @Override
    public void onLoaded(Location location) {
        if (!location.isLoaded()) {
            if (loadFailureAlert.isShowing()) {
                loadFailureMessage += "/n" + location.getID();
            } else {
                loadFailureMessage = location.getID();
                loadFailureAlert.show();
            }

            loadFailureAlert.setMessage(loadFailureMessage);

            return;
        }

        /* remove duplicates */
        String key = location.getCity();

        if (verificationMap.containsKey(key)) {
            Location dupeVerifier = verificationMap.get(key);
            if (dupeVerifier != location) {
                locations.remove(location);
                locationAdapter.notifyDataSetChanged();
                existsAlert.setMessage(location.getCity());
                existsAlert.show();
                return;
            }
        } else {
            verificationMap.put(key, location);
        }

        /* IF render when everythings ready */
        //if (++dataLoaded == locations.size()) {
            locationAdapter.notifyDataSetChanged();
        //}
    }

    public void onClick_AddCity(View v) {
        onAddCity();
    }

    public void onClick_Refresh(View v) {
        requestData();
    }
}
