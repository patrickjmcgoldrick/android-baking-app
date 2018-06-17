package com.mcgoldrick.bakingapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcgoldrick.bakingapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoPlayerActivityFragment extends Fragment {

    public VideoPlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_player, container, false);
    }
}
