package com.ctrlaltelite.copshop.presentation.activities;

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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login); // Grab xml file and display it

        final TextView errorMsg = (TextView) findViewById(R.id.incorrectCredentialsMsg); // Get error ready, just in case
        final SharedPreferences sharedPreferences = getSharedPreferences("currentUser", 0);

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
                    errorMsg.setText("What's all this, then? You're going to need" +
                            " a valid username and password. Try again.");
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userID", user.getId());
                    editor.putString("email", user.getEmail());
                    editor.commit(); //saves user info in the SharedPreferences object

                    //go to Listings page
                    Intent intent = new Intent(LoginActivity.this, ListingListActivity.class);
                    startActivity(intent); //goes to listing activity
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


}
