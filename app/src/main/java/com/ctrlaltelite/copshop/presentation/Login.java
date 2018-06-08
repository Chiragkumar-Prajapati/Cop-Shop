package com.ctrlaltelite.copshop.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.logic.services.stubs.AccountService;
import com.ctrlaltelite.copshop.objects.AccountObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //grab xml file and display it
        final TextView errorMsg = (TextView) findViewById(R.id.incorrectCredentialsMsg); //get error ready, just in case

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               EditText etEmail = (EditText) findViewById(R.id.email);
                String userEmail = etEmail.toString(); //grab text from text box

                EditText etPassword = (EditText) findViewById(R.id.password);
                String password = etPassword.toString(); //grab password from textbox

                AccountService check = new AccountService();
                AccountObject user = check.validateUsernameAndPassword(userEmail,password);

                if (user==null){
                    errorMsg.setText("What's all this, then? You're going to need" +
                            " a valid username and password. Try again.");
                }
                else{
                    //go to Listings page for current user
                    //Intent intent = new Intent(Login.this, LISTING ACTIVITY NAME .class)
                    //intent.putExtra("CurrentUser", user); //adds user to things getting passed
                    //startActivity(intent); //goes to listing activity
                }


            }
        });
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Login.this, Create_account.class);
                startActivity(intent);
            }
        });
    }
}
