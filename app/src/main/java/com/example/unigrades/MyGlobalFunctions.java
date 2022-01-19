package com.example.unigrades;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Type;

/**
 * This class is just a few functions that I would like to use globally to reduce code redundancy.
 */
public class MyGlobalFunctions {

    public static final String UID_BUNDLE = "UID";
    public static final String BUNDLE = "BUNDLE";

    /**
     * This function starts new activity and closing current activity.
     * @param currentActivity the current activity.
     * @param classType the class of the activity we want to start.
     */
    public static void startNewActivity(AppCompatActivity currentActivity,
                                        Class classType,
                                        String uid) {
        Intent myIntent = new Intent(currentActivity, classType);
        if(uid != "" && uid != null)
            myIntent.putExtra(BUNDLE, setBundle(uid));
        currentActivity.startActivity(myIntent);
        currentActivity.finish();
    }

    private static Bundle setBundle(String uid){
        Bundle b = new Bundle();
        b.putString(UID_BUNDLE, uid);
        return b;
    }


    public static String getUidFromBundle(Bundle b){
        return b.getString(UID_BUNDLE);
    }



}
