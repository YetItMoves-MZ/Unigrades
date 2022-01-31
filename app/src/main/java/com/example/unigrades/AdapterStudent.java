package com.example.unigrades;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class AdapterStudent extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Activity activity;
    private ArrayList<Student> students = new ArrayList<>();
    private AdapterStudent.StudentItemClickListener studentItemClickListener;
    private Course currentCourse;

    public AdapterStudent(Activity activity, ArrayList<Student> students, Course currentCourse){
        this.activity = activity;
        this.students = students;
        this.currentCourse = currentCourse;
    }

    public AdapterStudent setStudentItemClickListener(AdapterStudent.StudentItemClickListener studentItemClickListener){
        this.studentItemClickListener = studentItemClickListener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_student, viewGroup, false);
        return new AdapterStudent.StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterStudent.StudentViewHolder studentViewHolder = (AdapterStudent.StudentViewHolder) holder;
        Student student = getItem(position);
        Log.d("student_list", "position = " + position);

        studentViewHolder.studentName.setText(student.getName());
        if(student.getGrade()>=0){
            int grade = student.getGrade();
            studentViewHolder.grade.setText(String.valueOf(grade));
        }

    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    private Student getItem(int position) {
        return students.get(position);
    }

    public interface StudentItemClickListener {
        void saveClicked(int position, int grade);
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder{
        public AppCompatImageView saveButton;
        public MaterialTextView studentName;
        public AppCompatEditText grade;
        public StudentViewHolder(final View itemView){
            super(itemView);
            this.saveButton = itemView.findViewById(R.id.listStudent_BUTTON_saveGrade);
            this.studentName = itemView.findViewById(R.id.listStudent_TEXT_studentName);
            this.grade = itemView.findViewById(R.id.listStudent_EDITTEXT_grade);


            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String gradeString = grade.getText().toString();
                    if(!gradeString.equals(""))
                    {
                        int gradeNumber =Integer.parseInt(gradeString);
                        studentItemClickListener.saveClicked(getAdapterPosition(), gradeNumber);
                    }
                }
            });








        }

    }
}
