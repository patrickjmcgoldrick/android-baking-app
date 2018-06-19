package com.mcgoldrick.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.mcgoldrick.bakingapp.R;
import com.mcgoldrick.bakingapp.adapter.RecipeListAdapter;
import com.mcgoldrick.bakingapp.data.Recipe;
import com.mcgoldrick.bakingapp.data.Recipes;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Recipes> {

    final static String TAG = MainActivity.class.getSimpleName();

    public static final int TASK_LOADER_ID = 0;

    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        mGridView = (GridView) findViewById(R.id.images_grid_view);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "Click on position: " + position);
                Toast.makeText(MainActivity.this, "Click on position: " + position, Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailFragment.ARG_RECIPE_INDEX, position);

                startActivity(intent);

            }
        });

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<Recipes> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Recipes>(this) {

            // Initialize a Cursor, this will hold all the task data
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

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Recipes data) {
                mRecipes = data;
                super.deliverResult(data);
            }

        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Recipes> loader, Recipes recipes) {
        RecipeListAdapter adapter = new RecipeListAdapter(this, recipes.getRecipeList());
        mGridView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Recipes> loader) {

    }

}
