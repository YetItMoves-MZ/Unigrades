package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SearchCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);
        String uid = FirebaseAuth.getInstance().getUid();
        Toolbar toolbar = new Toolbar(this);

        findViews();

        // show info based on uid:
        Account myAccount = new Account();
        Account.Callback_Account callback_account = new Account.Callback_Account() {

            @Override
            public void dataReady(Account value) {
                myAccount.setAccountByAccount(value);
                toolbar.setCurrentMode(myAccount.getType());
            }
        };
        myAccount.findAccount(uid, callback_account);
    }

    private void findViews() {
    }
}