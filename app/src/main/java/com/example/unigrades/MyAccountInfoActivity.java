package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MyAccountInfoActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_info);

        //TODO repeat those 2 always from now on.
        String uid = MyGlobalFunctions.getUidFromBundle(getIntent().getExtras().
                getBundle(MyGlobalFunctions.BUNDLE));
        Toolbar toolbar = new Toolbar(this);

        // show info based on uid:








    }


}