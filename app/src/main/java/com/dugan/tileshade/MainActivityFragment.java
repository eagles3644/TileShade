package com.dugan.tileshade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivityFragment extends Fragment {

    private AppCompatActivity activity;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get activity action bar
        ActionBar actionBar = activity.getSupportActionBar();

        //Inflate custom action bar
        @SuppressLint("InflateParams") View customActionBarView = inflater.inflate(R.layout.main_action_bar, null);

        //set action bar settings
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        //set custom view
        actionBar.setCustomView(customActionBarView);

        //inflate fragment
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //get mode buttons
        Button btnPlay = (Button) rootView.findViewById(R.id.btn_play);
        Button btnLeaderboard = (Button) rootView.findViewById(R.id.btn_leaderboard);
        Button btnSettings = (Button) rootView.findViewById(R.id.btn_settings);

        //set mode button onclick listeners
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shared prefs
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(rootView.getContext());

                //create intent
                Intent intent = new Intent(v.getContext(), GameActivity.class);

                //intent set mode extra
                intent.putExtra(Constants.PREF_MODE, prefs.getString(Constants.PREF_MODE, Constants.PREF_MODE_DEFAULT));

                //intent set shape extra
                intent.putExtra(Constants.PREF_SHAPE,
                        prefs.getString(Constants.PREF_SHAPE, Constants.PREF_SHAPE_DEFAULT));

                //intent set seconds extra
                intent.putExtra(Constants.PREF_SECONDS,
                        prefs.getInt(Constants.PREF_SECONDS, Constants.PREF_SECONDS_DEFAULT));

                //start game activity
                startActivity(intent);
            }
        });

        btnLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent
                Intent intent = new Intent(v.getContext(), LeaderboardActivity.class);

                //start activity
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent
                Intent intent = new Intent(v.getContext(), SettingsActivity.class);

                //start activity
                startActivity(intent);
            }
        });

        //return rootView
        return rootView;
    }
}
