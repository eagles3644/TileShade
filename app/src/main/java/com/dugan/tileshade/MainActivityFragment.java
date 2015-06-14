package com.dugan.tileshade;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate fragment
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //create shader for gradient text
        Shader gradTextShader = new LinearGradient(0,0,0,160,
                Color.parseColor(Constants.COLOR_GRAD_START),
                Color.parseColor(Constants.COLOR_GRAD_END),
                Shader.TileMode.CLAMP);

        //get main textview to be painted
        TextView mainText = (TextView) rootView.findViewById(R.id.main_text);

        //apply paint to main textview via gradient shader
        mainText.getPaint().setShader(gradTextShader);

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
