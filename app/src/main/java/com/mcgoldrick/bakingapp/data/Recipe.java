package com.mcgoldrick.bakingapp.data;

import org.json.JSONArray;

public class Recipe {
    int mId;
    String mName;
    JSONArray mIngredients;
    JSONArray mSteps;
    int mServings;
    String mImage;

    public Recipe (int id, String name, JSONArray ingredients, JSONArray steps, int servings, String image) {
        mId = id;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mServings = servings;
        mImage = image;
    }
}
