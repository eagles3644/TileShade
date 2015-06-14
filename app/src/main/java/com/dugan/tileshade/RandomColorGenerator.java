package com.dugan.tileshade;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Todd on 5/30/2015.
 */
public class RandomColorGenerator {

    public ColorDrawable getRandomBaseColor(){
        ArrayList<String> gameColors = makeGameColorArray();
        int arraySize = gameColors.size();
        Random random = new Random();
        int randColorIndex = random.nextInt(arraySize);
        String randColorString = gameColors.get(randColorIndex);
        ColorDrawable newColor = new ColorDrawable(Color.parseColor(randColorString));
        newColor = changeColorOpacity(newColor);
        return newColor;
    }

    public ColorDrawable changeColorOpacity(ColorDrawable color){
        ArrayList<Float> gameOpacity = makeGameOpacityArray();
        int arraySize = gameOpacity.size();
        Random rand = new Random();
        int randOpacityIndex = rand.nextInt(arraySize);
        float opacity = gameOpacity.get(randOpacityIndex);
        int alpha = Math.round((float)color.getAlpha()*opacity);
        int r = Color.red(color.getColor());
        int g = Color.green(color.getColor());
        int b = Color.blue(color.getColor());
        int newColor = Color.argb(alpha, r , g, b);
        ColorDrawable newColorDrawable = new ColorDrawable(newColor);
        if(Math.abs(newColorDrawable.getAlpha()-color.getAlpha()) > 200){
            rand = new Random(100);
            newColor = Color.argb(color.getAlpha()+rand.nextInt(), r, g, b);
            newColorDrawable = new ColorDrawable(newColor);
        }
        Log.e("Opacity", "" + opacity + " Alpha: " + newColorDrawable.getAlpha());
        return newColorDrawable;
    }

    private ArrayList<String> makeGameColorArray(){
        ArrayList<String> gameColors = new ArrayList<>();
        gameColors.add(Constants.COLOR_RED_900);
        gameColors.add(Constants.COLOR_RED_A100);
        gameColors.add(Constants.COLOR_RED_A200);
        gameColors.add(Constants.COLOR_RED_A400);
        gameColors.add(Constants.COLOR_RED_A700);
        gameColors.add(Constants.COLOR_PINK_900);
        gameColors.add(Constants.COLOR_PINK_A100);
        gameColors.add(Constants.COLOR_PINK_A200);
        gameColors.add(Constants.COLOR_PINK_A400);
        gameColors.add(Constants.COLOR_PINK_A700);
        gameColors.add(Constants.COLOR_PURPLE_900);
        gameColors.add(Constants.COLOR_PURPLE_A100);
        gameColors.add(Constants.COLOR_PURPLE_A200);
        gameColors.add(Constants.COLOR_PURPLE_A400);
        gameColors.add(Constants.COLOR_PURPLE_A700);
        gameColors.add(Constants.COLOR_DEEP_PURPLE_900);
        gameColors.add(Constants.COLOR_DEEP_PURPLE_A100);
        gameColors.add(Constants.COLOR_DEEP_PURPLE_A200);
        gameColors.add(Constants.COLOR_DEEP_PURPLE_A400);
        gameColors.add(Constants.COLOR_DEEP_PURPLE_A700);
        gameColors.add(Constants.COLOR_INDIGO_900);
        gameColors.add(Constants.COLOR_INDIGO_A100);
        gameColors.add(Constants.COLOR_INDIGO_A200);
        gameColors.add(Constants.COLOR_INDIGO_A400);
        gameColors.add(Constants.COLOR_INDIGO_A700);
        gameColors.add(Constants.COLOR_BLUE_900);
        gameColors.add(Constants.COLOR_BLUE_A100);
        gameColors.add(Constants.COLOR_BLUE_A200);
        gameColors.add(Constants.COLOR_BLUE_A400);
        gameColors.add(Constants.COLOR_BLUE_A700);
        gameColors.add(Constants.COLOR_LIGHT_BLUE_900);
        gameColors.add(Constants.COLOR_LIGHT_BLUE_A100);
        gameColors.add(Constants.COLOR_LIGHT_BLUE_A200);
        gameColors.add(Constants.COLOR_LIGHT_BLUE_A400);
        gameColors.add(Constants.COLOR_LIGHT_BLUE_A700);
        gameColors.add(Constants.COLOR_CYAN_900);
        gameColors.add(Constants.COLOR_CYAN_A100);
        gameColors.add(Constants.COLOR_CYAN_A200);
        gameColors.add(Constants.COLOR_CYAN_A400);
        gameColors.add(Constants.COLOR_CYAN_A700);
        gameColors.add(Constants.COLOR_TEAL_900);
        gameColors.add(Constants.COLOR_TEAL_A100);
        gameColors.add(Constants.COLOR_TEAL_A200);
        gameColors.add(Constants.COLOR_TEAL_A400);
        gameColors.add(Constants.COLOR_TEAL_A700);
        gameColors.add(Constants.COLOR_GREEN_900);
        gameColors.add(Constants.COLOR_GREEN_A100);
        gameColors.add(Constants.COLOR_GREEN_A200);
        gameColors.add(Constants.COLOR_GREEN_A400);
        gameColors.add(Constants.COLOR_GREEN_A700);
        gameColors.add(Constants.COLOR_LIGHT_GREEN_900);
        gameColors.add(Constants.COLOR_LIGHT_GREEN_A100);
        gameColors.add(Constants.COLOR_LIGHT_GREEN_A200);
        gameColors.add(Constants.COLOR_LIGHT_GREEN_A400);
        gameColors.add(Constants.COLOR_LIGHT_GREEN_A700);
        gameColors.add(Constants.COLOR_LIME_900);
        gameColors.add(Constants.COLOR_LIME_A100);
        gameColors.add(Constants.COLOR_LIME_A200);
        gameColors.add(Constants.COLOR_LIME_A400);
        gameColors.add(Constants.COLOR_LIME_A700);
        gameColors.add(Constants.COLOR_YELLOW_900);
        gameColors.add(Constants.COLOR_YELLOW_A100);
        gameColors.add(Constants.COLOR_YELLOW_A200);
        gameColors.add(Constants.COLOR_YELLOW_A400);
        gameColors.add(Constants.COLOR_YELLOW_A700);
        gameColors.add(Constants.COLOR_AMBER_900);
        gameColors.add(Constants.COLOR_AMBER_A100);
        gameColors.add(Constants.COLOR_AMBER_A200);
        gameColors.add(Constants.COLOR_AMBER_A400);
        gameColors.add(Constants.COLOR_AMBER_A700);
        gameColors.add(Constants.COLOR_ORANGE_900);
        gameColors.add(Constants.COLOR_ORANGE_A100);
        gameColors.add(Constants.COLOR_ORANGE_A200);
        gameColors.add(Constants.COLOR_ORANGE_A400);
        gameColors.add(Constants.COLOR_ORANGE_A700);
        gameColors.add(Constants.COLOR_DEEP_ORANGE_900);
        gameColors.add(Constants.COLOR_DEEP_ORANGE_A100);
        gameColors.add(Constants.COLOR_DEEP_ORANGE_A200);
        gameColors.add(Constants.COLOR_DEEP_ORANGE_A400);
        gameColors.add(Constants.COLOR_DEEP_ORANGE_A700);
        gameColors.add(Constants.COLOR_BROWN_900);
        gameColors.add(Constants.COLOR_GREY_900);
        gameColors.add(Constants.COLOR_BLUE_GREY_900);
        gameColors.add(Constants.COLOR_BLACK);
        return gameColors;
    }

    private ArrayList<Float> makeGameOpacityArray(){
        ArrayList<Float> gameOpacity = new ArrayList<>();
        gameOpacity.add(Constants.OPACITY_1);
        gameOpacity.add(Constants.OPACITY_2);
        gameOpacity.add(Constants.OPACITY_3);
        gameOpacity.add(Constants.OPACITY_4);
        gameOpacity.add(Constants.OPACITY_5);
        gameOpacity.add(Constants.OPACITY_6);
        gameOpacity.add(Constants.OPACITY_7);
        gameOpacity.add(Constants.OPACITY_8);
        return gameOpacity;
    }

}
