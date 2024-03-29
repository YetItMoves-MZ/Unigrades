package com.example.unigrades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button toSignIn;
    private Button signUp;
    private TextInputLayout textLayoutEmail;
    private TextInputLayout textLayoutPassword;
    private Spinner teacherOrStudent;
    private TextInputLayout textLayoutFullName;

    private String email="";
    private String password="";
    private String typeTeacherOrStudent="";
    private String fullName="";

    private Validator emailValidator;
    private Validator passwordValidator;
    private Validator nameValidator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();

        emailValidator = Validator.Builder.make(textLayoutEmail).
                addWatcher(new Validator.WatcherEmail("Not email")).
                build();
        passwordValidator = Validator.Builder.make(textLayoutPassword).
                addWatcher(new Validator.WatcherAtLeastOneUpperCase("At least one upper case")).
                addWatcher(new Validator.WatcherAtLeastOneLowerCase("At least one lower case")).
                addWatcher(new Validator.WatcherAtLeastOneNumber("At least one number")).
                addWatcher(new Validator.WatcherMinimumText("Password must contain at least 8 letters", 8)).
                build();
        nameValidator = Validator.Builder.make(textLayoutFullName).
                addWatcher(new Validator.WatcherMaximumText("Name cannot be longer than " + MyGlobalFunctions.MAXIMUM_NAME_SIZE, MyGlobalFunctions.MAXIMUM_NAME_SIZE)).
                addWatcher(new Validator.WatcherStartWithUpperCase("Start with upper case")).
                build();

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
                if(emailValidator.validateIt() &&
                        nameValidator.validateIt() &&
                        passwordValidator.validateIt() &&
                        (!typeTeacherOrStudent.equals(""))){
                    signUpNewUser();
                }
            }
        });

    }

    //sign up new users
    private void signUpNewUser(){
        email = textLayoutEmail.getEditText().getText().toString();
        password = textLayoutPassword.getEditText().getText().toString();
        fullName = textLayoutFullName.getEditText().getText().toString();
        try {
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
            MyGlobalFunctions.startNewActivity(this, MyAccountInfoActivity.class);
        }

    }

    /**
     * This function is used just to get out of listener to get the current activity
     * @param classType the class of the activity we want to start.
     */
    private void outOfListener(Class classType) {
        MyGlobalFunctions.startNewActivity(this, classType);
    }


    private void findViews() {
        toSignIn = findViewById(R.id.signUp_BUTTON_toSignIn);
        signUp = findViewById(R.id.signUp_BUTTON_signUp);
        textLayoutEmail = findViewById(R.id.signUp_EDITTEXT_emailAddress);
        textLayoutPassword = findViewById(R.id.signUp_EDITTEXT_password);
        teacherOrStudent = findViewById(R.id.signUp_SPINNER_teacherOrStudent);
        textLayoutFullName = findViewById(R.id.signUp_EDITTEXT_name);
    }
}