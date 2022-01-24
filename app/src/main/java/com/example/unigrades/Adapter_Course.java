package com.example.unigrades;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

/**
 * used for recycle view
 */
public class Adapter_Course extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<Course> courses = new ArrayList<>();
    private CourseItemClickListener courseItemClickListener;
    private boolean hasAddButton;
    private String uid;

    public Adapter_Course(Activity activity, ArrayList<Course> courses, String uid, boolean hasAddButton){
        this.activity = activity;
        this.courses = courses;
        this.uid = uid;
        this.hasAddButton = hasAddButton;
    }

    public Adapter_Course setCourseItemClickListener(CourseItemClickListener courseItemClickListener){
        this.courseItemClickListener = courseItemClickListener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_course, viewGroup, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CourseViewHolder courseViewHolder = (CourseViewHolder) holder;
        Course course = getItem(position);
        Log.d("course_list", "position = " + position);

        courseViewHolder.courseName.setText(course.getName());
        courseViewHolder.teacherName.setText(course.getTeacherName());



    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


    private Course getItem(int position) {
        return courses.get(position);
    }

    public interface CourseItemClickListener {
        void courseItemClicked(Course course);
        void signInClicked(Course course);
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder{
        public AppCompatImageView addCourseButton;
        public MaterialTextView courseName;
        public MaterialTextView teacherName;
        public CourseViewHolder(final View itemView){
            super(itemView);
            this.addCourseButton = itemView.findViewById(R.id.listCourse_IMG_addCourse);
            this.courseName = itemView.findViewById(R.id.listCourse_TEXT_courseName);
            this.teacherName = itemView.findViewById(R.id.listCourse_TEXT_teacherName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    courseItemClickListener.
                            courseItemClicked(getItem(getAdapterPosition()));
                }
            });
            // check if add course is enabled
            if(hasAddButton){
                addCourseButton.setVisibility(View.VISIBLE);
                addCourseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Course currentCourse = getItem(getAdapterPosition());
                        boolean flag = false;
                        if(currentCourse.getStudents() != null){
                            for (Student student: currentCourse.getStudents()){
                                if(student.getUid().equals(uid)){
                                    flag = true;
                                }
                            }
                        }
                        // check if student is not already signed in.
                        if(!flag){
                            courseItemClickListener.signInClicked(currentCourse);
                        }
                        else{
                            //TODO check if i can make the sign in button disappear instead.
                            Toast.makeText(activity,"student already signed in.",Toast.LENGTH_LONG);
                        }
                    }
                });






            }

        }

    }
}
