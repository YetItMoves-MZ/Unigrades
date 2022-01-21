package com.example.unigrades;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class Toolbar{
    private AppCompatActivity appCompatActivity;
    private ImageView menuButton;
    private ImageView backButton;
    private String mode="";

    public Toolbar(){}
    public Toolbar(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        findViews();
        //TODO check if "else" is needed here
        if (MyAccountInfoActivity.class.equals(appCompatActivity.getClass())) {
            backButton.setVisibility(View.INVISIBLE);
        }
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToolbarMenu(view);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO check where "back button" needs to go in each and every activity.
                if (MyAccountInfoActivity.class.equals(appCompatActivity.getClass())) {
                    mode = " ";
                }
                else{
                    appCompatActivity.finish();
                }
            }
        });
    }

    private void showToolbarMenu(View v) {
        PopupMenu popup = new PopupMenu(appCompatActivity, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, popup.getMenu());

        if(mode.equals(Account.student))
            popup.getMenu().getItem(1).setVisible(true);
        if(mode.equals(Account.teacher))
            popup.getMenu().getItem(2).setVisible(true);

        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.toolbarMenu_ITEM_toMyCoursesActivity:
                        MyGlobalFunctions.startNewActivity(appCompatActivity,
                                MyCoursesActivity.class);
                        return true;
                    case R.id.toolbarMenu_ITEM_toSearchCourse:
                        MyGlobalFunctions.startNewActivity(appCompatActivity,
                                SearchCourseActivity.class);
                        return true;
                    case R.id.toolbarMenu_ITEM_toCreateCourse:
                        MyGlobalFunctions.startNewActivity(appCompatActivity,
                                CreateCourseActivity.class);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void setCurrentMode(String type){
        mode = type;
    }

    private void findViews() {
        menuButton = appCompatActivity.findViewById(R.id.toolbar_IMG_menu);
        backButton = appCompatActivity.findViewById(R.id.toolbar_IMG_back);
    }
}
