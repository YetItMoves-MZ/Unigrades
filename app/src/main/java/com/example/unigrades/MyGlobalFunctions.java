package com.example.unigrades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
}
