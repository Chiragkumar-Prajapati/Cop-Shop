package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountValidationObject;

public class CreateSellerAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_seller_account);

        Button createSellerAccount = findViewById(R.id.btnCreateSellerAccount);
        createSellerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerAccountObject sellerAccount = new SellerAccountObject(
                    "",
                    ((EditText) findViewById(R.id.editTextOrganizationName)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextStreetAddress)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextPostalCode)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextProvince)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextEmail)).getText().toString(),
                    ((EditText) findViewById(R.id.editTextPassword)).getText().toString()
            );

                SellerAccountValidationObject validationObject = CopShopHub.getAccountService().validate(sellerAccount);

            // Check validation object to see if all fields are valid
            // If valid: store user information
            // Else invalid: check each form field, highlighting those that are invalid in red
            if (validationObject.allValid()) {
                String id = CopShopHub.getAccountService().registerNewSeller(sellerAccount);
                if (id == null) {
                    // It actually failed, presumably due to a duplicate email address.
                    findViewById(R.id.editTextEmail).setBackgroundResource(R.drawable.txt_field_red_border);
                }
                else // success!
                {
                    // Make sure all form fields are set back to black on success
                    findViewById(R.id.editTextOrganizationName).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextStreetAddress).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextPostalCode).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextProvince).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextEmail).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextPassword).setBackgroundResource(R.drawable.txt_field_black_border);

                    // Go to login page
                    startActivity(new Intent(CreateSellerAccountActivity.this, LoginActivity.class));
                }
            } else {

                // Check precinct name
                if (!validationObject.getValidOrganizationName()) {
                    findViewById(R.id.editTextOrganizationName).setBackgroundResource(R.drawable.txt_field_red_border);
                } else {
                    findViewById(R.id.editTextOrganizationName).setBackgroundResource(R.drawable.txt_field_black_border);
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

