package com.example.unigrades;

public class AccountCourse {
    private String name;
    private String cid;
    private String teacherName;

    public AccountCourse() {
    }
    public AccountCourse(Course course){
        this.name = course.getName();
        this.cid = course.getCid();
        this.teacherName = course.getTeacherName();
    }

    public String getName() {
        return name;
    }
    public AccountCourse setName(String name) {
        this.name = name;
        return this;
    }
    public String getCid() {
        return cid;
    }
    public AccountCourse setCid(String cid) {
        this.cid = cid;
        return this;
    }
    public String getTeacherName() {
        return teacherName;
    }
    public AccountCourse setTeacherName(String teacherName) {
        this.teacherName = teacherName;
        return this;
    }
}
