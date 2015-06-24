package com.dugan.tileshade;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivityFragment extends Fragment {

    private AppCompatActivity activity;

    public GameOverActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate view
        View rootView = inflater.inflate(R.layout.fragment_game_over, container, false);

        //get text views
        TextView scoreText = (TextView) rootView.findViewById(R.id.final_score);
        TextView highScoreText = (TextView) rootView.findViewById(R.id.high_score);
        TextView newHighText = (TextView) rootView.findViewById(R.id.new_high_score);

        //get shared prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());

        //get pref values
        boolean newHighScore = getActivity().getIntent().getBooleanExtra(Constants.PREF_NEW_HIGH, false);
        int lastScore = getActivity().getIntent().getIntExtra(Constants.PREF_LAST_SCORE, 0);
        int highScore = prefs.getInt(Constants.PREF_HIGH_SCORE, 0);

        //show new high score text if necessary
        if(newHighScore){
            newHighText.setVisibility(View.VISIBLE);
        }

        //set text
        scoreText.setText(Integer.toString(lastScore));
        highScoreText.setText(Integer.toString(highScore));

        //create play again intent
        final Intent playAgainIntent = new Intent(rootView.getContext(), GameActivity.class);
        playAgainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //intent set mode extra
        playAgainIntent.putExtra(Constants.PREF_MODE, Constants.PREF_MODE_STANDARD);

        //intent set shape extra
        playAgainIntent.putExtra(Constants.PREF_SHAPE,
                prefs.getString(Constants.PREF_SHAPE, Constants.PREF_SHAPE_DEFAULT));

        //intent set seconds extra
        playAgainIntent.putExtra(Constants.PREF_SECONDS,
                prefs.getInt(Constants.PREF_SECONDS, Constants.PREF_SECONDS_DEFAULT));

        //create main menu intent
        final Intent mainMenuIntent = new Intent(rootView.getContext(), MainActivity.class);
        mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //get buttons
        Button btnPlayAgain = (Button) rootView.findViewById(R.id.btn_play_again);
        Button btnMainMenu = (Button) rootView.findViewById(R.id.btn_main_menu);

        //play again onclick listener
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(playAgainIntent);
            }
        });

        //main menu on click listener
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainMenuIntent);
            }
        });

        return rootView;
    }
}