package com.example.unigrades;

import android.content.Context;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class Toolbar{
    private AppCompatActivity appCompatActivity;
    private ImageView menu;

    public Toolbar(){}
    public Toolbar(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        findViews();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToolbarMenu(view);
            }
        });
        String uid = FirebaseAuth.getInstance().getUid();
    }

    private void showToolbarMenu(View v) {
        PopupMenu popup = new PopupMenu(appCompatActivity, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.item1:
                        MyGlobalFunctions.startNewActivity(appCompatActivity,
                                MyCoursesActivity.class,"");

                        // TODO delete this comment later
                        // this is how you check the same activity
                        // if (appCompatActivity.getClass() == MyAccountInfoActivity.class)
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void findViews() {
        menu = appCompatActivity.findViewById(R.id.toolbar_IMG_menu);
    }
}
