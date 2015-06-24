package com.dugan.tileshade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivityFragment extends Fragment {

    private int score = -1;
    private int time = 0;
    private int countDown = 4;
    private int level = 0;
    private int round = 0;
    private String shape;
    private String mode;
    private RelativeLayout fragmentLayout;
    private LayoutInflater inflater;
    private ViewGroup container;
    private GameCountdownTimer gameCountdownTimer;
    private GameStartTimer gameStartTimer;
    private AppCompatActivity activity;
    private Boolean gameTimerRunning = false;
    private Boolean gamePaused = false;
    private Boolean gameComplete = false;

    public GameActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflaterLI, ViewGroup containerVG,
                             Bundle savedInstanceState) {

        //set container variable
        container = containerVG;

        //set inflater variable
        inflater = inflaterLI;

        //inflate layout
        final View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        //get mode from intent
        mode = activity.getIntent().getStringExtra(Constants.PREF_MODE);

        //get shape from intent
        shape = activity.getIntent().getStringExtra(Constants.PREF_SHAPE);

        //set time variable
        time = activity.getIntent().getIntExtra(Constants.PREF_SECONDS,
                Constants.PREF_SECONDS_DEFAULT);

        //get activity action bar
        ActionBar actionBar = activity.getSupportActionBar();

        //create custom action bar view
        @SuppressLint("InflateParams") View customActionBarView = inflater.inflate(R.layout.score_board, null);

        //get action bar text views
        TextView timeText = (TextView) customActionBarView.findViewById(R.id.time);
        TextView scoreText = (TextView) customActionBarView.findViewById(R.id.score);

        //set action bar text
        timeText.setText(Integer.toString(time));
        scoreText.setText(Integer.toString(0));

        //set action bar settings
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        //set custom view
        actionBar.setCustomView(customActionBarView);

        //get fragment layout
        fragmentLayout = (RelativeLayout) rootView.findViewById(R.id.game_frag);

        gameStartTimer = new GameStartTimer(countDown*1000, 1000);
        gameStartTimer.start();

        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
        cancelTimer();
        if(gameTimerRunning) {
            creatPauseView(fragmentLayout.getContext(), (RelativeLayout) fragmentLayout.getChildAt(0));
        } else if(!gamePaused && !gameComplete) {
            getActivity().finish();
        }
    }

    public void cancelTimer(){
        if(gameStartTimer != null){
            gameStartTimer.cancel();
        }
        if(gameCountdownTimer != null){
            gameCountdownTimer.cancel();
        }
    }

    public class GameStartTimer extends  CountDownTimer {

        public GameStartTimer(int start, int interval){
            super(start, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            countDown = countDown - 1;

            RelativeLayout oldGameLayout = null;

            //get game board
            if(fragmentLayout.getChildCount() > 0){
                oldGameLayout = (RelativeLayout) fragmentLayout.getChildAt(0);
            }

            //remove game board
            fragmentLayout.removeView(oldGameLayout);

            View gameOverInflatorView;
            RelativeLayout gameCountDown;

            //inflate appropriate game view
            switch (countDown){
                case 3:
                    gameOverInflatorView = inflater.inflate(R.layout.game_3, container, false);
                    gameCountDown =  (RelativeLayout) gameOverInflatorView.findViewById(R.id.game_count_view_3);
                    break;
                case 2:
                    gameOverInflatorView = inflater.inflate(R.layout.game_2, container, false);
                    gameCountDown =  (RelativeLayout) gameOverInflatorView.findViewById(R.id.game_count_view_2);
                    break;
                case 1:
                    gameOverInflatorView = inflater.inflate(R.layout.game_1, container, false);
                    gameCountDown =  (RelativeLayout) gameOverInflatorView.findViewById(R.id.game_count_view_1);
                    break;
                default:
                    gameOverInflatorView = inflater.inflate(R.layout.game_3, container, false);
                    gameCountDown =  (RelativeLayout) gameOverInflatorView.findViewById(R.id.game_count_view_3);
                    break;
            }

            //create game board layout specs
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            //add layout rules
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            //add board level layout to fragment layout
            fragmentLayout.addView(gameCountDown, layoutParams);

        }

        @Override
        public void onFinish() {

            RelativeLayout oldGameLayout = null;

            //get game board
            if(fragmentLayout.getChildCount() > 0){
                oldGameLayout = (RelativeLayout) fragmentLayout.getChildAt(0);
            }

            //remove game board
            fragmentLayout.removeView(oldGameLayout);

            //setup game
            setupGamePieces(inflater, container);

            //start game timer
            gameCountdownTimer = new GameCountdownTimer(time*1000, 1000);
            gameCountdownTimer.start();
        }
    }

    public class GameCountdownTimer extends CountDownTimer {

        public GameCountdownTimer(int start, int interval){
            super(start, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            gameTimerRunning = true;

            time = time - 1;

            //get activity action bar
            ActionBar actionBar = ((GameActivity)getActivity()).getSupportActionBar();

            //create custom action bar view
            @SuppressLint("InflateParams") View customActionBarView = inflater.inflate(R.layout.score_board, null);

            //get action bar text views
            TextView timeText = (TextView) customActionBarView.findViewById(R.id.time);
            TextView scoreText = (TextView) customActionBarView.findViewById(R.id.score);

            //set action bar text
            timeText.setText(Integer.toString(time));
            scoreText.setText(Integer.toString(score));

            if(time <= 10){
                timeText.setTextColor(Color.RED);
            }

            //set action bar settings
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            //set custom view
            actionBar.setCustomView(customActionBarView);
        }

        @Override
        public void onFinish() {
            gameTimerRunning = false;

            time = time - 1;

            //get activity action bar
            ActionBar actionBar = ((GameActivity)getActivity()).getSupportActionBar();

            //create custom action bar view
            @SuppressLint("InflateParams") View customActionBarView = inflater.inflate(R.layout.score_board, null);

            //get action bar text views
            TextView timeText = (TextView) customActionBarView.findViewById(R.id.time);
            TextView scoreText = (TextView) customActionBarView.findViewById(R.id.score);

            //set action bar text
            timeText.setText(Integer.toString(time));
            scoreText.setText(Integer.toString(score));

            if(time <= 10){
                timeText.setTextColor(Color.RED);
            }

            //set action bar settings
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            //set custom view
            actionBar.setCustomView(customActionBarView);

            RelativeLayout oldGameLayout = null;

            //get game board
            if(fragmentLayout.getChildCount() > 0){
                oldGameLayout = (RelativeLayout) fragmentLayout.getChildAt(0);
            }

            //remove game board
            fragmentLayout.removeView(oldGameLayout);

            //inflate game over
            View gameOverInflatorView = inflater.inflate(R.layout.game_over, container, false);

            //get relative layout
            RelativeLayout gameOverLayout =  (RelativeLayout) gameOverInflatorView.findViewById(R.id.board_game_over);

            //create game board layout specs
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            //add layout rules
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            //add board level layout to fragment layout
            fragmentLayout.addView(gameOverLayout, layoutParams);

            //get prefs and editor
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(customActionBarView.getContext());
            SharedPreferences.Editor editor = prefs.edit();

            //new high score variable
            boolean newHighScore = false;

            //store new high score
            if(score > prefs.getInt(Constants.PREF_HIGH_SCORE, 0)){
                editor.putInt(Constants.PREF_HIGH_SCORE, score).apply();
                newHighScore = true;
            }

            //intent to for game over
            final Intent intent = new Intent(fragmentLayout.getContext(), GameOverActivity.class);

            //put last score intent extra
            intent.putExtra(Constants.PREF_LAST_SCORE, score);

            //put new high score intent extra
            intent.putExtra(Constants.PREF_NEW_HIGH, newHighScore);

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    gameComplete = true;
                    startActivity(intent);
                }
            }, 1000);
        }
    }

    private void setupGamePieces(final LayoutInflater inflater,
                                 ViewGroup container){
        //local vars
        int gamePieces;
        RelativeLayout gameLayout;
        RelativeLayout oldGameLayout = null;

        //increase score
        score = score + 1;
        round = round + 1;

        //inflate dynamic layout
        View dynamic = inflater.inflate(R.layout.game_board, container, false);

        //get gameLayout
        gameLayout = (RelativeLayout) dynamic.findViewById(R.id.board);

        //get activity action bar
        ActionBar actionBar = ((GameActivity)getActivity()).getSupportActionBar();

        //create custom action bar view
        @SuppressLint("InflateParams") View customActionBarView = inflater.inflate(R.layout.score_board, null);

        //get action bar text views
        TextView scoreText = (TextView) customActionBarView.findViewById(R.id.score);
        final TextView timeText = (TextView) customActionBarView.findViewById(R.id.time);

        //set action bar text
        scoreText.setText(Integer.toString(score));
        timeText.setText(Integer.toString(time));

        if(time <= 10){
            timeText.setTextColor(Color.RED);
        }

        //set action bar settings
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        //set custom view
        actionBar.setCustomView(customActionBarView);

        if(fragmentLayout.getChildCount() > 0){
            oldGameLayout = (RelativeLayout) fragmentLayout.getChildAt(0);
        }

        //determine next level
        if(score >= Constants.LEVEL_1_MIN && score <= Constants.LEVEL_1_MAX){
            level = 1;
            gamePieces = Constants.SHAPES_LEVEL_1;
            if(oldGameLayout != null){
                ((RelativeLayout)oldGameLayout.getParent()).removeView(oldGameLayout);
            }
            if(round == Constants.LEVEL_1_MAX){
                round = 1;
            }
            gameLayout = creatGameView(fragmentLayout.getContext(), gameLayout, level, gamePieces);
        } else if(score >= Constants.LEVEL_2_MIN && score <= Constants.LEVEL_2_MAX){
            level = 2;
            gamePieces = Constants.SHAPES_LEVEL_2;
            if(oldGameLayout != null){
                ((RelativeLayout)oldGameLayout.getParent()).removeView(oldGameLayout);
            }
            if(round == Constants.LEVEL_2_MAX){
                round = 1;
            }
            gameLayout = creatGameView(fragmentLayout.getContext(), gameLayout, level, gamePieces);
        } else if(score >= Constants.LEVEL_3_MIN && score <= Constants.LEVEL_3_MAX){
            level = 3;
            gamePieces = Constants.SHAPES_LEVEL_3;
            if(oldGameLayout != null){
                ((RelativeLayout)oldGameLayout.getParent()).removeView(oldGameLayout);
            }
            if(round == Constants.LEVEL_3_MAX){
                round = 1;
            }
            gameLayout = creatGameView(fragmentLayout.getContext(), gameLayout, level, gamePieces);
        } else if(score >= Constants.LEVEL_4_MIN && score <= Constants.LEVEL_4_MAX){
            level = 4;
            gamePieces = Constants.SHAPES_LEVEL_4;
            if(oldGameLayout != null){
                ((RelativeLayout)oldGameLayout.getParent()).removeView(oldGameLayout);
            }
            if(round == Constants.LEVEL_4_MAX){
                round = 1;
            }
            gameLayout = creatGameView(fragmentLayout.getContext(), gameLayout, level, gamePieces);
        } else if(score >= Constants.LEVEL_5_MIN && score <= Constants.LEVEL_5_MAX){
            level = 5;
            gamePieces = Constants.SHAPES_LEVEL_5;
            if(oldGameLayout != null){
                ((RelativeLayout)oldGameLayout.getParent()).removeView(oldGameLayout);
            }
            if(round == Constants.LEVEL_5_MAX){
                round = 1;
            }
            gameLayout = creatGameView(fragmentLayout.getContext(), gameLayout, level, gamePieces);
        } else if(score >= Constants.LEVEL_6_MIN && score <= Constants.LEVEL_6_MAX){
            level = 6;
            gamePieces = Constants.SHAPES_LEVEL_6;
            if(oldGameLayout != null) {
                ((RelativeLayout) oldGameLayout.getParent()).removeView(oldGameLayout);
            }
            if(round == Constants.LEVEL_6_MAX){
                round = 1;
            }
            gameLayout = creatGameView(fragmentLayout.getContext(), gameLayout, level, gamePieces);
        } else if(score >= Constants.LEVEL_7_MIN && score <= Constants.LEVEL_7_MAX){
            level = 7;
            gamePieces = Constants.SHAPES_LEVEL_7;
            if(oldGameLayout != null){
                ((RelativeLayout)oldGameLayout.getParent()).removeView(oldGameLayout);
            }
            if(round == Constants.LEVEL_7_MAX){
                round = 1;
            }
            gameLayout = creatGameView(fragmentLayout.getContext(), gameLayout, level, gamePieces);
        } else if(score >= Constants.LEVEL_8_MIN && score <= Constants.LEVEL_8_MAX){
            level = 8;
            gamePieces = Constants.SHAPES_LEVEL_8;
            if(oldGameLayout != null){
                ((RelativeLayout)oldGameLayout.getParent()).removeView(oldGameLayout);
            }
            if(round == Constants.LEVEL_8_MAX){
                round = 1;
            }
            gameLayout = creatGameView(fragmentLayout.getContext(), gameLayout, level, gamePieces);
        } else {
            level = 9;
            gamePieces = Constants.SHAPES_LEVEL_9;
            if (oldGameLayout != null) {
                ((RelativeLayout) oldGameLayout.getParent()).removeView(oldGameLayout);
            }
            if (round == Constants.LEVEL_9_MAX) {
                round = 1;
            }
            gameLayout = creatGameView(fragmentLayout.getContext(), gameLayout, level, gamePieces);
        }

        //get buttons
        Button btnPause = (Button) gameLayout.findViewById(R.id.game_pause);
        Button btnReset = (Button) gameLayout.findViewById(R.id.game_reset);
        Button btnQuit  = (Button) gameLayout.findViewById(R.id.game_quit);

        //set pause button click action
        final RelativeLayout finalGameLayout = gameLayout;
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                creatPauseView(v.getContext(), finalGameLayout);
            }
        });

        //set reset button click action
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shared prefs
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(fragmentLayout.getContext());

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

                //clear back stack
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        });

        //set quit button click action
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cancel timers
                cancelTimer();

                //create intent
                Intent intent = new Intent(v.getContext(), MainActivity.class);

                //clear back stack
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //start activity
                startActivity(intent);

            }
        });

        //create game board layout specs
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        //add layout rules
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        gameLayout.setLayoutParams(layoutParams);

        //add board level layout to fragment layout
        fragmentLayout.addView(gameLayout, layoutParams);

    }

    private int chooseWinner(int gamePieces){
        Random rand = new Random();
        int min = 1;
        return rand.nextInt(gamePieces - min + 1) + 1;
    }

    private View.OnClickListener createClickListener(){
        //create click listener
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupGamePieces(inflater, container);
            }
        };
    }

    private RelativeLayout creatGameView(Context context,
                                         RelativeLayout gameLayout,
                                         int level, int gamePieces) {

        //initialize locals
        int rows;
        int size;
        int pieceNum = 0;
        int margin = getResources().getDimensionPixelSize(R.dimen.game_shape_spacing);
        RelativeLayout.LayoutParams layoutParams;
        GradientDrawable gradientDrawablePressed;
        GradientDrawable gradientDrawablePressedWinner;
        GradientDrawable gradientDrawableNotPressed;
        StateListDrawable stateListDrawable;
        Button button;
        ColorDrawable colorMain;
        ColorDrawable colorAccent;
        int winColor;
        int mainColor;
        int winner;

        //Dynamic sub layout
        RelativeLayout dynamicSub = new RelativeLayout(context);

        //Dynamic sub layout params
        RelativeLayout.LayoutParams dynamicSubLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        dynamicSubLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        dynamicSubLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);

        dynamicSub.setLayoutParams(dynamicSubLayout);

        //initialize random color generator
        RandomColorGenerator randomColorGenerator = new RandomColorGenerator();

        //get randomized colors
        colorMain = randomColorGenerator.getRandomBaseColor();
        mainColor = colorMain.getColor();
        colorAccent = randomColorGenerator.changeColorOpacity(colorMain);
        winColor = colorAccent.getColor();

        //get winner
        winner = chooseWinner(gamePieces);

        //get appropriate row count for level
        switch(level){
            case 1:
                rows = Constants.ROWS_LEVEL_1;
                size = getResources().getDimensionPixelSize(R.dimen.level_1);
                break;
            case 2:
                rows = Constants.ROWS_LEVEL_2;
                size = getResources().getDimensionPixelSize(R.dimen.level_2);
                break;
            case 3:
                rows = Constants.ROWS_LEVEL_3;
                size = getResources().getDimensionPixelSize(R.dimen.level_3);
                break;
            case 4:
                rows = Constants.ROWS_LEVEL_4;
                size = getResources().getDimensionPixelSize(R.dimen.level_4);
                break;
            case 5:
                rows = Constants.ROWS_LEVEL_5;
                size = getResources().getDimensionPixelSize(R.dimen.level_5);
                break;
            case 6:
                rows = Constants.ROWS_LEVEL_6;
                size = getResources().getDimensionPixelSize(R.dimen.level_6);
                break;
            case 7:
                rows = Constants.ROWS_LEVEL_7;
                size = getResources().getDimensionPixelSize(R.dimen.level_7);
                break;
            case 8:
                rows = Constants.ROWS_LEVEL_8;
                size = getResources().getDimensionPixelSize(R.dimen.level_8);
                break;
            case 9:
                rows = Constants.ROWS_LEVEL_9;
                size = getResources().getDimensionPixelSize(R.dimen.level_9);
                break;
            default:
                rows = Constants.ROWS_LEVEL_1;
                size = getResources().getDimensionPixelSize(R.dimen.level_1);
                break;
        }

        //add buttons
        for(int rowCount = 0; rowCount < rows; rowCount++){
            for(int colCount = 0; colCount < rows; colCount++){
                pieceNum = pieceNum + 1;
                button = new Button(context);
                gradientDrawableNotPressed = new GradientDrawable();
                gradientDrawableNotPressed.setCornerRadius(12f);
                gradientDrawablePressed = new GradientDrawable();
                gradientDrawablePressed.setCornerRadius(12f);
                gradientDrawablePressed.setColor(Color.RED);
                gradientDrawablePressedWinner = new GradientDrawable();
                gradientDrawablePressedWinner.setCornerRadius(12f);
                gradientDrawablePressedWinner.setColor(Color.GREEN);
                button.setMinimumHeight(0);
                button.setMinimumWidth(0);
                layoutParams = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                if(pieceNum > 1 && colCount > 0){
                    layoutParams.addRule(RelativeLayout.RIGHT_OF, pieceNum-1);
                }
                if(rowCount > 0){
                    layoutParams.addRule(RelativeLayout.BELOW, pieceNum-rows);
                }
                layoutParams.setMargins(margin, margin, margin, margin);
                layoutParams.height = size;
                layoutParams.width = size;
                button.setId(pieceNum);
                button.setLayoutParams(layoutParams);
                if(pieceNum == winner){
                    button.setOnClickListener(createClickListener());
                    gradientDrawableNotPressed.setColor(winColor);
                    stateListDrawable = new StateListDrawable();
                    stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, gradientDrawablePressedWinner);
                    stateListDrawable.addState(new int[] {android.R.attr.state_enabled}, gradientDrawableNotPressed);
                    button.setBackground(stateListDrawable);
                } else {
                    button.setClickable(true);
                    gradientDrawableNotPressed.setColor(mainColor);
                    stateListDrawable = new StateListDrawable();
                    stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, gradientDrawablePressed);
                    stateListDrawable.addState(new int[] {android.R.attr.state_enabled}, gradientDrawableNotPressed);
                    button.setBackground(stateListDrawable);
                }
                dynamicSub.addView(button);
            }
        }
        gameLayout.addView(dynamicSub);
        return gameLayout;
    }

    private RelativeLayout creatPauseView(Context context,
                                         RelativeLayout gameLayout) {

        gameTimerRunning = false;
        gamePaused = true;

        //initialize locals
        RelativeLayout dynamicSub;
        RelativeLayout.LayoutParams dynamicSubLayout;
        RelativeLayout.LayoutParams pauseLayout;
        RelativeLayout oldGameBoard;

        if(gameLayout.getChildCount() > 1){
            oldGameBoard = (RelativeLayout) gameLayout.getChildAt(1);
            gameLayout.removeView(oldGameBoard);
        }

        Button btnPause = (Button) gameLayout.findViewById(R.id.game_pause);

        btnPause.setText("Resume");

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDown = 4;
                gameStartTimer = new GameStartTimer(countDown*1000, 1000);
                gameStartTimer.start();
                gamePaused = false;
            }
        });

        //Dynamic sub layout
        dynamicSub = new RelativeLayout(context);

        //Dynamic sub layout params
        dynamicSubLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        dynamicSubLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        dynamicSubLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);

        dynamicSub.setLayoutParams(dynamicSubLayout);

        pauseLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        pauseLayout.setMargins(0,300,0,0);

        //add TextView
        TextView pauseText = new TextView(context);

        pauseText.setText("Pause");
        pauseText.setTypeface(null, Typeface.BOLD);
        pauseText.setTextSize(50);
        pauseText.setTextColor(Color.BLACK);
        pauseText.setLayoutParams(pauseLayout);

        dynamicSub.addView(pauseText);
        gameLayout.addView(dynamicSub);

        return gameLayout;
    }
}
