package com.example.unigrades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MyAccountInfoActivity extends AppCompatActivity {


    private TextView textName;
    private EditText changeName;
    private EditText changePassword;
    private Button saveInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_info);
        //TODO repeat those 2 always from now on.
        String uid = MyGlobalFunctions.getUidFromBundle(getIntent().getExtras().
                getBundle(MyGlobalFunctions.BUNDLE));
        Toolbar toolbar = new Toolbar(this);

        findViews();

        // show info based on uid:
        Account myAccount = new Account();
        Account.Callback_Account callback_account= new Account.Callback_Account(){

            @Override
            public void dataReady(Account value) {
                myAccount.setAccountByAccount(value);
                textName.setText("Welcome, " + myAccount.getFullName());
            }
        };
        myAccount.findAccount(uid, callback_account);

        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = changeName.getText().toString();
                String newPassword = changePassword.getText().toString();
                if(!newName.equals("")){
                    myAccount.setFullName(newName);
                    myAccount.addAccountToDB(uid);
                    textName.setText("Welcome, " + myAccount.getFullName());
                }
                if(!newPassword.equals("")){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(newPassword);
                }
            }
        });

    }

    private void findViews() {
        textName = findViewById(R.id.myAccountInfo_TEXT_name);
        changeName = findViewById(R.id.myAccountInfo_EDITTEXT_name);
        changePassword = findViewById(R.id.myAccountInfo_EDITTEXT_Password);
        saveInfo = findViewById(R.id.myAccountInfo_BUTTON_save);
    }


}