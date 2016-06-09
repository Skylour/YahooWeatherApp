package com.scline.yahooweather.yahoo.data;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by scline on 6/8/16.
 */
public class Astronomy {
    /**
     * JSON
     *  "astronomy": {
     *      "sunrise": "1:32 am",
     *      "sunset": "4:31 am" },
     */
    final public String sunrise;
    final public String sunset;

    public Astronomy(@NonNull JSONObject json) throws JSONException {
        sunrise = json.getString("sunrise");
        sunset = json.getString("sunset");
    }
}
