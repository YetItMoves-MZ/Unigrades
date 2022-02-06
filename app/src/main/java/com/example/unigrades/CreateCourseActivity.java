package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class CreateCourseActivity extends AppCompatActivity {

    private Button createButton;
    private TextInputLayout textLayoutCourseName;
    private TextInputLayout textLayoutAcademicCredits;

    private Account myAccount;
    private String uid;

    private Validator validatorCourseName;
    private Validator validatorAcademicCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        uid = FirebaseAuth.getInstance().getUid();
        Toolbar toolbar = new Toolbar(this);

        findViews();

        validatorCourseName = Validator.Builder.make(textLayoutCourseName).
                addWatcher(new Validator.WatcherMaximumText("Name cannot be longer than " + MyGlobalFunctions.MAXIMUM_NAME_SIZE, MyGlobalFunctions.MAXIMUM_NAME_SIZE)).
                addWatcher(new Validator.WatcherStartWithUpperCase("Must start with upper case letter")).
                build();

        validatorAcademicCredits = Validator.Builder.make(textLayoutAcademicCredits).
                addWatcher(new Validator.WatcherIsANumber("Must be a decimal number")).
                build();

        // show info based on uid:
        myAccount = new Account();
        Account.Callback_Account callback_account = new Account.Callback_Account() {

            @Override
            public void dataReady(Account value) {
                myAccount.setAccountByAccount(value);
                toolbar.setCurrentMode(myAccount.getType());
                createButton.setVisibility(View.VISIBLE);
            }
        };
        myAccount.findAccount(uid, callback_account);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validatorCourseName.validateIt() &&
                        validatorAcademicCredits.validateIt()){
                    String courseName = textLayoutCourseName.getEditText().getText().toString();
                    String academicCreditsStr = textLayoutAcademicCredits.getEditText().getText().toString();
                    if((!courseName.equals("")) &&
                            (!academicCreditsStr.equals(""))){
                        double academicCredits = Double.parseDouble(academicCreditsStr);
                        String cid = courseName + "_" + uid;
                        if(myAccount.hasCourse(cid)){
                            Toast.makeText(CreateCourseActivity.this,
                                    "Course already exists.",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            //create course
                            AccountCourse newAccountCourse = new AccountCourse();
                            newAccountCourse.setCid(cid).
                                    setName(courseName).
                                    setTeacherName(myAccount.getFullName()).
                                    setAcademicCredits(academicCredits);

                            //add course to account database
                            myAccount.getAccountCourses().add(newAccountCourse);
                            myAccount.addAccountToDB(uid);

                            //add course to course database
                            Course newCourse = new Course(newAccountCourse);
                            newCourse.addCourseToDB();

                            //move to my courses
                            MyGlobalFunctions.
                                    startNewActivity(CreateCourseActivity.this,MyCoursesActivity.class);

                        }
                    }
                    else{
                        Toast.makeText(CreateCourseActivity.this,
                                "Fields can't be empty.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        });


    }

    private void findViews() {
        createButton = findViewById(R.id.createCourse_BUTTON_createCourse);
        textLayoutCourseName = findViewById(R.id.createCourse_EDITTEXT_courseName);
        textLayoutAcademicCredits = findViewById(R.id.createCourse_EDITTEXT_academicCredits);
    }
}