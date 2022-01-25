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


}
