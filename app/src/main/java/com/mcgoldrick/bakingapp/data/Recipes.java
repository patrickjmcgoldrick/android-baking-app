package com.mcgoldrick.bakingapp.data;

import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.mcgoldrick.bakingapp.remote.RemoteEndpointUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * First pass at Singleton to hold data
 */
public class Recipes {

    private JSONArray mJSONdata;

    private static Recipes data;

    public static Recipes getInstance() {
        if (data == null) {
            data = new Recipes(RemoteEndpointUtil.fetchJsonArray());
        }
        return data;
    }

    private Recipes(JSONArray jsonData) {
        mJSONdata = jsonData;
    }

    public int getCount() {
        if (mJSONdata == null) {
            return 0;
        }
        return mJSONdata.length();
    }

    public JSONObject getRow(int position) {
        JSONObject object = null;
        try {
            object =  mJSONdata.getJSONObject(position);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error reading content.", jsone);
        }
        return object;
    }
}
