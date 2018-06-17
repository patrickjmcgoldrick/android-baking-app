package com.mcgoldrick.bakingapp.data;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


/**
 * An adapter between the JSON data structures and the
 * Cursor Interface. 
 * We need to keep track of both the Ingredients and the
 * steps to create the recipe.
 */
public class DetailCursor implements Cursor {

    JSONArray mIngredients;
    JSONArray mSteps;
    int mPosition;
    String mIngredientsList = "No ingredients found.";

    public DetailCursor(JSONArray ingredients, JSONArray steps) {
        mIngredients = ingredients;
        mSteps = steps;
        mPosition = 0;
    }

    @Override
    public int getCount() {
        // a separate view for each step
        // These steps need to be clickable,
        // if there is a video associated.
        if(mSteps != null) {
            return mSteps.length();
        }
        return 0;
    }

    @Override
    public int getPosition() {
        return mPosition;
    }

    @Override
    public boolean move(int offset) {
        int newPosition = offset + mPosition;
        if(newPosition < getCount() &&
                newPosition >= 0) {
            mPosition = newPosition;
            return true;
        }
        return false;
    }

    @Override
    public boolean moveToPosition(int position) {
        if (position < getCount() &&
                position >= 0) {
            mPosition = position;
            return true;
        }
        return false;
    }

    @Override
    public boolean moveToFirst() {
        if(getCount() > 0) {
            mPosition = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean moveToLast() {
        if(getCount() > 0) {
            mPosition = getCount() - 1;
            return true;
        }
        return false;
    }

    @Override
    public boolean moveToNext() {
        if(mPosition + 1 < getCount()) {
            mPosition += 1;
            return true;
        }
        return false;
    }

    @Override
    public boolean moveToPrevious() {
        if(mPosition - 1 >= 0) {
            mPosition -= 1;
            return true;
        }
        return false;
    }

    @Override
    public boolean isFirst() {
        if(mPosition == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLast() {
        if(mPosition == getCount() -1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isBeforeFirst() {
        if(mPosition < 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAfterLast() {
        if(mPosition >= getCount()) {
            return true;
        }
        return false;
    }

    @Override
    public int getColumnIndex(String columnName) {
        return RecipeContract.Steps.indexDict.get(columnName);
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        return RecipeContract.Steps.indexDict.get(columnName);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return RecipeContract.Steps.nameLookup[columnIndex];
    }

    @Override
    public String[] getColumnNames() {
        return RecipeContract.Steps.nameLookup;
    }

    @Override
    public int getColumnCount() {
        return RecipeContract.Steps.nameLookup.length;
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        return new byte[0];
    }

    @Override
    public String getString(int columnIndex) {
        String value = "";
       // lookup short description in Steps JSONArray
        String shortDesc = null;
        try {
            JSONObject stepRow = mSteps.getJSONObject(mPosition);

            shortDesc = stepRow.getString(RecipeContract.Steps.SHORT_DESC);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error reading row: " + mPosition + ", index: " + columnIndex);
        }
        return shortDesc;

    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

    }

    @Override
    public short getShort(int columnIndex) {
        return 0;
    }

    @Override
    public int getInt(int columnIndex) {
        return 0;
    }

    @Override
    public long getLong(int columnIndex) {
        return 0;
    }

    @Override
    public float getFloat(int columnIndex) {
        return 0;
    }

    @Override
    public double getDouble(int columnIndex) {
        return 0;
    }

    @Override
    public int getType(int columnIndex) {
        return 0;
    }

    @Override
    public boolean isNull(int columnIndex) {
        return false;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public boolean requery() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {

    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {

    }

    @Override
    public Uri getNotificationUri() {
        return null;
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return false;
    }

    @Override
    public void setExtras(Bundle extras) {

    }

    @Override
    public Bundle getExtras() {
        return null;
    }

    @Override
    public Bundle respond(Bundle extras) {
        return null;
    }
}
