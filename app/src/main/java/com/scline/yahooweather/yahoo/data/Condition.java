package com.scline.yahooweather.yahoo.data;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by scline on 6/8/16.
 */
public class Condition {
    /**
     * JSON
     *  "condition": {
     *      "code": "26",
     *      "date": "Wed, 08 Jun 2016 07:00 AM AKDT",
     *      "temp": "39",
     *      "text": "Cloudy" },
     */
    final public int code;
    final public String date;
    final public int temp;
    final public String text;

    public Condition(@NonNull JSONObject json) throws JSONException {
        code = json.getInt("code");
        date = json.getString("date");
        temp = json.getInt("temp");
        text = json.getString("text");
    }
}
