package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    final int ANIM_DURATION = 3500;

    private ImageView imageLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViews();

        imageLogo.setVisibility(View.INVISIBLE);

        showViewSlideDown(imageLogo);
    }

    private void findViews() {
        imageLogo = findViewById(R.id.splash_IMAGE_logo);
    }

    private void showViewSlideDown(final View v){
        v.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        v.setY(-height/2);
        v.setScaleY(0.0f);
        v.setScaleX(0.0f);
        v.animate().
                scaleX(1.0f).
                scaleY(1.0f).
                translationY(0).
                setDuration(ANIM_DURATION).
                setInterpolator(new AccelerateInterpolator()).
                setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animationDone();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void animationDone() {
        MyGlobalFunctions.startNewActivity(this,SignInActivity.class);
    }
}