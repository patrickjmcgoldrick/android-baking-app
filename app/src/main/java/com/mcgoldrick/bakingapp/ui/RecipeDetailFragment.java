package com.mcgoldrick.bakingapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcgoldrick.bakingapp.R;
import com.mcgoldrick.bakingapp.data.DetailCursor;
import com.mcgoldrick.bakingapp.data.RecipeContract;
import com.mcgoldrick.bakingapp.data.Recipes;

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
    public static final String ARG_ITEM_ID = "item_id";

    private JSONObject recipeRow;
    private int position = 0;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * Manages data for display in recyclerview.
     */
    DetailAdapter mAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Recipes data = Recipes.getInstance();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            position = getArguments().getInt(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                recipeRow = data.getRow(position);
                String title = "default";
                try {
                    title = recipeRow.getString(RecipeContract.Recipes.NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appBarLayout.setTitle(title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        JSONArray ingredients = null;
        JSONArray steps = null;

        try {
            ingredients = recipeRow.getJSONArray(RecipeContract.Recipes.INGREDIENTS);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error loading Ingredients.");
        }

        try {
            steps = recipeRow.getJSONArray(RecipeContract.Recipes.STEPS);
        } catch (JSONException jsone) {
            Log.e(TAG, "Error loading Steps.");
        }

        Cursor cursor = new DetailCursor(ingredients, steps);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_detail);
        assert recyclerView != null;
        mAdapter = new DetailAdapter(this, mTwoPane, cursor);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private class DetailAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final RecipeDetailFragment mParentActivity;
        private Cursor mCursor;
        private final boolean mTwoPane;

        public DetailAdapter(RecipeDetailFragment parent, boolean twoPane, Cursor cursor) {
            mParentActivity = parent;
            mTwoPane = twoPane;
            mCursor = cursor;
        }

        // TODO: maybe get rid of this
//        @Override
//        public long getItemId(int position) {
//            mCursor.moveToPosition(position);
//            return mCursor.getLong(
//                    RecipeContract.Recipes.indexDict.get(RecipeContract.Recipes._ID)
//            );
//        }
        private void openDetailPage(View view, int position) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putInt(RecipeDetailFragment.ARG_ITEM_ID, position);
                VideoPlayerActivityFragment fragment = new VideoPlayerActivityFragment();
                fragment.setArguments(arguments);
                //TODO: fixe this line.
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.recipe_detail_container, fragment)
//                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, position);

                context.startActivity(intent);
            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_detail_content, parent, false);
            final ViewHolder vh = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDetailPage(view, vh.getAdapterPosition());
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            // get either the Ingredients list for the short descriiption of the step.
            holder.mStepTitle.setText(mCursor.getString(
                    RecipeContract.Steps.indexDict.get(RecipeContract.Steps.SHORT_DESC)));

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

        ViewHolder(View view) {
            super(view);
            mStepTitle = (TextView) view.findViewById(R.id.step_title);
        }
    }
}
