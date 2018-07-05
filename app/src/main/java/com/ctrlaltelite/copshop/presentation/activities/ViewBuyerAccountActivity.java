package com.ctrlaltelite.copshop.presentation.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.logic.services.stubs.AccountService;
import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;

public class ViewBuyerAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_buyer_account);

        final Button editBuyerAccount = findViewById(R.id.btnEditBuyerAccount);
        final Button saveBuyerAccount = findViewById(R.id.btnSaveBuyerAccount);
        final Button cancelSaveBuyerAccount = findViewById(R.id.btnCancelSaveBuyerAccount);

        final TextView errorMsg = findViewById(R.id.notLoggedInMsg); // Get error ready, just in case

        final Context context = this;

        // View mode 1
        setFieldFocusability(false);
        setFieldBorder(false);
        editBuyerAccount.setVisibility( View.VISIBLE );
        saveBuyerAccount.setVisibility( View.GONE );
        cancelSaveBuyerAccount.setVisibility( View.GONE );

        populateAccountInfo();

        cancelSaveBuyerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateAccountInfo();

                // View mode 1
                setFieldFocusability(false);
                setFieldBorder(false);
                editBuyerAccount.setVisibility( View.VISIBLE );
                saveBuyerAccount.setVisibility( View.GONE );
                cancelSaveBuyerAccount.setVisibility( View.GONE );
            }
        });

        editBuyerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // View mode 2
                setFieldFocusability(true);
                setFieldBorder(true);
                editBuyerAccount.setVisibility( View.GONE );
                saveBuyerAccount.setVisibility( View.VISIBLE );
                cancelSaveBuyerAccount.setVisibility( View.VISIBLE );
            }
        });

        saveBuyerAccount.setOnClickListener(new View.OnClickListener() {
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

                BuyerAccountValidationObject validationObject = CopShopHub.getAccountService().validate(buyerAccount);

                // Check validation object to see if all fields are valid
                // If valid: store user information
                // Else invalid: check each form field, highlighting those that are invalid in red
                if (validationObject.allValid()) {
                    boolean success = false;

                    String idPref = CopShopHub.getUserSessionService().getUserID();
                    if (idPref != null) {
                        success = CopShopHub.getAccountService().updateBuyerAccount(idPref, buyerAccount);
                    }
                    if (success) {
                        CopShopHub.getUserSessionService().setUserEmail(buyerAccount.getEmail());

                        errorMsg.setText("Account info updated successfully");
                    }
                    else {
                        errorMsg.setText("Account info could not be updated");
                    }

                    // Make sure all form fields are set back to black on success
                    findViewById(R.id.editTextFirstName).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextLastName).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextStreetAddress).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextPostalCode).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextProvince).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextEmail).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextPassword).setBackgroundResource(R.drawable.txt_field_black_border);

                    //go to Listings page
                    //Intent intent = new Intent(ViewBuyerAccountActivity.this, ListingListActivity.class);
                    //startActivity(intent); //goes to listing activity

                    populateAccountInfo();

                    // View mode 1
                    setFieldFocusability(false);
                    setFieldBorder(false);
                    editBuyerAccount.setVisibility( View.VISIBLE );
                    saveBuyerAccount.setVisibility( View.GONE );
                    cancelSaveBuyerAccount.setVisibility( View.GONE );
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

        protected void setFieldFocusability(boolean focusable) {
            findViewById(R.id.editTextFirstName).setFocusable(focusable);
            findViewById(R.id.editTextLastName).setFocusable(focusable);
            findViewById(R.id.editTextStreetAddress).setFocusable(focusable);
            findViewById(R.id.editTextPostalCode).setFocusable(focusable);
            findViewById(R.id.editTextProvince).setFocusable(focusable);
            findViewById(R.id.editTextEmail).setFocusable(focusable);
            findViewById(R.id.editTextPassword).setFocusable(focusable);

            findViewById(R.id.editTextFirstName).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextLastName).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextStreetAddress).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextPostalCode).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextProvince).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextEmail).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextPassword).setFocusableInTouchMode(focusable);

        }

        protected void setFieldBorder(boolean wantBorder) {
            if (wantBorder) {
                findViewById(R.id.editTextFirstName).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextLastName).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextStreetAddress).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextPostalCode).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextProvince).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextEmail).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextPassword).setBackgroundResource(R.drawable.txt_field_black_border);
            }
            else {
                findViewById(R.id.editTextFirstName).setBackgroundResource(0);
                findViewById(R.id.editTextLastName).setBackgroundResource(0);
                findViewById(R.id.editTextStreetAddress).setBackgroundResource(0);
                findViewById(R.id.editTextPostalCode).setBackgroundResource(0);
                findViewById(R.id.editTextProvince).setBackgroundResource(0);
                findViewById(R.id.editTextEmail).setBackgroundResource(0);
                findViewById(R.id.editTextPassword).setBackgroundResource(0);
            }

        }

        protected void populateAccountInfo() {
            // For accessing user info

            // Text for user if logged in
            //TextView greeting = (TextView) findViewById(R.id.editTextFirstName);
            TextView editTextFirstName = findViewById(R.id.editTextFirstName);
            TextView editTextLastName = findViewById(R.id.editTextLastName);
            TextView editTextStreetAddress = findViewById(R.id.editTextStreetAddress);
            TextView editTextPostalCode = findViewById(R.id.editTextPostalCode);
            TextView editTextProvince = findViewById(R.id.editTextProvince);
            TextView editTextEmail = findViewById(R.id.editTextEmail);
            TextView editTextPassword = findViewById(R.id.editTextPassword);

            TextView errorMsg = findViewById(R.id.notLoggedInMsg); // Get error ready, just in case

            // Nothing there if user not logged in
            String emailPref = CopShopHub.getUserSessionService().getUserEmail();
            if (!(emailPref == null)) {

                AccountObject account = CopShopHub.getAccountService().fetchAccountByEmail(emailPref);

                if (account instanceof BuyerAccountObject) {
                    BuyerAccountObject buyer = (BuyerAccountObject)account;

                    if (buyer == null) {
                        errorMsg.setText("No account found");
                        findViewById(R.id.btnEditBuyerAccount).setVisibility( View.GONE );
                    }
                    else {
                        if (editTextFirstName != null) {
                            editTextFirstName.setText(buyer.getFirstName());
                        }
                        if (editTextLastName != null) {
                            editTextLastName.setText(buyer.getLastName());
                        }
                        if (editTextStreetAddress != null) {
                            editTextStreetAddress.setText(buyer.getStreetAddress());
                        }
                        if (editTextPostalCode != null) {
                            editTextPostalCode.setText(buyer.getPostalCode());
                        }
                        if (editTextProvince != null) {
                            editTextProvince.setText(buyer.getProvince());
                        }
                        if (editTextEmail != null) {
                            editTextEmail.setText(buyer.getEmail());
                        }
                        if (editTextPassword != null) {
                            editTextPassword.setText(buyer.getPassword());
                        }
                    }
                }
                else {
                    errorMsg.setText("No account found");
                    findViewById(R.id.btnEditBuyerAccount).setVisibility( View.GONE );
                }
            }
        }

    }

