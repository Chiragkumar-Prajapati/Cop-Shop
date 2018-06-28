package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopApp;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;

public class ViewBuyerAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_buyer_account);

        Button createBuyerAccount = findViewById(R.id.btnCreateBuyerAccount);
        createBuyerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            BuyerAccountObject buyerAccount = new BuyerAccountObject(
                    "",
                    ((EditText) findViewById(R.id.editTextFirstName)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextLastName)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextStreetAddress)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextPostalCode)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextProvince)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextEmail)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextPassword)).getText().toString()
            );

            BuyerAccountValidationObject validationObject = CopShopApp.accountService.validate(buyerAccount);

            // Check validation object to see if all fields are valid
            // If valid: store user information
            // Else invalid: check each form field, highlighting those that are invalid in red
            if (validationObject.allValid()) {
                CopShopApp.accountService.registerNewBuyer(buyerAccount);

                // Make sure all form fields are set back to black on success
                findViewById(R.id.editTextFirstName).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextLastName).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextStreetAddress).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextPostalCode).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextProvince).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextEmail).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextPassword).setBackgroundResource(R.drawable.txt_field_black_border);


                // Go to login page
                startActivity(new Intent(ViewBuyerAccountActivity.this, LoginActivity.class));
            } else {

                // Check first name
                if (!validationObject.getValidFirstName()) {
                    findViewById(R.id.editTextFirstName).setBackgroundResource(R.drawable.txt_field_red_border);
                } else {
                    findViewById(R.id.editTextFirstName).setBackgroundResource(R.drawable.txt_field_black_border);
                }

                // Check last name
                if (!validationObject.getValidLastName()) {
                    findViewById(R.id.editTextLastName).setBackgroundResource(R.drawable.txt_field_red_border);
                } else {
                    findViewById(R.id.editTextLastName).setBackgroundResource(R.drawable.txt_field_black_border);
                }

                // Check street address
                if (!validationObject.getValidStreetAddress()) {
                    findViewById(R.id.editTextStreetAddress).setBackgroundResource(R.drawable.txt_field_red_border);
                } else {
                    findViewById(R.id.editTextStreetAddress).setBackgroundResource(R.drawable.txt_field_black_border);
                }

                // Check postal code
                if (!validationObject.getValidPostalCode()) {
                    findViewById(R.id.editTextPostalCode).setBackgroundResource(R.drawable.txt_field_red_border);
                } else {
                    findViewById(R.id.editTextPostalCode).setBackgroundResource(R.drawable.txt_field_black_border);
                }

                // Check province
                if (!validationObject.getValidProvince()) {
                    findViewById(R.id.editTextProvince).setBackgroundResource(R.drawable.txt_field_red_border);
                } else {
                    findViewById(R.id.editTextProvince).setBackgroundResource(R.drawable.txt_field_black_border);
                }

                // Check email
                if (!validationObject.getValidEmail()) {
                    findViewById(R.id.editTextEmail).setBackgroundResource(R.drawable.txt_field_red_border);
                } else {
                    findViewById(R.id.editTextEmail).setBackgroundResource(R.drawable.txt_field_black_border);
                }

                //Check password
                if (!validationObject.getValidPassword()) {
                    findViewById(R.id.editTextPassword).setBackgroundResource(R.drawable.txt_field_red_border);
                } else {
                    findViewById(R.id.editTextPassword).setBackgroundResource(R.drawable.txt_field_black_border);
                }
            }
        }
    });
        }
    }

