package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login); // Grab xml file and display it
        final Context context = this;
        final TextView errorMsg = (TextView) findViewById(R.id.incorrectCredentialsMsg); // Get error ready, just in case

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               EditText etEmail = (EditText) findViewById(R.id.email);
                String userEmail = etEmail.getText().toString(); // Grab text from text box

                EditText etPassword = (EditText) findViewById(R.id.password);
                String password = etPassword.getText().toString(); // Grab password from textbox

                AccountObject user = CopShopHub.getAccountService().validateEmailAndPassword(userEmail,password);

                if (user == null) {
                    errorMsg.setText("Username or password is incorrect");
                } else {

                    boolean success = CopShopHub.getUserSessionService().loginUser(user);
                    if (success) {
                        // Go to Listings page
                        Intent intent = new Intent(LoginActivity.this, ListingListActivity.class);
                        startActivity(intent); // Goes to listing activity
                    } else {
                        errorMsg.setText("Account does not exist"); // Would only happen if DB contains invalid info or a model is broken
                    }
                }
            }
        });

        // Register button
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, ListingListActivity.class);
        startActivity(intent);
    }
}
