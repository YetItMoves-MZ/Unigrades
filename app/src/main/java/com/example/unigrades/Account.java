package com.example.unigrades;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Account {


    public interface Callback_Account{
        void dataReady(Account value);
    }

    public interface Callback_AverageGrade{
        void dataReady(double value);
    }

    public static final String student="Student";
    public static final String teacher="Teacher";

    private String type;
    private ArrayList<AccountCourse> accountCourses;
    private String fullName;


    public Account(){}

    public String getType() {
        return type;
    }
    public Account setType(String type) {
        this.type = type;
        return this;
    }
    public ArrayList<AccountCourse> getAccountCourses() {
        return accountCourses;
    }
    public Account setAccountCourses(ArrayList<AccountCourse> courses) {
        this.accountCourses = courses;
        return this;
    }

    public ArrayList<Course> getCourses(){
        if(this.accountCourses == null)
            return null;
        ArrayList<Course> courses = new ArrayList<Course>();
        for(AccountCourse course: this.accountCourses){
            courses.add(new Course(course));
        }
        return courses;
    }

    public String getFullName() {
        return fullName;
    }
    public Account setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }



    public void addAccountToDB(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> acc = new HashMap<>();
        acc.put("type", this.type);
        acc.put("accountCourses", this.accountCourses);
        acc.put("fullName", this.fullName);
        DocumentReference myRef = db.collection("users").document(uid);
        myRef.set(acc);
    }

    public void findAccount(String uid, Callback_Account callback_account){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference myRef = db.collection("users").document(uid);
        myRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Account acc = documentSnapshot.toObject(Account.class);
                    if(callback_account != null){
                        callback_account.dataReady(acc);
                    }
                }
            }
        });
    }

    public void getAverageGrade(String uid, Callback_AverageGrade callback_averageGrade){
        if(accountCourses == null || accountCourses.size() == 0){
            if(callback_averageGrade != null){
                callback_averageGrade.dataReady(-1);
            }
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Integer> allGrades = new ArrayList<Integer>();
        //allGrades in index 0 = courses found.
        allGrades.add(0,0);
        for(AccountCourse course: this.getAccountCourses()){
            Course c = new Course();
            c.findCourse(course.getCid(), new Course.Callback_Course() {
                @Override
                public void dataReady(Course value) {
                    allGrades.set(0,allGrades.get(0)+1);
                    int grade = value.getStudent(uid).getGrade();
                    if(grade>0){
                        allGrades.add(grade);
                    }
                    if(allGrades.get(0) == getAccountCourses().size()){
                        // got final grade.
                        double avg = 0;
                        if(allGrades.size()==1){
                            avg = -1;
                        }
                        else{
                            for (int i=1; i<allGrades.size();i++){
                                //starting with i=1 so it wont calculate the amount of courses found.
                                avg+= allGrades.get(i);
                            }
                            avg/=(allGrades.size()-1);
                        }
                        if(callback_averageGrade != null){
                            callback_averageGrade.dataReady(avg);
                        }
                    }
                }
            });
        }
    }

    public double getAcademicGradeTotal(){
        double total = 0;
        if(accountCourses != null) {
            for(AccountCourse course: accountCourses){
                total += course.getAcademicCredits();
            }
        }
        return total;
    }

    public void setAccountByAccount(Account other) {
        type = other.getType();
        fullName = other.getFullName();

        accountCourses = other.getAccountCourses();
        if(accountCourses == null){//no courses are up yet:
            accountCourses = new ArrayList<>();
        }
    }

    public boolean hasCourse(String cid) {
        for(AccountCourse course: accountCourses){
            if(course.getCid().equals(cid))
                return true;
        }
        return false;
    }

    public void deleteCourseFromDB(String cid, String uid) {

        for(AccountCourse course: this.getAccountCourses()){
            if(course.getCid().equals(cid)){
                this.getAccountCourses().remove(course);
                this.addAccountToDB(uid);
                return;
            }
        }
    }
}
