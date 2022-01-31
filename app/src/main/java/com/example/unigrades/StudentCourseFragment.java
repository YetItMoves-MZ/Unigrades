package com.example.unigrades;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentCourseFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CID = "cid";

    private String cid;
    private AppCompatActivity activity;
    private TextView grade;
    private TextInputLayout comment;
    private Button send;

    private Validator validatorComment;

    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
    }

    public StudentCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cid course id.
     * @return A new instance of fragment StudentCourseFragment.
     */
    public static StudentCourseFragment newInstance(String cid) {
        StudentCourseFragment fragment = new StudentCourseFragment();
        Bundle args = new Bundle();
        args.putString(CID, cid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            validatorComment = Validator.Builder.make(comment).
                    addWatcher(new Validator.WatcherMinimumText("comment cannot be empty", 1)).
                    build();
            cid = getArguments().getString(CID);
            Course course = new Course();
            Course.Callback_Course callback_course = new Course.Callback_Course() {
                @Override
                public void dataReady(Course value) {
                    Student me = null;
                    String uid = FirebaseAuth.getInstance().getUid();
                    if(value.getStudents() != null){
                        for(Student student: value.getStudents()){
                            if(student.getUid().equals(uid)){
                                me = student;
                                break;
                            }
                        }
                        if(me != null){
                            // signed in
                            if(me.getGrade() < 0){
                                grade.setText("teacher did not update your grade yet");
                            }
                            else{
                                grade.setText(String.valueOf(me.getGrade()));
                            }
                            send.setVisibility(View.VISIBLE);
                            send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String inputComment = comment.getEditText().getText().toString();
                                    if (!inputComment.equals("")){
                                        if(value.getStudentComments() == null)
                                            value.setStudentComments(new ArrayList<String>());

                                        value.getStudentComments().add(inputComment);
                                        value.addCourseToDB();
                                        comment.getEditText().setText("");
                                        Toast.makeText(activity,
                                                "comment added",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else{
                            grade.setText("not signed in yet");
                        }
                    }
                    else{
                        grade.setText("not signed in yet");
                    }
                }
            };
            course.findCourse(cid,callback_course);

        }
    }

    private void findViews(View view) {

        grade = view.findViewById(R.id.courseStudentFragment_TEXT_grade);
        comment = view.findViewById(R.id.courseStudentFragment_EDITTENXT_comments);
        send = view.findViewById(R.id.courseStudentFragment_BUTTON_sendComments);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_student_course, container, false);
        findViews(view);
        return view;
    }
}