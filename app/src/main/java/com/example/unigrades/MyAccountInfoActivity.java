package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MyAccountInfoActivity extends AppCompatActivity {


    private TextView textName;
    private EditText changeName;
    private EditText changePassword;
    private Button saveInfo;

    /*
    TODO WHAT NEEDS TO BE DONE:
        teacher course fragment - 25.1
        sign in to course as a student (button added, just need to implement it) - 25.1
        my courses activity (just copy paste it from search courses) - 25.1
        change the back button in the toolbar to go to the right location and not exit app - 26.1
        make everything prettier - 26.1
        optional: add more statistics (like total average for student and number of academic credits)
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_info);
        String uid = FirebaseAuth.getInstance().getUid();
        Toolbar toolbar = new Toolbar(this);

        findViews();

        // show info based on uid:
        Account myAccount = new Account();
        Account.Callback_Account callback_account= new Account.Callback_Account(){

            @Override
            public void dataReady(Account value) {
                myAccount.setAccountByAccount(value);
                textName.setText("Welcome, " + myAccount.getFullName());
                toolbar.setCurrentMode(myAccount.getType());
            }
        };
        myAccount.findAccount(uid, callback_account);

        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = changeName.getText().toString();
                String newPassword = changePassword.getText().toString();
                if(!newName.equals("")){
                    myAccount.setFullName(newName);
                    myAccount.addAccountToDB(uid);
                    textName.setText("Welcome, " + myAccount.getFullName());
                }
                if(!newPassword.equals("")){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(newPassword);
                }
            }
        });

    }

    private void findViews() {
        textName = findViewById(R.id.myAccountInfo_TEXT_name);
        changeName = findViewById(R.id.myAccountInfo_EDITTEXT_name);
        changePassword = findViewById(R.id.myAccountInfo_EDITTEXT_Password);
        saveInfo = findViewById(R.id.myAccountInfo_BUTTON_save);
    }


}