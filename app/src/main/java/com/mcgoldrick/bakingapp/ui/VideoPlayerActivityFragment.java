package com.mcgoldrick.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoPlayerActivityFragment extends Fragment {

    private static final String TAG = VideoPlayerActivityFragment.class.getSimpleName();

    /** Media Player needs it's state saved on rotation, these variables refer to those values */
    private static final String MEDIA_PLAYER_STATE = "media_player_state";
    private static final String MEDIA_PLAYER_POSITION = "media_player_position";

    /** Current recipe step reference variable */
    public static final String ARG_STEP_POSITION = "step_position";

    private int mRecipeIndex = 0;
    private int mStepIndex = 0;

    private JSONObject mRecipeStep;

    int mNumberOfSteps = 0;

    // ExoPlayer variables
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private MediaSource videoSource;
    private TrackSelector trackSelector;

    // Thumbnail View
    private ImageView mThumbnailView;

    // Navigation buttons
    private Button mButtonNext;
    private Button mButtonPrevious;

    // Callback in the host activity
    private OnStepClickListener mCallback;

    // Bundle savedState
    Bundle mSavedState;

    /** Default Contructor required for the host Activity to instanciate */
    public VideoPlayerActivityFragment() {
    }

    /** Setter for chosen Recipe, based on index */
    public void setRecipeIndex(int recipeIndex) {
        mRecipeIndex = recipeIndex;
    }

    /** Setter for chosen Recipe Step, based on index */
    public void setStepIndex(int stepindex) {
        mStepIndex = stepindex;
    }


    /**
     * Init layout elements and attach appropriate media
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);

        Recipes data = Recipes.getInstance();
        // Load the saved state (the list of images and list index) if there is one
        if (savedInstanceState != null) {
            mRecipeIndex = savedInstanceState.getInt(RecipeDetailFragment.ARG_RECIPE_INDEX);
            mStepIndex = savedInstanceState.getInt(ARG_STEP_POSITION);
        }

        Recipe recipe = data.getRecipe(mRecipeIndex);
        getActivity().setTitle(recipe.getName());

        mRecipeStep = data.getStep(mRecipeIndex, mStepIndex);
        mNumberOfSteps = data.numberOfSteps(mRecipeIndex);

        mButtonPrevious = (Button) rootView.findViewById(R.id.previous_step);
        mButtonNext = (Button) rootView.findViewById(R.id.next_step);

        if (mButtonPrevious != null) { // test that button exist in UI
            // disable navigation buttons, if approriate
            if (mStepIndex == 0) {
                mButtonPrevious.setEnabled(false);
            }
            if (mStepIndex >= mNumberOfSteps - 1) {
                mButtonNext.setEnabled(false);
            }
            if (mButtonPrevious.isEnabled()) {
                mButtonPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onStepSelected(mStepIndex - 1);
                    }
                });
            }

            if (mButtonNext.isEnabled()) {
                mButtonNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onStepSelected(mStepIndex + 1);
                    }
                });
            }
        }

        // setup Instruction TextView
        TextView instructionView = (TextView) rootView.findViewById(R.id.instructions);
        if (instructionView != null) { // test ui item exists
            String instructions = "";
            try {
                instructions = mRecipeStep.getString(RecipeContract.Steps.INSTRUCTION);
            } catch (JSONException jsone) {
                Log.e(TAG, "Error getting instructions from JSON object.");
            }
            instructionView.setText(instructions);

        }

        mPlayerView = (PlayerView) rootView.findViewById(R.id.player_view);
        mThumbnailView = (ImageView) rootView.findViewById(R.id.media_player_overlay);

        mSavedState = savedInstanceState;

        return  rootView;
    }

    /**
     * Separate method to allow media player initialization to happen
     * onStart or onResume based on SDK version.
     */
    private void initializeMediaFrame() {

        String videoURL = "";
        try {
            videoURL = mRecipeStep.getString(RecipeContract.Steps.VIDEO_URL);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error getting videoURL from JSON object.");
        }

        String thumbnailURL = "";
        try {
            thumbnailURL = mRecipeStep.getString(RecipeContract.Steps.THUMBNAIL_URL);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error getting thumbnailURL from JSON object.");
        }

        // if video is provided, load and play the video
        if(videoURL != null && !"".equals(videoURL)) {
            // Initialize the player.
            initializePlayer(Uri.parse(videoURL));

        } else { // if no video, show thumbnail image, if provided
            if (thumbnailURL != null && !"".equals(thumbnailURL)) {
                Picasso.with(mThumbnailView.getContext())
                        .load(thumbnailURL)
                        .error(R.drawable.error_loading_thumbnail)
                        .into(mThumbnailView);

            } else {  // otherwise show default image
                Picasso.with(mThumbnailView.getContext())
                        .load(R.drawable.no_media_provided)
                        .into(mThumbnailView);
            }
        }
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

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaUri);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);
            if(mSavedState != null) {
                loadSavedMediaPlayerState(mSavedState);
            } else {
                mExoPlayer.setPlayWhenReady(true);
            }
        } else {  // not clear this code is needed.
            if(mSavedState != null) {
                loadSavedMediaPlayerState(mSavedState);
            } else {
                Log.e(TAG, "Media Player existed at onCreateLayout, but not state was saved to setup player.");
            }

        }
    }

    /**
     * Attempt to load previous media player state (media position and pause/play status)
     *
     * @param savedInstanceState
     */
    private void loadSavedMediaPlayerState(Bundle savedInstanceState) {
        long playerPosition = savedInstanceState.getLong(MEDIA_PLAYER_POSITION, 0);
        boolean playerState = savedInstanceState.getBoolean(MEDIA_PLAYER_STATE, false);
        mExoPlayer.seekTo(playerPosition);
        mExoPlayer.setPlayWhenReady(playerState);
    }

    /**
     * Initialize media player based on SDK version
     */
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializeMediaFrame();
        }
    }

    /**
     * Initialize media player based on SDK version
     */
    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializeMediaFrame();
        }
    }

    /**
     * Cleanup media player resources based on SDK version
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    /**
     * Cleanup media player resources based on SDK version
     */
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
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

    /**
     * Save the current values for Recipe chosen and Step in recipe process.  Also,
     * save media player position and pause/play state.
     *
     * @param currentState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putInt(RecipeDetailFragment.ARG_RECIPE_INDEX, mRecipeIndex);
        currentState.putInt(ARG_STEP_POSITION, mStepIndex);

        // media player state variables
        if(mExoPlayer != null) {
           long playerPosiition =  mExoPlayer.getCurrentPosition();
           boolean mediaPlayerReady = mExoPlayer.getPlayWhenReady();
           currentState.putLong(MEDIA_PLAYER_POSITION, playerPosiition);
           currentState.putBoolean(MEDIA_PLAYER_STATE, mediaPlayerReady);
        } else {
            currentState.putLong(MEDIA_PLAYER_POSITION, 0);
            currentState.putBoolean(MEDIA_PLAYER_STATE, false);
        }
    }

    /**
     * Wireup callback and verify that parent Activity implements require interface
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

}
