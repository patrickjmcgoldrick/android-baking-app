package com.mcgoldrick.bakingapp.data;

import android.util.Log;

import com.mcgoldrick.bakingapp.remote.RemoteEndpointUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * First pass at Singleton to hold data
 */
public class Recipes {

    final static String TAG = Recipes.class.getSimpleName();

    private List<Recipe> recipeList;
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

    public List<Recipe> getRecipeList() {
        // make sure to only build this once.
        if(recipeList != null) {
            return recipeList;
        }

        recipeList = new ArrayList<Recipe>();
        try {
            for (int i = 0; i < mJSONdata.length(); i++) {
                JSONObject object = mJSONdata.getJSONObject(i);
                Recipe recipe = new Recipe(i,
                        object.getInt(RecipeContract.Recipes._ID),
                        object.getString(RecipeContract.Recipes.NAME),
                        object.getInt(RecipeContract.Recipes.SERVINGS),
                        object.getString(RecipeContract.Recipes.IMAGE));
                recipeList.add(recipe);
            }
        } catch (JSONException jsone) {
            Log.e(TAG, "Failed to parse Recipes from top level of JSON file.");
        }
        return recipeList;
    }

    public JSONObject getRecipe(int position) {
        JSONObject object = null;
        try {
            object =  mJSONdata.getJSONObject(position);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error reading content.", jsone);
        }
        return object;
    }

    private JSONArray getSteps(int recipeIndex) {
        JSONArray steps = null;
        JSONObject recipe = getRecipe(recipeIndex);
        try {
            steps =  recipe.getJSONArray(RecipeContract.Recipes.STEPS);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error reading content.", jsone);
        }
        return steps;
    }

    public int numberOfSteps(int recipeIndex) {
        JSONArray steps = getSteps(recipeIndex);
        if (steps != null) {
            return steps.length();
        }
        return 0;
    }

    public JSONObject getStep(int recipeIndex, int stepIndex) {
        JSONObject step = null;
        JSONArray steps =  getSteps(recipeIndex);
        try {
            step = steps.getJSONObject(stepIndex);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error reading content.", jsone);
        }
        return step;
    }
}
