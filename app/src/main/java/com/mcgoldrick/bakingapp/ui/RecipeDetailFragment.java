package com.mcgoldrick.bakingapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcgoldrick.bakingapp.R;
import com.mcgoldrick.bakingapp.data.DetailCursor;
import com.mcgoldrick.bakingapp.data.Recipe;
import com.mcgoldrick.bakingapp.data.RecipeContract;
import com.mcgoldrick.bakingapp.data.Recipes;
import com.mcgoldrick.bakingapp.data.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {

    private static String TAG = RecipeDetailFragment.class.getSimpleName();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_RECIPE_INDEX = "recipe_index";

    private Recipe mRecipe;
    private JSONArray mSteps;
    private JSONArray mIngredients;
    private boolean mTwoPane = false;
    private int mRecipeIndex = 0;
    private int mStepIndex = 0;

    /**
     * Manages data for display in recyclerview.
     */
    RecyclerView mRecyclerView;
    DetailAdapter mAdapter;

    String mIngredientsList = "No ingredients found.";


    // Define a new interface OnStepClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;

    // OnStepClickListener interface, calls a method in the host activity named onStepSelected
    public interface OnStepClickListener {
        void onStepSelected(int stepIndex);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

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

    public void setRecipeIndex(int recipeIndex) {
        mRecipeIndex = recipeIndex;
    }
    public void setStepIndex(int stepIndex) {
        mStepIndex = stepIndex;
    }

    public void setTwoPane(boolean twoPane) {
        mTwoPane = twoPane;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

       // Load the saved state (the list of images and list index) if there is one
        if(savedInstanceState != null) {
            mRecipeIndex = savedInstanceState.getInt(ARG_RECIPE_INDEX);
            mStepIndex = savedInstanceState.getInt(VideoPlayerActivityFragment.ARG_STEP_POSITION, 0);
            mTwoPane = savedInstanceState.getBoolean(RecipeDetailActivity.ARG_TWO_PANE);
        }

        Log.e(TAG, "recipe index: " + mRecipeIndex);

        Recipes data = Recipes.getInstance();

        mRecipe = data.getRecipe(mRecipeIndex);
        mSteps = data.getSteps(mRecipeIndex);
        mIngredients = data.getIngredients(mRecipeIndex);
        String title = mRecipe.getName();
        getActivity().setTitle(title);

        Cursor cursor = new DetailCursor(mIngredients, mSteps);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.steps);
        assert mRecyclerView != null;
        mAdapter = new DetailAdapter(this, cursor);
        mRecyclerView.setAdapter(mAdapter);

        TextView ingredientsView = (TextView) rootView.findViewById(R.id.ingredients);
        mIngredientsList = data.buildIngredientsString(mIngredients);
        ingredientsView.setText(mIngredientsList);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putInt(ARG_RECIPE_INDEX, mRecipeIndex);
        currentState.putInt(VideoPlayerActivityFragment.ARG_STEP_POSITION, mStepIndex);
        currentState.putBoolean(RecipeDetailActivity.ARG_TWO_PANE, mTwoPane);
    }

    private class DetailAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final RecipeDetailFragment mParentActivity;
        private Cursor mCursor;

        public DetailAdapter(RecipeDetailFragment parent, Cursor cursor) {
            mParentActivity = parent;
            mCursor = cursor;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_detail_content, parent, false);
            final ViewHolder vh = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int newPosition = vh.getAdapterPosition();
                    if(newPosition != mStepIndex) {
                        mStepIndex = newPosition;  // update the chosen step
                        //forceUIrefresh();  // invalidate ui, to update UI.
                        mCallback.onStepSelected(vh.getAdapterPosition());  // inform Activity of changes
                    }
                }
            });
            return vh;
        }

        private void forceUIrefresh() {
            mRecyclerView.swapAdapter(mAdapter,false);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
            mAdapter.notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            // get either the Ingredients list for the short descriiption of the step.
            holder.mStepTitle.setText(mCursor.getString(
                    RecipeContract.Steps.indexDict.get(RecipeContract.Steps.SHORT_DESC)));

            if(mTwoPane) {
                if (mStepIndex == position) {
                    holder.mCardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    holder.mCardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                }
            }
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            if(mCursor != null) {
                return mCursor.getCount();
            } else {
                return 0;
            }
        }

        /**
         * Method from: https://github.com/udacity/ud851-Exercises/tree/student/Lesson09-ToDo-List
         * When data changes and a re-query occurs, this function swaps the old Cursor
         * with a newly updated Cursor (Cursor c) that is passed in.
         */
        public Cursor swapCursor(Cursor c) {
            // check if this cursor is the same as the previous cursor (mCursor)
            if (mCursor == c) {
                return null; // bc nothing has changed
            }
            Cursor temp = mCursor;
            this.mCursor = c; // new cursor value assigned

            //check if this is a valid cursor, then update the cursor
            if (c != null) {
                this.notifyDataSetChanged();
            }
            return temp;
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mStepTitle;
        final CardView mCardView;

        ViewHolder(View view) {
            super(view);
            mStepTitle = (TextView) view.findViewById(R.id.step_title);
            mCardView = (CardView) view.findViewById(R.id.card_view_step);
        }
    }
}
