package com.mcgoldrick.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.mcgoldrick.bakingapp.R;
import com.mcgoldrick.bakingapp.adapter.RecipeListAdapter;
import com.mcgoldrick.bakingapp.data.Recipe;
import com.mcgoldrick.bakingapp.data.Recipes;
import com.mcgoldrick.bakingapp.data.WidgetData;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class RecipeWidgetConfigureActivity extends AppCompatActivity
                implements LoaderManager.LoaderCallbacks<Recipes>{

    private static final String TAG = RecipeWidgetConfigureActivity.class.getSimpleName();

    private static final String PREFS_CONTEXT = "com.mcgoldrick.bakingapp.widget.RecipeWidget";
    private static final String PREFS_NAME = "RecipeWidget.NAME.";
    private static final String PREFS_INGREDIENTS = "RecipeWidget.INGREDIENTS.";

    private static final String PREF_PREFIX_KEY = "appwidget_";

    public static final int TASK_LOADER_ID = 1;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private Recipes mRecipes;
    private GridView mGridView;

    public RecipeWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, WidgetData widgetData) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_CONTEXT, 0).edit();
        prefs.putString(PREFS_NAME + PREF_PREFIX_KEY + appWidgetId, widgetData.getName());
        prefs.putString(PREFS_INGREDIENTS + PREF_PREFIX_KEY + appWidgetId, widgetData.getIngredients());
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static WidgetData loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_CONTEXT, 0);
        String name = prefs.getString(PREFS_NAME + PREF_PREFIX_KEY + appWidgetId, "NAME_DEFAULT");
        String ingredients = prefs.getString(PREFS_INGREDIENTS + PREF_PREFIX_KEY + appWidgetId, "INGREDIENTS_DEFAULT" );
        return new WidgetData(name, ingredients);
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_CONTEXT, 0).edit();
        prefs.remove(PREFS_NAME + PREF_PREFIX_KEY + appWidgetId);
        prefs.remove(PREFS_INGREDIENTS + PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_configure);

        mGridView = (GridView) findViewById(R.id.images_grid_view);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Context context = RecipeWidgetConfigureActivity.this;

                // When the button is clicked, store the string locally
                if(mRecipes == null) {
                    Log.e(TAG, "Recipes object is null");
                    return;
                }

                WidgetData widgetData = mRecipes.getWidgetData(position);

                saveTitlePref(context, mAppWidgetId, widgetData);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RecipeWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();

            }
        });

        initWidgetParams();

        // load data on background task
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

    }

    private void initWidgetParams() {
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    @NonNull
    @Override
    public Loader<Recipes> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Recipes>(this) {

            // Initialize a Recipes object, this will hold all the task data
            Recipes mRecipes = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mRecipes != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mRecipes);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Nullable
            @Override
            public Recipes loadInBackground() {
                // Will implement to load data
                mRecipes = Recipes.getInstance();

                return mRecipes;
            }

            // deliverResult sends the result of the load, a Recipes, to the registered listener
            public void deliverResult(Recipes data) {
                mRecipes = data;
                super.deliverResult(data);
            }

        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Recipes> loader, Recipes recipes) {
        RecipeListAdapter adapter = new RecipeListAdapter(this, recipes.getRecipeList());
        mRecipes = recipes;
        mGridView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Recipes> loader) {
        // No-op
    }
}

