package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    private TextView textViewCourseName;
    private TextView textViewAverageScores;
    private TextView textViewAcademicCredits;
    private StudentCourseFragment studentCourseFragment;
    private TeacherCourseFragment teacherCourseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
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
                Bundle b = getIntent().getExtras().getBundle("bundle");
                String cid = b.getString("cid");
                Course course = new Course();
                Course.Callback_Course callback_course = new Course.Callback_Course() {
                    @Override
                    public void dataReady(Course value) {
                        textViewCourseName.setText("Course " + value.getName());
                        textViewAcademicCredits.setText("Academic Credits: " + value.getAcademicCredits());
                        double averageScores = calculateAverageGrade(value.getStudents());
                        if(averageScores > 0){
                            textViewAverageScores.
                                    setText("Average scores on course: " + MyGlobalFunctions.df.format(averageScores));
                        }
                        else{
                            textViewAverageScores.
                                    setText("No scores given yet.");
                        }
                        if(myAccount.getType().equals(Account.teacher)){
                            //teacher
                            teacherCourseFragment = TeacherCourseFragment.newInstance(cid);
                            teacherCourseFragment.setActivity(CourseActivity.this);
                            getSupportFragmentManager().
                                    beginTransaction().
                                    add(R.id.course_FRAMELAYOUT_teacherOrStudent, teacherCourseFragment).
                                    commit();

                        }
                        else if(myAccount.getType().equals(Account.student)){
                            //student
                            studentCourseFragment = StudentCourseFragment.newInstance(cid);
                            studentCourseFragment.setActivity(CourseActivity.this);
                            getSupportFragmentManager().
                                    beginTransaction().
                                    add(R.id.course_FRAMELAYOUT_teacherOrStudent, studentCourseFragment).
                                    commit();
                        }
                        else{
                            //shouldn't be able to get here.
                        }
                    }
                };
                course.findCourse(cid, callback_course);
            }
        };
        myAccount.findAccount(uid, callback_account);
    }

    private double calculateAverageGrade(ArrayList<Student> students) {
        double averageScores = -1;
        int countGradelessStudents = 0;
        if(students != null){
            for(Student student: students){
                if(student.getGrade()>=0){
                    averageScores+=student.getGrade();
                }
                else{
                    countGradelessStudents++;
                }
            }
        }
        if (averageScores != -1){
            averageScores++;
            averageScores /=  (students.size()-countGradelessStudents);
        }
        return averageScores;
    }

    private void findViews() {
        textViewCourseName = findViewById(R.id.course_TEXT_courseName);
        textViewAverageScores = findViewById(R.id.course_TEXT_averageScore);
        textViewAcademicCredits = findViewById(R.id.course_TEXT_academicCredits);
    }
}