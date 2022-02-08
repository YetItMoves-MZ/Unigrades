package com.example.unigrades;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course extends AccountCourse{

    public interface Callback_Course{
        void dataReady(Course value);
    }

    public interface Callback_Courses{
        void dataReady(ArrayList<Course> value);
    }


    private ArrayList<String> studentComments;
    private ArrayList<Student> students;

    public Course() {
    }
    public Course(AccountCourse newAccountCourse) {
        setCid(newAccountCourse.getCid()).
                setName(newAccountCourse.getName()).
                setTeacherName(newAccountCourse.getTeacherName()).
                setAcademicCredits(newAccountCourse.getAcademicCredits());
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

    public Student getStudent(String uid){
        for(Student student: students){
            if(student.getUid().equals(uid))
                return student;
        }
        return null;
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

    public static void findCourses(Callback_Courses callback_courses){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference myRef = db.collection("courses");
        myRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> arr = queryDocumentSnapshots.getDocuments();
                ArrayList<Course> courses = new ArrayList<>();
                for(DocumentSnapshot documentSnapshot: arr){
                    courses.add(documentSnapshot.toObject(Course.class));
                }
                if(callback_courses != null){
                    callback_courses.dataReady(courses);
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

    public boolean removeStudentFromDB(String uid) {
        if(students != null){
            for(Student student: students){
                if(student.getUid().equals(uid)){
                    students.remove(student);
                    addCourseToDB();
                    return true;
                }
            }
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
        course.put("academicCredits", this.getAcademicCredits());
        course.put("students", this.students);
        course.put("studentComments", this.studentComments);
        DocumentReference myRef = db.collection("courses").document(cid);
        myRef.set(course);
    }

    public void deleteCourseFromDB(FirebaseFirestore db){
        if(getStudents()!= null){
            for(Student student: getStudents()){
                String uid = student.getUid();
                Account acc = new Account();
                Account.Callback_Account callback_account= new Account.Callback_Account(){

                    @Override
                    public void dataReady(Account value) {
                        acc.setAccountByAccount(value);
                        acc.deleteCourseFromDB(getCid(),uid);
                    }
                };
                acc.findAccount(uid, callback_account);
            }
        }
        db.collection("courses").document(getCid()).delete();
    }

}
