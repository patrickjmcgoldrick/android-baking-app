package com.mcgoldrick.bakingapp.ui;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcgoldrick.bakingapp.R;
import com.mcgoldrick.bakingapp.data.RecipeContract;
import com.mcgoldrick.bakingapp.data.Recipes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private Recipes data;
    private int position = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = Recipes.getInstance();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            position = getArguments().getInt(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                JSONObject object = data.getRow(position);
                String title = "default";
                try {
                    title = object.getString(RecipeContract.Recipes.NAME);
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

        // Show the dummy content as text in a TextView.
        if (data != null) {
            //((TextView) rootView.findViewById(R.id.recipe_detail)).setText(mItem.details);
            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText("Postion #" + position);
        }

        return rootView;
    }
}
