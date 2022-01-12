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

public class SignInActivity extends AppCompatActivity {


    private Button toSignUp;
    private Button signIn;
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email="";
    private String password="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        findViews();

        mAuth = FirebaseAuth.getInstance();

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUpActivity();
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }

    private void signIn() {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fireBase", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w("fireBase", "signInWithEmail:failure",
                                    task.getException());
                            Toast.makeText(SignInActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void startSignUpActivity() {
        Intent myIntent = new Intent(this, SignUpActivity.class);
        startActivity(myIntent);
    }

    private void findViews() {
        toSignUp = findViewById(R.id.signIn_BUTTON_toSignUp);
        signIn = findViewById(R.id.signIn_BUTTON_signIn);
        editTextEmail = findViewById(R.id.signIn_EDITTEXT_emailAddress);
        editTextPassword = findViewById(R.id.signIn_EDITTEXT_password);

    }

    private void updateUI(FirebaseUser user) {
        if (user!=null){
            Toast.makeText(SignInActivity.this, "Account Found!",
                    Toast.LENGTH_SHORT).show();
            String uid = user.getUid(); // TODO create firebase database

        }

    }

}