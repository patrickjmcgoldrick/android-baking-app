package com.mcgoldrick.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mcgoldrick.bakingapp.R;

public class VideoPlayerActivity extends AppCompatActivity
                    implements OnStepClickListener {

    int mRecipeIndex;
    int mStepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecipeIndex = getIntent().getIntExtra(RecipeDetailFragment.ARG_RECIPE_INDEX, 0);
        mStepIndex = getIntent().getIntExtra(VideoPlayerActivityFragment.ARG_STEP_POSITION, 0);

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
            replaceVideoFragment();
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
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_RECIPE_INDEX, mRecipeIndex);

            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepSelected(int stepIndex) {
        mStepIndex = stepIndex;
        replaceVideoFragment();
    }

    private void replaceVideoFragment() {
        VideoPlayerActivityFragment videoFragment = new VideoPlayerActivityFragment();
        videoFragment.setRecipeIndex(mRecipeIndex);
        videoFragment.setStepIndex(mStepIndex);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.media_container, videoFragment)
                .commit();
    }
}

