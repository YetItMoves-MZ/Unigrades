package com.example.unigrades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private String email="";
    private String password="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        String email = "test@test.com";
        String password = "1234";

        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignInActivity();
                finish();
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
        // sign up new users
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fireBase", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fireBase", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show(); // TODO: add reasons why.
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user!=null){
            Toast.makeText(SignUpActivity.this, "Account Created!",
                    Toast.LENGTH_SHORT).show();
            String uid = user.getUid(); // TODO create firebase database

        }

    }

    private void startSignInActivity() {
        Intent myIntent = new Intent(this, SignInActivity.class);
        startActivity(myIntent);
    }

    private void findViews() {
        toSignIn = findViewById(R.id.signUp_BUTTON_toSignIn);
        signUp = findViewById(R.id.signUp_BUTTON_signUp);
        editTextEmail = findViewById(R.id.signUp_EDITTEXT_emailAddress);
        editTextPassword = findViewById(R.id.signUp_EDITTEXT_password);

    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
    */
}