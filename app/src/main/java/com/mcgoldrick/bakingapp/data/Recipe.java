package com.mcgoldrick.bakingapp.data;

import org.json.JSONArray;

/**
 * POJO for Recipe base data
 */
public class Recipe {
    private int mIndex;
    private int mId;
    private String mName;
    private int mServings;
    private String mImage;

    public Recipe (int index, int id, String name, int servings, String image) {
        mIndex = index;
        mId = id;
        mName = name;
        mServings = servings;
        mImage = image;
    }

    public int getIndex() {
        return mIndex;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getServings() {
        return mServings;
    }

    public String getImage() {
        return mImage;
    }
}
