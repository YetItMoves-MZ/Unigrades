package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MyAccountInfoActivity extends AppCompatActivity {


    private TextView textName;
    private TextInputLayout textLayoutChangeName;
    private TextInputLayout textLayoutChangePassword;
    private Button saveInfo;

    private Validator validatorName;
    private Validator validatorPassword;

    /*
    TODO WHAT NEEDS TO BE DONE:
             make everything prettier
        optional: add more statistics (like total average for student and number of academic credits)
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_info);
        String uid = FirebaseAuth.getInstance().getUid();
        Toolbar toolbar = new Toolbar(this);

        findViews();

        validatorName = Validator.Builder.make(textLayoutChangeName).
                addWatcher(new Validator.WatcherStartWithUpperCase("Start with upper case")).
                build();
        validatorPassword = Validator.Builder.make(textLayoutChangePassword).
                addWatcher(new Validator.WatcherAtLeastOneUpperCase("At least one upper case")).
                addWatcher(new Validator.WatcherAtLeastOneLowerCase("At least one lower case")).
                addWatcher(new Validator.WatcherAtLeastOneNumber("At least one number")).
                addWatcher(new Validator.WatcherMinimumText("Password must contain at least 8 letters", 8)).
                build();

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
                boolean passwordChanged = false;
                boolean nameChanged = false;
                String newName = textLayoutChangeName.getEditText().getText().toString();
                String newPassword = textLayoutChangePassword.getEditText().getText().toString();
                if(!newName.equals("")){
                    myAccount.setFullName(newName);
                    myAccount.addAccountToDB(uid);
                    textName.setText("Welcome, " + myAccount.getFullName());
                    nameChanged = true;
                }
                if(!newPassword.equals("")){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(newPassword);
                    passwordChanged = true;
                }
                String toastText = "";
                if(passwordChanged)
                    toastText = "password, ";
                if(nameChanged)
                    toastText += "name, ";
                if(!toastText.equals("")){
                    toastText = toastText.substring(0,toastText.length()-2);
                    toastText += " changed.";
                    Toast.makeText(MyAccountInfoActivity.this,
                            toastText,
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    private void findViews() {
        textName = findViewById(R.id.myAccountInfo_TEXT_name);
        textLayoutChangeName = findViewById(R.id.myAccountInfo_EDITTEXT_name);
        textLayoutChangePassword = findViewById(R.id.myAccountInfo_EDITTEXT_Password);
        saveInfo = findViewById(R.id.myAccountInfo_BUTTON_save);
    }


}