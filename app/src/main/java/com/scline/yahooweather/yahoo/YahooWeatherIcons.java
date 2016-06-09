package com.scline.yahooweather.yahoo;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by scline on 6/8/16.
 */
public class YahooWeatherIcons implements ImageLoader.ImageCache
{
    /**
     * Singleton
     */
    static YahooWeatherIcons instance = null;
    private YahooWeatherIcons() {}
    static public YahooWeatherIcons instance()
    {
        if(instance == null)
            instance = new YahooWeatherIcons();
        return instance;
    }

    LruCache<String, Bitmap> iconCache = new LruCache<>(15);

    @Override
    public Bitmap getBitmap(String url) {
        return iconCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        iconCache.put(url, bitmap);
    }
}
