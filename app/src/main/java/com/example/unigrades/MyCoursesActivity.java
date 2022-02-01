package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
                    public void buttonClicked(Course accountCourse) {
                        String cid = accountCourse.getCid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Course course = new Course();
                        Course.Callback_Course callback_course = new Course.Callback_Course() {
                            @Override
                            public void dataReady(Course value) {
                                if(myAccount.getType().equals(Account.teacher)){
                                    //remove course from courses collection in database
                                    value.deleteCourseFromDB(db);
                                }
                                if(myAccount.getType().equals(Account.student)){
                                    //remove student from students arraylist in course in database
                                    value.removeStudentFromDB(uid);
                                }
                                //remove course from account courses arraylist in database
                                myAccount.deleteCourseFromDB(value.getCid(),uid);
                                MyGlobalFunctions.refreshActivity(MyCoursesActivity.this);
                            }
                        };
                        course.findCourse(cid, callback_course);
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