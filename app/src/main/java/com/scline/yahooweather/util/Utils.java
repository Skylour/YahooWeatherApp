package com.scline.yahooweather.util;

import android.content.Context;

import com.scline.yahooweather.R;

/**
 * Created by scline on 6/8/16.
 */
public class Utils {
    public enum DEVICE {
        MOBILE, TABLET, UNKNOWN;

        public boolean isType(String string) {
            if(this.toString().equalsIgnoreCase(string)) return true;
            return false;
        }

        static public DEVICE getValue(String string) {
            if(DEVICE.MOBILE.isType(string)) return DEVICE.MOBILE;
            if(DEVICE.TABLET.isType(string)) return DEVICE.TABLET;

            return DEVICE.UNKNOWN;
        }
    }

    static private DEVICE device = null;

    static public DEVICE getDeviceType(Context context) {
        if(device == null) {
            String value = context.getResources().getString(R.string.device);
            device = DEVICE.getValue(value);
        }

        return device;
    }
}
