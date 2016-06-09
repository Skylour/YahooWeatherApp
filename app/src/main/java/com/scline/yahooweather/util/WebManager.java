package com.scline.yahooweather.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by scline on 6/8/16.
 */
public class WebManager {

    private Context context;
    private RequestQueue requestQueue;

    public final RequestQueue getRequestQueue()
    {
        return requestQueue;
    }

    private static final String TAG = "webmanager"; // for queue cancellation
    private static final String CELSIUS = "&u=c";
    private static final String FARENHEIGHT = "&u=f";
    private static final String YAHOO_WEATHER_RSS = "http://weather.yahooapis.com/forecastrss";

    public enum ERROR
    {
        NONE, INVALID_CITY, UNKNOWN
    }

    /**
     * Singleton
     */
    static private WebManager instance = null;

    static public WebManager instance(@NonNull Context context)
    {
        if (instance == null)
        {
            instance = new WebManager();
            instance.context = context;
            instance.initialize();
        } else {
            instance.context = context;
        }

        return instance;
    }

    private WebManager() {}
    private void initialize() { requestQueue = Volley.newRequestQueue(context);}

    static public String encodeURL(String str)
    {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(str, "utf-8");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        return encoded;
    }

    public void requestJSON(String url, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
        request.setTag(TAG);
        requestQueue.add(request);
    }

    static public ERROR checkError(String response)
    {
        if(response.contains("Error:"))
        {
            return ERROR.UNKNOWN;
        }

        return ERROR.NONE;
    }

}
