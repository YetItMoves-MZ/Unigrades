package com.example.unigrades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {


    private Button toSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        findViews();

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUpActivity();
                finish();
            }
        });


    }

    private void startSignUpActivity() {
        Intent myIntent = new Intent(this, SignUpActivity.class);
        startActivity(myIntent);
    }

    private void findViews() {
        toSignUp = findViewById(R.id.signIn_BUTTON_toSignUp);


    }

    private void updateUI(FirebaseUser user) {
    }

}