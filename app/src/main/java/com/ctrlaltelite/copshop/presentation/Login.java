package com.ctrlaltelite.copshop.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.ctrlaltelite.copshop.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //grab xml file and display it
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TO DO: implement login verification
                //call logic to verify email and password in the database
                //on success go to Listings Activity
                //on fail print error and go nowhere
            }
        });
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Login.this, Create_account.class);
                startActivity(intent);
            }
        });
    }
}
