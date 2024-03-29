package com.example.unigrades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {


    private Button toSignUp;
    private Button signIn;
    private FirebaseAuth mAuth;
    private TextInputLayout textLayoutEmail;
    private TextInputLayout textLayoutPassword;

    private Validator emailValidator;

    private String email="";
    private String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        findViews();


        emailValidator = Validator.Builder.make(textLayoutEmail).
                addWatcher(new Validator.WatcherEmail("Not email")).
                build();


        mAuth = FirebaseAuth.getInstance();

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outOfListener(SignUpActivity.class);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }

    /**
     * This function sign the user in with the given email and password.
     */
    private void signIn() {
        email = textLayoutEmail.getEditText().getText().toString();
        password = textLayoutPassword.getEditText().getText().toString();
        if(emailValidator.validateIt()){
            try {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("fbtag", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {

                                    // If sign in fails, display a message to the user.
                                    Log.w("fbtag", "signInWithEmail:failure",
                                            task.getException());
                                    Toast.makeText(SignInActivity.this,
                                            task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } catch (Exception e) {
                Log.w("fbtag", "signInWithEmail:failure",
                        e);
                Toast.makeText(SignInActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    private void findViews() {
        toSignUp = findViewById(R.id.signIn_BUTTON_toSignUp);
        signIn = findViewById(R.id.signIn_BUTTON_signIn);
        textLayoutEmail = findViewById(R.id.signIn_EDITTEXT_emailAddress);
        textLayoutPassword = findViewById(R.id.signIn_EDITTEXT_password);

    }

    /**
     * This function updates the activity after getting the user
     * @param user the firebase user
     */
    private void updateUI(FirebaseUser user) {
        if (user!=null){
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


}