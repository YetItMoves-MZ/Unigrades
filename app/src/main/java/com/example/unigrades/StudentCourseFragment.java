package com.example.unigrades;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            cid = getArguments().getString(CID);
            findViews();
            Course course = new Course();
            Course.Callback_Course callback_course = new Course.Callback_Course() {
                @Override
                public void dataReady(Course value) {
                    //TODO add stuff to fragment
                }
            };
            course.findCourse(cid,callback_course);

        }
    }

    private void findViews() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_course, container, false);
    }
}