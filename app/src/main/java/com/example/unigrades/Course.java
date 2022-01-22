package com.example.unigrades;

import android.os.Bundle;

import java.util.ArrayList;

public class Course extends AccountCourse{
    private ArrayList<String> studentComments;
    private ArrayList<Student> students;

    public Course() {
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
}
