package com.example.unigrades;

public class Student {
    private String name;
    private int grade;
    private String uid;

    public Student() {
    }

    public String getName() {
        return name;
    }
    public Student setName(String name) {
        this.name = name;
        return this;
    }
    public int getGrade() {
        return grade;
    }
    public Student setGrade(int grade) {
        this.grade = grade;
        return this;
    }
    public String getUid() {
        return uid;
    }
    public Student setUid(String uid) {
        this.uid = uid;
        return this;
    }
}
