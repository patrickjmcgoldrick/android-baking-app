package com.mcgoldrick.bakingapp.data;

/**
 * POJO for passing the widget data to widget from configuration activity
 */
public class WidgetData {
    private String mName;
    private String mIngredients;

    public WidgetData(String name, String ingredients) {
        mName = name;
        mIngredients = ingredients;
    }

    public String getName() {
        return mName;
    }

    public String getIngredients() {
        return mIngredients;
    }
}
