package com.example.unigrades;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course extends AccountCourse{

    public interface Callback_Course{
        void dataReady(Course value);
    }


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

    public void findCourse(String cid, Course.Callback_Course callback_course){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference myRef = db.collection("courses").document(cid);
        myRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Course course = documentSnapshot.toObject(Course.class);
                    if(callback_course != null){
                        callback_course.dataReady(course);
                    }
                }
            }
        });
    }

    public boolean hasStudent(String uid){
        if(this.getStudents() == null)
            return false;
        for(Student student: this.getStudents()){
            if(student.getUid().equals(uid))
                return true;
        }
        return false;
    }

    public void addCourseToDB(){
        String cid = this.getCid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> course = new HashMap<>();
        course.put("cid", cid);
        course.put("name", this.getName());
        course.put("teacherName", this.getTeacherName());
        course.put("students", this.students);
        course.put("studentComments", this.studentComments);
        DocumentReference myRef = db.collection("courses").document(cid);
        myRef.set(course);
    }

}
