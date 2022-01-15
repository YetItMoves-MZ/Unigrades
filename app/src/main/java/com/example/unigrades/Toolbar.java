package com.example.unigrades;

import android.content.Context;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Toolbar{
    private Context context;
    private ImageView menu;

    public Toolbar(){}
    public Toolbar(Context context) {
        this.context = context;
        findViews();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToolbarMenu(view);
            }
        });
    }

    private void showToolbarMenu(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.item1:
                        Toast.makeText(context,
                                "item 1 clicked",
                                Toast.LENGTH_LONG).show();

                        return true;
                    default:
                        return false;

                }
            }
        });
    }

    private void findViews() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        menu = appCompatActivity.findViewById(R.id.toolbar_IMG_menu);
    }
}
