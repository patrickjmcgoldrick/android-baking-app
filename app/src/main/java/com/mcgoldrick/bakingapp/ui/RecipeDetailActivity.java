package com.mcgoldrick.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.mcgoldrick.bakingapp.R;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailFragment.OnStepClickListener {

    private final static String TAG = RecipeDetailFragment.class.getSimpleName();

    public final static String ARG_TWO_PANE = "two_pane";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private String mRecipeName;
    private int mRecipeIndex = 0;
    private int mStepIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            mRecipeIndex = getIntent().getIntExtra(RecipeDetailFragment.ARG_RECIPE_INDEX, 0);
            Log.e(TAG, "recipe index: " + mRecipeIndex);

            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            detailFragment.setRecipeIndex(mRecipeIndex);
            detailFragment.setStepIndex(mStepIndex);
            detailFragment.setTwoPane(mTwoPane);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, detailFragment)
                    .commit();

            if(findViewById(R.id.media_container) != null) {
                mTwoPane = true;

                VideoPlayerActivityFragment videoFragment = new VideoPlayerActivityFragment();
                videoFragment.setRecipeIndex(mRecipeIndex);
                videoFragment.setStepIndex(mStepIndex);
                fragmentManager.beginTransaction()
                        .add(R.id.media_container, videoFragment)
                        .commit();
            } else {
                mTwoPane = false;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepSelected(int newStepIndex) {
        if(newStepIndex == mStepIndex) { // nothing is changing
            return;
        }
        mStepIndex = newStepIndex;
        if (mTwoPane) {
            // in TwoPane mode, replace fragment
            VideoPlayerActivityFragment videoFragment = new VideoPlayerActivityFragment();
            videoFragment.setRecipeIndex(mRecipeIndex);
            videoFragment.setStepIndex(mStepIndex);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.media_container, videoFragment)
                    .commit();
        } else {
            // single pane, phone, send intent for new Activity.
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_RECIPE_INDEX, mRecipeIndex);
            intent.putExtra(VideoPlayerActivityFragment.ARG_STEP_POSITION, mStepIndex);
            startActivity(intent);
        }

    }
}
