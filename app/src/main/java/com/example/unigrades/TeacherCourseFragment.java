package com.example.unigrades;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherCourseFragment extends Fragment {

    // the fragment initialization parameters
    private static final String CID = "cid";

    private String cid;
    private AppCompatActivity activity;
    private RecyclerView recyclerViewStudents;
    private RecyclerView recyclerViewComments;
    private AdapterStudent adapterStudent;
    private AdapterComment adapterComment;


    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
    }

    public TeacherCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cid course id.
     * @return A new instance of fragment TeacherCourseFragment.
     */
    public static TeacherCourseFragment newInstance(String cid) {
        TeacherCourseFragment fragment = new TeacherCourseFragment();
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
            Course course = new Course();
            Course.Callback_Course callback_course = new Course.Callback_Course() {
                @Override
                public void dataReady(Course value) {
                    if (value.getStudentComments() != null){
                        adapterComment = new AdapterComment(activity, value.getStudentComments(), value);

                        // Vertically
                        recyclerViewComments.setLayoutManager(new LinearLayoutManager(
                                activity, LinearLayoutManager.VERTICAL, false));

                        recyclerViewComments.setHasFixedSize(true);
                        recyclerViewComments.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewComments.setAdapter(adapterComment);

                        adapterComment.setCommentItemClickListener(new AdapterComment.CommentItemClickListener() {
                            @Override
                            public void deleteClicked(String comment) {
                                //TODO wtf do i do with this...
                            }
                        });
                    }
                    if (value.getStudents() != null){

                        adapterStudent = new AdapterStudent(activity, value.getStudents(), value);

                        // Grid
                        recyclerViewStudents.setLayoutManager(
                                new GridLayoutManager(activity, 1));
                        recyclerViewStudents.setHasFixedSize(true);
                        recyclerViewStudents.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewStudents.setAdapter(adapterStudent);

                        adapterStudent.setStudentItemClickListener(new AdapterStudent.StudentItemClickListener() {
                            @Override
                            public void saveClicked(Course course) {
                                //TODO wtf do i do with this...
                            }
                        });
                    }
                }
            };
            course.findCourse(cid,callback_course);
        }
    }

    private void findViews(View view) {
        recyclerViewStudents = view.findViewById(R.id.courseTeacherFragment_RECYCLERVIEW_students);
        recyclerViewComments = view.findViewById(R.id.courseTeacherFragment_RECYCLERVIEW_comments);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_course, container, false);
        findViews(view);
        return view;

    }
}