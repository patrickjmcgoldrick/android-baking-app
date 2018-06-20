package com.mcgoldrick.bakingapp.ui;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mcgoldrick.bakingapp.R;
import com.mcgoldrick.bakingapp.data.Recipe;
import com.mcgoldrick.bakingapp.data.RecipeContract;
import com.mcgoldrick.bakingapp.data.Recipes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoPlayerActivityFragment extends Fragment {

    private static final String TAG = VideoPlayerActivityFragment.class.getSimpleName();

    public static final String ARG_STEP_POSITION = "step_position";

    private int mRecipeIndex = 0;
    private int mStepIndex = 0;
    private String mRecipeName = "";

    private JSONObject recipeRow;
    private JSONObject mRecipeStep;

    int mNumberOfSteps = 0;

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private MediaSource videoSource;
    TrackSelector trackSelector;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public VideoPlayerActivityFragment() {
    }

    public void setRecipeIndex(int recipeIndex) {
        mRecipeIndex = recipeIndex;
    }

    public void setStepIndex(int stepindex) {
        mStepIndex = stepindex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);

        Recipes data = Recipes.getInstance();
       // Load the saved state (the list of images and list index) if there is one
        if(savedInstanceState != null) {
            mRecipeIndex = savedInstanceState.getInt(RecipeDetailFragment.ARG_RECIPE_INDEX);
            mStepIndex = savedInstanceState.getInt(ARG_STEP_POSITION);
        }

//        if(getArguments() != null) {
//            if (getArguments().containsKey(RecipeDetailFragment.ARG_RECIPE_INDEX)
//                    && getArguments().containsKey(ARG_STEP_POSITION)) {
//
//                mRecipeIndex = getArguments().getInt(RecipeDetailFragment.ARG_RECIPE_INDEX);
//                mStepIndex = getArguments().getInt(ARG_STEP_POSITION);
//            }
//        }

        Recipe recipe = data.getRecipe(mRecipeIndex);
        getActivity().setTitle(recipe.getName());

        mRecipeStep = data.getStep(mRecipeIndex, mStepIndex);
        mNumberOfSteps = data.numberOfSteps(mRecipeIndex);



        TextView instructionView = (TextView) rootView.findViewById(R.id.instructions);
        if(instructionView != null) {
            String instructions = "";
            try {
                instructions = mRecipeStep.getString(RecipeContract.Steps.INSTRUCTION);
            } catch (JSONException jsone) {
                Log.e(TAG, "Error getting instructions from JSON object.");
            }
            instructionView.setText(instructions);

        }

        mPlayerView = (PlayerView) rootView.findViewById(R.id.player_view);
        String videoURL = "";
        try {
            videoURL = mRecipeStep.getString(RecipeContract.Steps.VIDEO_URL);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error getting videoURL from JSON object.");
        }

        if(videoURL != null && !"".equals(videoURL)) {
            // Initialize the player.
            initializePlayer(Uri.parse(videoURL));
        }
        return rootView;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // 1. Create a default TrackSelector
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the player
            mExoPlayer =
                    ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            // Bind the player to the view.
            mPlayerView.setPlayer(mExoPlayer);


            // Prepare the MediaSource.

// Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
// This is the MediaSource representing the media to be played.
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaUri);
// Prepare the player with the source.
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    /**
     * Release ExoPlayer and related resources.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
            videoSource = null;
            trackSelector = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putInt(RecipeDetailFragment.ARG_RECIPE_INDEX, mRecipeIndex);
        currentState.putInt(ARG_STEP_POSITION, mStepIndex);
    }


}
