package com.scline.yahooweather.yahoo.data;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by scline on 6/8/16.
 */
public class Wind {
    /**
     * JSON
     *  "wind": {
     *      "chill": "32",
     *      "direction": "23",
     *      "speed": "14" },
     */
    final public int chill;
    final public int direction;
    final public int speed;

    public Wind(@NonNull JSONObject json) throws JSONException {
        chill = json.getInt("chill");
        direction = json.getInt("direction");
        speed = json.getInt("speed");
    }
}
