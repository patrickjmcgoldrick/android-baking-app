package com.mcgoldrick.bakingapp.remote;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * From: https://github.com/udacity/xyz-reader-starter-code
 *
 * Downloads and parses JSON date for use in app.
 */
public class RemoteEndpointUtil {
    private static final String TAG = "RemoteEndpointUtil";

    private RemoteEndpointUtil() {
    }

    /**
     * Parser. turns JSON into an object tree that we can traverse.
     *
     * @return tree of JSON data
     */
    public static JSONArray fetchJsonArray() {
        String itemsJson = null;
        try {

            Log.e(TAG, "base url: " + Config.BASE_URL);

            itemsJson = fetchPlainText(Config.BASE_URL);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching items JSON", e);
            return null;
        }

        Log.e(TAG, itemsJson.substring(0,100));


        // Parse JSON
        try {
            JSONTokener tokener = new JSONTokener(itemsJson);
            Object val = tokener.nextValue();
            if (!(val instanceof JSONArray)) {
                throw new JSONException("Expected JSONArray");
            }
            return (JSONArray) val;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing items JSON", e);
        }

        return null;
    }

    /**
     * Makes a network connection to url, downloads data and returns it as a String.
     *
     * @param url
     * @return data in String format
     * @throws IOException
     */
    static String fetchPlainText(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
