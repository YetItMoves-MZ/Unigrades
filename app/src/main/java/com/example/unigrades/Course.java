package com.example.unigrades;

import android.os.Bundle;

public class Course {
    String name;
    int maxNumOfStudents = 0;
    Student students[];
    Teacher teacher;

    public Course() {
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public int getMaxNumOfStudents() {
        return maxNumOfStudents;
    }

    public Course setMaxNumOfStudents(int maxNumOfStudents) {
        this.maxNumOfStudents = maxNumOfStudents;
        return this;
    }

    public Student[] getStudents() {
        return students;
    }

    public Course setStudents(Student[] students) {
        this.students = students;
        return this;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Course setTeacher(Teacher teacher) {
        this.teacher = teacher;
        return this;
    }

    public void setCourseFromBundle(Bundle bundle){
        //TODO

    }
    public void setBundleFromCourse(Bundle bundle){
        //TODO

    }


}
