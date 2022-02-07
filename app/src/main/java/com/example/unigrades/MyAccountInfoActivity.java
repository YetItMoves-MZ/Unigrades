package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MyAccountInfoActivity extends AppCompatActivity {


    private TextView textName;
    private TextInputLayout textLayoutChangePassword;
    private Button saveInfo;
    private ConstraintLayout studentExtraInfo;
    private TextView averageGrade;
    private TextView academicCredits;
    private ImageView teacherLogo;

    private Validator validatorPassword;


    /*TODO:
        remove academic credits from account database.

     */

    /*fixme:
        fix search to search by course name AND by teacher name.
        fix that when giving grades, you cant insert dots.
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_info);
        String uid = FirebaseAuth.getInstance().getUid();
        Toolbar toolbar = new Toolbar(this);

        findViews();

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
                if(myAccount.getType().equals(Account.student)){
                    studentExtraInfo.setVisibility(View.VISIBLE);
                    academicCredits.setText("Total academic credits: " + myAccount.getAcademicGradeTotal());
                    myAccount.getAverageGrade(uid, new Account.Callback_AverageGrade() {
                        @Override
                        public void dataReady(double avg) {
                            if(avg>0){
                                averageGrade.setText("Your average grade is " + MyGlobalFunctions.df.format(avg));
                            }
                            else{
                                averageGrade.setText("no grades given yet");
                            }
                        }
                    });
                }
                else if(myAccount.getType().equals(Account.teacher)){
                    teacherLogo.setVisibility(View.VISIBLE);
                }
            }
        };
        myAccount.findAccount(uid, callback_account);

        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = textLayoutChangePassword.getEditText().getText().toString();
                if(validatorPassword.validateIt()){
                    //password changed
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(newPassword);
                    Toast.makeText(MyAccountInfoActivity.this,
                            "password changed.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    private void findViews() {
        textName = findViewById(R.id.myAccountInfo_TEXT_name);
        textLayoutChangePassword = findViewById(R.id.myAccountInfo_EDITTEXT_Password);
        saveInfo = findViewById(R.id.myAccountInfo_BUTTON_save);
        studentExtraInfo = findViewById(R.id.myAccountInfo_LAYOUT_studentInfo);
        averageGrade = findViewById(R.id.myAccountInfo_TEXT_averageGrade);
        academicCredits = findViewById(R.id.myAccountInfo_TEXT_academicCredits);
        teacherLogo = findViewById(R.id.myAccountInfo_IMAGE_teacherLogo);
    }


}