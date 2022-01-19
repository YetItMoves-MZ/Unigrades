package com.example.unigrades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button toSignIn;
    private Button signUp;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Spinner teacherOrStudent;
    private EditText editTextFullName;

    private String email="";
    private String password="";
    private String typeTeacherOrStudent="";
    private String fullName="";








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();

        //create a list of items for the spinner.
        String[] itemsForSpinner = new String[]{"student", "teacher"};
        //create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, itemsForSpinner);
        //set the spinners adapter to the previously created one.
        teacherOrStudent.setAdapter(adapter);

        teacherOrStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        typeTeacherOrStudent = Account.student;
                        break;
                    case 1:
                        typeTeacherOrStudent = Account.teacher;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                typeTeacherOrStudent = "";
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outOfListener(SignInActivity.class);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpNewUsers();

            }
        });

    }

    //sign up new users
    private void signUpNewUsers(){
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        fullName = editTextFullName.getText().toString();
        try {
            //TODO guy had a class i didn't watched yet about how to do this properly. change this later to what he did.
            if(typeTeacherOrStudent == ""){
                Toast.makeText(this,
                       "Please insert if you are a teacher or student.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if(fullName == ""){
                Toast.makeText(this,
                        "Please insert full name.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            // sign up new users
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("fbtag", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("fbtag", "createUserWithEmail:failure",
                                        task.getException());
                                Toast.makeText(SignUpActivity.this,
                                        task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                        }
                    });
        } catch (Exception e){
            Log.w("fbtag", "signUpWithEmail:failure",
                    e);
            Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user!=null){
            Toast.makeText(SignUpActivity.this, "Account Created!",
                    Toast.LENGTH_SHORT).show();
            String uid = user.getUid();
            Account acc = new Account().setType(typeTeacherOrStudent).setFullName(fullName);
            acc.addAccountToDB(uid);
            MyGlobalFunctions.startNewActivity(this, MyAccountInfoActivity.class,uid);
        }

    }

    /**
     * This function is used just to get out of listener to get the current activity
     * @param classType the class of the activity we want to start.
     */
    private void outOfListener(Class classType) {
        MyGlobalFunctions.startNewActivity(this, classType,"");
    }


    private void findViews() {
        toSignIn = findViewById(R.id.signUp_BUTTON_toSignIn);
        signUp = findViewById(R.id.signUp_BUTTON_signUp);
        editTextEmail = findViewById(R.id.signUp_EDITTEXT_emailAddress);
        editTextPassword = findViewById(R.id.signUp_EDITTEXT_password);
        teacherOrStudent = findViewById(R.id.signUp_SPINNER_teacherOrStudent);
        editTextFullName = findViewById(R.id.signUp_EDITTEXT_name);
    }
}