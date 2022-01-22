package com.example.unigrades;

import android.os.Bundle;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Course extends AccountCourse{
    private ArrayList<String> studentComments;
    private ArrayList<Student> students;

    public Course() {
    }
    public Course(AccountCourse newAccountCourse) {
        setCid(newAccountCourse.getCid()).
                setName(newAccountCourse.getName()).
                setTeacherName(newAccountCourse.getTeacherName());
    }

    public ArrayList<String> getStudentComments() {
        return studentComments;
    }
    public Course setStudentComments(ArrayList<String> studentComments) {
        this.studentComments = studentComments;
        return this;
    }
    public ArrayList<Student> getStudents() {
        return students;
    }
    public Course setStudents(ArrayList<Student> students) {
        this.students = students;
        return this;
    }

    public void addCourseToDB(){
        String cid = this.getCid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> course = new HashMap<>();
        course.put("cid", cid);
        course.put("name", this.getName());
        course.put("teacherName", this.getTeacherName());
        course.put("students", this.students);
        course.put("studentComments", studentComments);
        DocumentReference myRef = db.collection("courses").document(cid);
        myRef.set(course);

        //TODO need to continue this.


    }

}
