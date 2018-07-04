package com.ctrlaltelite.copshop.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountValidationObject;

public class ViewSellerAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_seller_account);

        final Button editSellerAccount = findViewById(R.id.btnEditSellerAccount);
        final Button saveSellerAccount = findViewById(R.id.btnSaveSellerAccount);
        final Button cancelSaveSellerAccount = findViewById(R.id.btnCancelSaveSellerAccount);

        final TextView errorMsg = findViewById(R.id.notLoggedInMsg); // Get error ready, just in case



        // View mode 1
        setFieldFocusability(false);
        setFieldBorder(false);
        editSellerAccount.setVisibility( View.VISIBLE );
        saveSellerAccount.setVisibility( View.GONE );
        cancelSaveSellerAccount.setVisibility( View.GONE );

        populateAccountInfo();

        cancelSaveSellerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateAccountInfo();

                // View mode 1
                setFieldFocusability(false);
                setFieldBorder(false);
                editSellerAccount.setVisibility( View.VISIBLE );
                saveSellerAccount.setVisibility( View.GONE );
                cancelSaveSellerAccount.setVisibility( View.GONE );
            }
        });

        editSellerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // View mode 2
                setFieldFocusability(true);
                setFieldBorder(true);
                editSellerAccount.setVisibility( View.GONE );
                saveSellerAccount.setVisibility( View.VISIBLE );
                cancelSaveSellerAccount.setVisibility( View.VISIBLE );
            }
        });

        saveSellerAccount.setOnClickListener(new View.OnClickListener() {
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
                    boolean success = false;

                    String idPref = CopShopHub.getUserSessionService().getUserID();
                    if (!(idPref == null)) {
                        success = CopShopHub.getAccountService().updateSellerAccount(idPref, sellerAccount);
                    }
                    if (success) {
                        CopShopHub.getUserSessionService().setUserEmail(sellerAccount.getEmail());

                        errorMsg.setText("Account info updated successfully");
                    }
                    else {
                        errorMsg.setText("Account info could not be updated");
                    }

                    // Make sure all form fields are set back to black on success
                    findViewById(R.id.editTextOrganizationName).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextStreetAddress).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextPostalCode).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextProvince).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextEmail).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.editTextPassword).setBackgroundResource(R.drawable.txt_field_black_border);

                    //go to Listings page
                    //Intent intent = new Intent(ViewSellerAccountActivity.this, ListingListActivity.class);
                    //startActivity(intent); //goes to listing activity

                    populateAccountInfo();

                    // View mode 1
                    setFieldFocusability(false);
                    setFieldBorder(false);
                    editSellerAccount.setVisibility( View.VISIBLE );
                    saveSellerAccount.setVisibility( View.GONE );
                    cancelSaveSellerAccount.setVisibility( View.GONE );
                } else {

                    // Check first name
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

        protected void setFieldFocusability(boolean focusable) {
            findViewById(R.id.editTextOrganizationName).setFocusable(focusable);
            findViewById(R.id.editTextStreetAddress).setFocusable(focusable);
            findViewById(R.id.editTextPostalCode).setFocusable(focusable);
            findViewById(R.id.editTextProvince).setFocusable(focusable);
            findViewById(R.id.editTextEmail).setFocusable(focusable);
            findViewById(R.id.editTextPassword).setFocusable(focusable);

            findViewById(R.id.editTextOrganizationName).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextStreetAddress).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextPostalCode).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextProvince).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextEmail).setFocusableInTouchMode(focusable);
            findViewById(R.id.editTextPassword).setFocusableInTouchMode(focusable);

        }

        protected void setFieldBorder(boolean wantBorder) {
            if (wantBorder) {
                findViewById(R.id.editTextOrganizationName).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextStreetAddress).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextPostalCode).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextProvince).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextEmail).setBackgroundResource(R.drawable.txt_field_black_border);
                findViewById(R.id.editTextPassword).setBackgroundResource(R.drawable.txt_field_black_border);
            }
            else {
                findViewById(R.id.editTextOrganizationName).setBackgroundResource(0);
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
            //TextView greeting = (TextView) findViewById(R.id.editTextOrganizationName);
            TextView editTextOrganizationName = findViewById(R.id.editTextOrganizationName);
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

                if (account instanceof SellerAccountObject) {
                    SellerAccountObject seller = (SellerAccountObject)account;

                    if (seller == null) {
                        errorMsg.setText("No account found");
                        findViewById(R.id.btnEditSellerAccount).setVisibility( View.GONE );
                    }
                    else {
                        if (editTextOrganizationName != null) {
                            editTextOrganizationName.setText(seller.getOrganizationName());
                        }
                        if (editTextStreetAddress != null) {
                            editTextStreetAddress.setText(seller.getStreetAddress());
                        }
                        if (editTextPostalCode != null) {
                            editTextPostalCode.setText(seller.getPostalCode());
                        }
                        if (editTextProvince != null) {
                            editTextProvince.setText(seller.getProvince());
                        }
                        if (editTextEmail != null) {
                            editTextEmail.setText(seller.getEmail());
                        }
                        if (editTextPassword != null) {
                            editTextPassword.setText(seller.getPassword());
                        }
                    }
                }
                else {
                    errorMsg.setText("No account found");
                    findViewById(R.id.btnEditSellerAccount).setVisibility( View.GONE );
                }
            }
        }

    }

