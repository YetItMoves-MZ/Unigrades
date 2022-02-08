package com.example.unigrades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

/**
 * This class is just a few functions that I would like to use globally to reduce code redundancy.
 */
public class MyGlobalFunctions {


    public static final DecimalFormat df = new DecimalFormat("0.00");
    public static final int MAXIMUM_NAME_SIZE = 20;

    /**
     * This function starts new activity and closing current activity.
     * @param currentActivity the current activity.
     * @param classType the class of the activity we want to start.
     */
    public static void startNewActivity(AppCompatActivity currentActivity,
                                        Class classType) {
        startNewActivity(currentActivity,classType,"");
    }


    public static void startNewActivity(AppCompatActivity currentActivity, Class classType, String cid) {
        Intent myIntent = new Intent(currentActivity, classType);
        if(!cid.equals("")){
            //add bundle
            Bundle b = new Bundle();
            b.putString("cid", cid);
            myIntent.putExtra("bundle",b);
        }
        currentActivity.startActivity(myIntent);
        if(!classType.equals(CourseActivity.class))
            currentActivity.finish();
    }

    /**
     * reset activity
     * @param activity current activity.
     */
    public static void refreshActivity(Activity activity) {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }
}
