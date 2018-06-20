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

    private List<Recipe> mRecipeList;
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
        if(mRecipeList != null) {
            return mRecipeList;
        }

        mRecipeList = new ArrayList<Recipe>();
        for (int i = 0; i < mJSONdata.length(); i++) {
            Recipe recipe = this.getRecipe(i);
            mRecipeList.add(recipe);
        }
        return mRecipeList;
    }

    /**
     * Returns a Recipe object.
     * TODO: to be more efficient, we cold check the RecipeList first,
     * TODO: If that exists, we alreadly have an object of the type we want created.
     * @param position
     * @return
     */
    public Recipe getRecipe(int position) {
        JSONObject object = getRecipeJSONObject(position);
        Recipe recipe = null;
        try {
            recipe = new Recipe(position,
                    object.getInt(RecipeContract.Recipes._ID),
                    object.getString(RecipeContract.Recipes.NAME),
                    object.getInt(RecipeContract.Recipes.SERVINGS),
                    object.getString(RecipeContract.Recipes.IMAGE));
        } catch (JSONException jsone) {
            Log.e(TAG, "Failed to parse Recipe from top level of JSON file. Position given: " + position);
        }
        return recipe;
    }

    public JSONObject getRecipeJSONObject(int position) {
        JSONObject object = null;
        try {
            object =  mJSONdata.getJSONObject(position);
        } catch (JSONException jsone) {
            Log.e(TAG, jsone.toString() + "Error reading JSON Recipe object at postion: " + position);
        }
        return object;
    }

    public JSONArray getSteps(int recipeIndex) {
        JSONArray steps = null;
        JSONObject recipe = getRecipeJSONObject(recipeIndex);
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

    public JSONArray getIngredients(int recipeIndex) {
        JSONArray ingredients = null;
        JSONObject recipe = getRecipeJSONObject(recipeIndex);
        try {
            ingredients =  recipe.getJSONArray(RecipeContract.Recipes.INGREDIENTS);
        } catch (JSONException jsone) {
            Log.e(TAG, jsone.toString() + "Error reading ingredients from recipe at position: " + recipeIndex);
        }
        return ingredients;
    }

    public String buildIngredientsString(JSONArray ingredients) {
        StringBuffer buffer = new StringBuffer("Ingredients:\n\n");
        for(int i=0; i < ingredients.length(); i++) {
            JSONObject object = null;
            try {
                object = ingredients.getJSONObject(i);
                if (i > 0) {
                    buffer.append("\n");
                }
                buffer.append("\t" + object.getString("quantity")
                        + " " + object.getString("measure")
                        + " " + object.getString("ingredient"));
            } catch (JSONException jsone) {
                Log.e(TAG, "JSON error while building ingredients list.");
                Log.e(TAG, "List built up to: " + buffer.toString());
            }

        }
        return buffer.toString();

    }

    public WidgetData getWidgetData(int position) {
        Recipe recipe = getRecipe(position);
        JSONArray ingredientsJSON = getIngredients(position);
        String ingredientsString = buildIngredientsString(ingredientsJSON);
        return new WidgetData(recipe.getName(), ingredientsString);
    }

}
