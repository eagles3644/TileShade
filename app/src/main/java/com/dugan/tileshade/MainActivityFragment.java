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
import android.util.Log;
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

        //create click listener
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //initialize local vars
                String mode;
                int id = v.getId();

                //set mode variable equal to button clicked
                switch (id){
                    case R.id.btn_mode_standard:
                        mode = Constants.PREF_MODE_STANDARD;
                        break;
                    case R.id.btn_mode_advanced:
                        mode = Constants.PREF_MODE_ADVANCED;
                        break;
                    case R.id.btn_mode_expert:
                        mode = Constants.PREF_MODE_EXPERT;
                        break;
                    default:
                        mode = Constants.PREF_MODE_DEFAULT;
                        break;
                }

                //shared prefs
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(rootView.getContext());

                //create intent
                Intent intent = new Intent(v.getContext(), GameActivity.class);

                //intent set mode extra
                intent.putExtra(Constants.PREF_MODE, mode);

                //intent set shape extra
                intent.putExtra(Constants.PREF_SHAPE,
                        prefs.getString(Constants.PREF_SHAPE, Constants.PREF_SHAPE_DEFAULT));

                //intent set seconds extra
                intent.putExtra(Constants.PREF_SECONDS,
                        prefs.getInt(Constants.PREF_SECONDS, Constants.PREF_SECONDS_DEFAULT));

                //log mode
                Log.i("SelectedMode", mode);

                //start game activity
                startActivity(intent);
            }
        };

        //get mode buttons
        Button modeStandard = (Button) rootView.findViewById(R.id.btn_mode_standard);
        Button modeAdvanced = (Button) rootView.findViewById(R.id.btn_mode_advanced);
        Button modeExpert = (Button) rootView.findViewById(R.id.btn_mode_expert);

        //set mode button onclick listeners
        modeStandard.setOnClickListener(clickListener);
        modeAdvanced.setOnClickListener(clickListener);
        modeExpert.setOnClickListener(clickListener);

        //return rootview
        return rootView;
    }
}
