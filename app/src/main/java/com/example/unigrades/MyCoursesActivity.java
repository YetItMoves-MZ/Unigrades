package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MyCoursesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCourses;
    private AdapterCourse adapterCourse;
    private ArrayList<Course> myCourses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        String uid = FirebaseAuth.getInstance().getUid();
        Toolbar toolbar = new Toolbar(this);

        findViews();

        // show info based on uid:
        Account myAccount = new Account();
        Account.Callback_Account callback_account= new Account.Callback_Account(){

            @Override
            public void dataReady(Account value) {
                myAccount.setAccountByAccount(value);
                toolbar.setCurrentMode(myAccount.getType());
                myCourses = value.getCourses();
                if(myCourses == null){
                    // no need to continue since no courses are assigned yet.
                    Toast.makeText(MyCoursesActivity.this,
                            "no courses are assigned yet.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                recyclerViewCourses.setVisibility(View.VISIBLE);

                adapterCourse = new AdapterCourse(MyCoursesActivity.this,
                        myCourses,
                        uid,
                        false);
                //Grid
                recyclerViewCourses.setLayoutManager(new GridLayoutManager(
                        MyCoursesActivity.this, 1));

                recyclerViewCourses.setHasFixedSize(true);
                recyclerViewCourses.setItemAnimator(new DefaultItemAnimator());
                recyclerViewCourses.setAdapter(adapterCourse);

                adapterCourse.setCourseItemClickListener(new AdapterCourse.CourseItemClickListener() {
                    @Override
                    public void courseItemClicked(Course course) {
                        MyGlobalFunctions.startNewActivity(MyCoursesActivity.this,
                                CourseActivity.class,
                                course.getCid());
                    }

                    @Override
                    public void signInClicked(Course course) {
                        // only usable when needed to sign in for courses.
                    }
                });

            }
        };
        myAccount.findAccount(uid, callback_account);




    }

    private void findViews() {
        recyclerViewCourses = findViewById(R.id.myCourses_RECYCLERVIEW_courses);
    }
}