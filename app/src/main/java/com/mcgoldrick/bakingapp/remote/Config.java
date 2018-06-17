package com.mcgoldrick.bakingapp.remote;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * From: https://github.com/udacity/xyz-reader-starter-code
 */
public class Config {
    public static final URL BASE_URL;
    private static String TAG = Config.class.toString();

    static {
        URL url = null;
        try {
            url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json" );
        } catch (MalformedURLException ignored) {
            // TODO: throw a real error
            Log.e(TAG, "Please check your internet connection.");
        }

        BASE_URL = url;
    }
}
