package com.mcgoldrick.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcgoldrick.bakingapp.R;
import com.mcgoldrick.bakingapp.data.RecipeContract;
import com.mcgoldrick.bakingapp.data.RecipesCursor;
import com.mcgoldrick.bakingapp.data.Utils;
import com.squareup.picasso.Picasso;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String TAG = RecipeListActivity.class.getSimpleName();

    private static final int TASK_LOADER_ID = 0;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * Manages data for display in recyclerview.
     */
    RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new RecipeAdapter(this, mTwoPane);
        recyclerView.setAdapter(mAdapter);
    }

    private void openDetailPage(View view, int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(RecipeDetailFragment.ARG_RECIPE_INDEX, position);
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {
            Context context = view.getContext();
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_RECIPE_INDEX, position);

            context.startActivity(intent);
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mRecipeCursor = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mRecipeCursor != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mRecipeCursor);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data
                mRecipeCursor = new RecipesCursor();
                return mRecipeCursor;
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mRecipeCursor = data;
                super.deliverResult(data);
            }

        };

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        data.getCount();
        Toast.makeText(this,"Loaded: " + data.getCount() + " records from JSON", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private class RecipeAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private Cursor mCursor;
        private final boolean mTwoPane;

        public RecipeAdapter(RecipeListActivity parent, boolean twoPane) {
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

//        @Override
//        public long getItemId(int position) {
//            mCursor.moveToPosition(position);
//            return mCursor.getLong(
//                    RecipeContract.Recipes.indexDict.get(RecipeContract.Recipes._ID)
//            );
//        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_grid_content, parent, false);
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
            String name = mCursor.getString(
                    RecipeContract.Recipes.indexDict.get(RecipeContract.Recipes.NAME));
            holder.mNameView.setText(name);

            String image = Utils.imageDict.get(name.toLowerCase());
            if (image != null) {
                Picasso.with(RecipeListActivity.this)
                        .load(image)
                        //.placeholder(R.drawable.user_placeholder)
                        //.error(R.drawable.user_placeholder_error)
                        .into(holder.mImageView);
                holder.itemView.setTag(position);
            }
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
        final TextView mNameView;
        final ImageView mImageView;

        ViewHolder(View view) {
            super(view);
            mNameView = (TextView) view.findViewById(R.id.recipe_name);
            mImageView = (ImageView) view.findViewById(R.id.recipe_image);
        }
    }

}
