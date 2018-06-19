package com.mcgoldrick.bakingapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcgoldrick.bakingapp.R;
import com.mcgoldrick.bakingapp.data.Recipe;
import com.mcgoldrick.bakingapp.data.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<Recipe> mRecipes;

    public RecipeListAdapter(Activity activity, List<Recipe> recipes) {
        mActivity = activity;
        mRecipes = recipes;
    }

    @Override
    public int getCount() {
        if(mRecipes != null) {
            return mRecipes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(position < getCount() && position >= 0) {
            return mRecipes.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return mRecipes.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mActivity.getLayoutInflater().inflate(R.layout.activity_grid_content, viewGroup, false);
        }

        final Recipe recipe = (Recipe) getItem(position);

        // Set the TextView's contents
        TextView nameView = (TextView) view.findViewById(R.id.recipe_name);
        String name = recipe.getName();
        nameView.setText(name);

        String imageURL = recipe.getImage();
        if(imageURL == null || "".equals(imageURL)) {
            // attempt to use some detault images is none was supplied
            imageURL = Utils.imageDict.get(name.toLowerCase());
        }

        if(imageURL != null && !"".equals(imageURL)) {
            // Load the thumbnail image
            ImageView imageView = (ImageView) view.findViewById(R.id.recipe_image);
            Picasso.with(imageView.getContext()).load(imageURL).into(imageView);
        }

        return view;
    }

}
