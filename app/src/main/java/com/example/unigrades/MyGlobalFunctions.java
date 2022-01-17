package com.example.unigrades;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Type;

/**
 * This class is just a few functions that I would like to use globally to reduce code redundancy.
 */
public class MyGlobalFunctions {

    /**
     * This function starts new activity and closing current activity.
     * @param currentActivity the current activity.
     * @param classType the class of the activity we want to start.
     */
    public static void startNewActivity(AppCompatActivity currentActivity, Class classType) {
        Intent myIntent = new Intent(currentActivity, classType);
        currentActivity.startActivity(myIntent);
        currentActivity.finish();
    }


}
