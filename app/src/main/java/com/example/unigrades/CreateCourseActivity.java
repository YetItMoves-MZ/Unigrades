package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CreateCourseActivity extends AppCompatActivity {

    private Button createButton;
    private EditText editTextCourseName;

    private Account myAccount;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        uid = FirebaseAuth.getInstance().getUid();
        Toolbar toolbar = new Toolbar(this);

        findViews();

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
                String courseName = editTextCourseName.getText().toString();
                if(!courseName.equals("")){
                    String cid = courseName + "_" + uid;
                    if(myAccount.hasCourse(cid)){
                        Toast.makeText(CreateCourseActivity.this,
                                "course already exists.",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        //create course
                        AccountCourse newAccountCourse = new AccountCourse();
                        newAccountCourse.setCid(cid).
                                setName(courseName).
                                setTeacherName(myAccount.getFullName());

                        //add course to account database
                        myAccount.getCourses().add(newAccountCourse);
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
                            "name can't be empty.",
                            Toast.LENGTH_LONG).show();                }
            }
        });


    }

    private void findViews() {
        createButton = findViewById(R.id.createCourse_BUTTON_createCourse);
        editTextCourseName = findViewById(R.id.createCourse_EDITTEXT_courseName);
    }
}