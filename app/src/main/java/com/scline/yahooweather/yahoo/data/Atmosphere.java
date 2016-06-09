package com.scline.yahooweather.yahoo.data;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by scline on 6/8/16.
 */
public class Atmosphere {
    /**
     * JSON
     *  "atmosphere": {
     *      "humidity": "65",
     *      "pressure": "1015.0",
     *      "rising": "0",
     *      "visibility": "16.1" },
     */
    final public int humidity;
    final public double pressure;
    final public int rising;
    final public double visibility;

    public Atmosphere(@NonNull JSONObject json) throws JSONException {
        humidity = json.getInt("humidity");
        pressure = json.getDouble("pressure");
        rising = json.getInt("rising");
        visibility = json.getDouble("visibility");
    }
}
