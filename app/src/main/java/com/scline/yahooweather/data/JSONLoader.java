package com.scline.yahooweather.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by scline on 6/8/16.
 */
public abstract class JSONLoader implements Response.Listener<JSONObject>, Response.ErrorListener{
    protected boolean isLoaded = false;

    abstract public void requestData(Context context);

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e("ERR", this.toString() + ": " + volleyError.getMessage());
        System.out.println(this.toString() + volleyError.getMessage());
    }

    /*
     * Accessors/Mutators
     */
    public final boolean isLoaded() {return isLoaded;}
}
