package com.scline.yahooweather.yahoo.data;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by scline on 6/8/16.
 */
public class Units {
    /**
     * JSON
     *  "units": {
     *      "distance": "mi",
     *      "pressure": "in",
     *      "speed": "mph",
     *      "temperature": "F" },
     */
    final public String distance;
    final public String pressure;
    final public String speed;
    final public String temperature;

    public Units(@NonNull JSONObject json) throws JSONException {
        distance = json.getString("distance");
        pressure = json.getString("pressure");
        speed = json.getString("speed");
        temperature = json.getString("temperature");
    }
}
