package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchCourseActivity extends AppCompatActivity {

    private EditText editTextCourseName;
    private EditText editTextTeacherName;
    private Button searchButton;
    private RecyclerView recyclerViewCourses;

    private ArrayList<Course> allCourses;
    private ArrayList<Course> searchedCourses;
    Adapter_Course adapter_course;

    public interface Callback_Courses{
        void dataReady(ArrayList<Course> value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);
        String uid = FirebaseAuth.getInstance().getUid();
        Toolbar toolbar = new Toolbar(this);

        findViews();

        // get all courses

        Callback_Courses callback_courses = new Callback_Courses() {
            @Override
            public void dataReady(ArrayList<Course> value) {
                allCourses = value;
                adapter_course = new Adapter_Course(SearchCourseActivity.this, allCourses);

                //Grid
                recyclerViewCourses.setLayoutManager(new GridLayoutManager(
                        SearchCourseActivity.this, 1));

                // Vertically
                // recyclerViewCourses.setLayoutManager(new LinearLayoutManager(
                // SearchCourseActivity.this, LinearLayoutManager.VERTICAL, false));

                recyclerViewCourses.setHasFixedSize(true);
                recyclerViewCourses.setItemAnimator(new DefaultItemAnimator());
                recyclerViewCourses.setAdapter(adapter_course);

                adapter_course.setCourseItemClickListener(new Adapter_Course.CourseItemClickListener() {
                    @Override
                    public void courseItemClicked(Course course, int position) {
                        //TODO change toast into switch activity to course activity with the cid.
                        Toast.makeText(SearchCourseActivity.this, "you clicked on: " +
                                course.getName(), Toast.LENGTH_LONG).show();
                        MyGlobalFunctions.startNewActivity(SearchCourseActivity.this, CourseActivity.class, course.getCid());


                    }
                });

                searchButton.setVisibility(View.VISIBLE);
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String teacherName = editTextTeacherName.getText().toString();
                        String courseName = editTextCourseName.getText().toString();
                        searchedCourses = new ArrayList<Course>();
                        if (teacherName.equals("") && courseName.equals("")) {
                            searchedCourses = allCourses;
                        } else {
                            for (Course course : allCourses) {

                                // checks if the non empty searched value are contained in the course fields.
                                if ((!courseName.equals("") && course.getName().contains(courseName)) ||
                                        (!teacherName.equals("") && course.getTeacherName().contains(teacherName))) {
                                    searchedCourses.add(course);
                                }
                            }
                        }
                        adapter_course = new Adapter_Course(SearchCourseActivity.this, searchedCourses);
                        recyclerViewCourses.setAdapter(adapter_course);

                        adapter_course.setCourseItemClickListener(new Adapter_Course.CourseItemClickListener() {
                            @Override
                            public void courseItemClicked(Course course, int position) {
                                //TODO change toast into switch activity to course activity with the cid.
                                Toast.makeText(SearchCourseActivity.this, "you clicked on: " +
                                        course.getName(), Toast.LENGTH_LONG).show();
                                MyGlobalFunctions.startNewActivity(SearchCourseActivity.this, CourseActivity.class, course.getCid());

                            }
                        });

                    }
                });
            }
        };
        findCourses(callback_courses);

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

    public void findCourses(Callback_Courses callback_courses){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference myRef = db.collection("courses");
        myRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //TODO check if it works on non existing collection
                List<DocumentSnapshot> arr = queryDocumentSnapshots.getDocuments();
                ArrayList<Course> courses = new ArrayList<>();
                for(DocumentSnapshot documentSnapshot: arr){
                    courses.add(documentSnapshot.toObject(Course.class));
                }
                if(callback_courses != null){
                    callback_courses.dataReady(courses);
                }
            }
        });
    }

    private void findViews() {
        editTextCourseName = findViewById(R.id.searchCourse_EDITTEXT_courseName);
        editTextTeacherName = findViewById(R.id.searchCourse_EDITTEXT_teacherName);
        searchButton = findViewById(R.id.searchCourse_BUTTON_search);
        recyclerViewCourses = findViewById(R.id.searchCourse_RECYCLERVIEW_courses);
    }
}