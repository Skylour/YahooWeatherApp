package com.scline.yahooweather.yahoo;

/**
 * Created by scline on 6/8/16.
 */
public class YahooWeatherRSS {

    private static String APP_ID = "dj0yJmk9M0FDYXk0eEhmRmdTJmQ9WVdrOWNsaEpkR2M1TmpJbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1lMg--";
    private static String URL = "http://weather.yahooapis.com/forecastrss";
    private static String START_QUERY = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    private static String END_QUERY = "%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    private static String ICON_START_QUERY = "http://l.yimg.com/a/i/us/we/52/";
    private static String ICON_END_QUERY = ".gif";

    static public String buildLocationURL(String cityName) {
        return START_QUERY + cityName + END_QUERY;
    }

    static public String buildIconURL(int code) {
        return ICON_START_QUERY + code + ICON_END_QUERY;
    }
}
