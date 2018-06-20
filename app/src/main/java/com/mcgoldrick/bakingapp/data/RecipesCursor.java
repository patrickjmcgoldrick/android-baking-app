package com.mcgoldrick.bakingapp.data;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class RecipesCursor implements Cursor {

    Recipes mRecipeData;
    int mPosition;

//    public RecipesCursor(Recipes recipes) {
//        mRecipeData = recipes;
//    }

    public RecipesCursor() {
        mRecipeData = Recipes.getInstance();
        mPosition = 0;
    }

    @Override
    public int getCount() {
        if(mRecipeData == null) {
            return 0;
        }
        return mRecipeData.getCount();
    }

    @Override
    public int getPosition() {
        return mPosition;
    }

    @Override
    public boolean move(int offset) {
        return moveToPosition(offset + mPosition);
    }

    @Override
    public boolean moveToPosition(int position) {
        if (position < getCount()) {
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
        if(mPosition - 1 > 0) {
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
        return RecipeContract.Recipes.indexDict.get(columnName);
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        return RecipeContract.Recipes.indexDict.get(columnName);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return RecipeContract.Recipes.nameLookup[columnIndex];
    }

    @Override
    public String[] getColumnNames() {
        return RecipeContract.Recipes.nameLookup;
    }

    @Override
    public int getColumnCount() {
        return RecipeContract.Recipes.nameLookup.length;
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        return new byte[0];
    }

    @Override
    public String getString(int columnIndex) {
        String value = null;
        JSONObject object = mRecipeData.getRecipeJSONObject(mPosition);
        try {
            value = object.getString(getColumnName(columnIndex));
        } catch (JSONException jsone) {
            Log.e(TAG, "Error reading row: " + mPosition + ", index: " + columnIndex);
        }
        return value;
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
