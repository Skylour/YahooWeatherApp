package com.scline.yahooweather.yahoo.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.scline.yahooweather.data.JSONLoader;
import com.scline.yahooweather.util.WebManager;
import com.scline.yahooweather.util.WebManager.ERROR;
import com.scline.yahooweather.yahoo.YahooWeatherRSS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scline on 6/8/16.
 *
 * Location is menu_main container for all the parsed JSON data
 */
public class Location extends JSONLoader {

    private static List<LocationsLoadedListener> listeners = new ArrayList<>();

    private String id;

    /**
     * JSON
     *  "location": {
     *      "city": "Nome",
     *      "country": "United States",
     *      "region": " AK" },
     */
    private String city = "Loading...";
    private String country = "N/A";
    private String region = "N/A";

    private String title = "Loading...";
    private double latitude = 0.0;
    private double longitude = 0.0;

    final private ArrayList<DayForecast> forecast = new ArrayList<>();
    private Wind wind;
    private Units units;
    private Atmosphere atmosphere;
    private Astronomy astronomy;
    private Condition condition;

    public Location(@NonNull  String cityName) {
        this.id = cityName;
    }

    @Override
    public void requestData(Context context) {
        String encoded = WebManager.encodeURL(id);
        String url = YahooWeatherRSS.buildLocationURL(encoded);
        isLoaded = false;
        WebManager.instance(context).requestJSON(url, this, this);
    }

    @Override
    public void onResponse(JSONObject json) {
        ERROR error = WebManager.checkError(json.toString());
        if(error != ERROR.NONE)
        {
            Log.e("ERR", json.toString());
            return;
        }

        forecast.clear();

        try {
            /*
             * query: { results: { channel: {}}}
             */
            JSONObject queryJSON = json.getJSONObject("query");
            JSONObject resultsJSON = queryJSON.getJSONObject("results");
            JSONObject channelJSON = resultsJSON.getJSONObject("channel");

            /*
             * channel: { units:{}, location:{}, wind:{}, atmosphere:{}, astronomy:{}, item:{} }
             */
            units = new Units(channelJSON.getJSONObject("units"));
            JSONObject locJSON = channelJSON.getJSONObject("location");
            city = locJSON.getString("city");
            country = locJSON.getString("country");
            region = locJSON.getString("region");
            wind = new Wind(channelJSON.getJSONObject("wind"));
            atmosphere = new Atmosphere(channelJSON.getJSONObject("atmosphere"));
            astronomy = new Astronomy(channelJSON.getJSONObject("astronomy"));

            /*
             * item: { title:, lat:, long:, condition:{}, forecast:[]}
             */
            JSONObject itemJSON = channelJSON.getJSONObject("item");
            title = itemJSON.getString("title");
            latitude = itemJSON.getDouble("lat");
            longitude = itemJSON.getDouble("long");
            condition = new Condition(itemJSON.getJSONObject("condition"));
            JSONArray forecastJSON = itemJSON.getJSONArray("forecast");
            for(int i=0; i<forecastJSON.length(); i++) {
                DayForecast dayForecast = new DayForecast((JSONObject)forecastJSON.get(i));
                forecast.add(dayForecast);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        isLoaded = true;

        for (LocationsLoadedListener listener : listeners) {
            listener.onLoaded(this);
        }
    }

    static public void addListener(LocationsLoadedListener listener) {
        if(listeners.contains(listener)) return;
        listeners.add(listener);
    }

    static public void removeListener(LocationsLoadedListener listener) {
        if(listeners.contains(listener)) listeners.remove(listener);
    }

    public interface LocationsLoadedListener {
        void onLoaded(Location location);
    }

    /**
     * Accessors/Mutators
     */
    public final String getID() {return id;}
    public final String getCity() {return city;}
    public final String getCountry() {return country;}
    public final String getRegion() {return region;}

    public final String getTitle() {return title;}
    public final double getLatitude() {return latitude;}
    public final double getLongitude() {return longitude;}

    public final ArrayList<DayForecast> getForecast() {return forecast;}
    public final Wind getWind() {return wind;}
    public final Units getUnits() {return units;}
    public final Atmosphere getAtmosphere() {return atmosphere;}
    public final Astronomy getAstronomy() {return astronomy;}
    public final Condition getCondition() {return condition;}
}
