package com.scline.yahooweather.yahoo.data;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by scline on 6/8/16.
 */
public class DayForecast {
    /**
     * JSON
     * { "code": "39",
     *   "date": "08 Jun 2016",
     *   "day": "Wed",
     *   "high": "50",
     *   "low": "39",
     *   "text": "Scattered Showers" },
     */
    final public int code;
    final public String date;
    final public String day;
    final public int low;
    final public int high;
    final public String text;

    public DayForecast(@NonNull JSONObject json) throws JSONException {
        code = json.getInt("code");
        date = json.getString("date");
        day = json.getString("day");
        low = json.getInt("low");
        high = json.getInt("high");
        text = json.getString("text");
    }
}
