package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.presentation.classes.DatePickerFragment;
import com.ctrlaltelite.copshop.presentation.classes.TimePickerFragment;

public class EditListingActivity extends AppCompatActivity {

    private static boolean startDateChanged = false;
    private static boolean endDateChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_edit_listing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String listingId = getIntent().getStringExtra("LISTING_ID");

        populateListingInfo(listingId);

        startDateChanged = false;
        endDateChanged = false;

        // Triggers date and time picker for Start date
        final Button startDateButton = findViewById(R.id.btnStartDate);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment startDateFragment = new DatePickerFragment();
                Bundle params = new Bundle();
                params.putInt("id", 1);
                startDateFragment.setArguments(params);
                startDateFragment.show(getSupportFragmentManager(), "startDatePicker");
            }
        });

        Button startTimeButton = findViewById(R.id.btnStartTime);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment startTimeFragment = new TimePickerFragment();
                Bundle params = new Bundle();
                params.putInt("id", 1);
                startTimeFragment.setArguments(params);
                startTimeFragment.show(getSupportFragmentManager(), "startTimePicker");
            }
        });

        // Triggers date and time picker for End date
        final Button endDateButton = findViewById(R.id.btnEndDate);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment endDateFragment = new DatePickerFragment();
                Bundle params = new Bundle();
                params.putInt("id", 2);
                endDateFragment.setArguments(params);
                endDateFragment.show(getSupportFragmentManager(), "endDatePicker");
            }
        });

        Button endTimeButton = findViewById(R.id.btnEndTime);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment endTimeFragment = new TimePickerFragment();
                Bundle params = new Bundle();
                params.putInt("id", 2);
                endTimeFragment.setArguments(params);
                endTimeFragment.show(getSupportFragmentManager(), "endTimePicker");
            }
        });

        // Cancel button
        Button cancelSaveButton = findViewById(R.id.btnCancelSaveListing);
        cancelSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditListingActivity.this, ListingViewActivity.class);
                intent.putExtra("listingId", listingId);
                startActivity(intent);
            }
        });


        // Delete Listing button
        Button deleteButton = findViewById(R.id.btnDeleteListing);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CopShopHub.getListingService().deleteListing(listingId)) {
                    Intent intent = new Intent(EditListingActivity.this, ListingListActivity.class);
                    startActivity(intent);
                }
                // else, something is horribly wrong.
            }
        });


        // Save button
        Button submitButton = findViewById(R.id.btnSaveListing);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SubmitButton", "Clicked!");
                // Create

                ListingObject listingObject = new ListingObject(
                        "ignored",
                        ((EditText) findViewById(R.id.txtListingTitle)).getText().toString(),
                        ((EditText) findViewById(R.id.txtAreaDescription)).getText().toString(),
                        ((EditText) findViewById(R.id.txtInitialPrice)).getText().toString(),
                        ((EditText) findViewById(R.id.txtMinBid)).getText().toString(),
                        ((TextView) findViewById(R.id.txtStartDate)).getText().toString(),
                        ((TextView) findViewById(R.id.txtEndDate)).getText().toString(),
                        ((TextView) findViewById(R.id.txtCategory)).getText().toString(),
                        CopShopHub.getUserSessionService(context).getUserID()
                );

                ListingFormValidationObject validationObject = CopShopHub.getCreateListingService().validate(listingObject);

                // Check validation object to see if all fields are valid
                // (In the case of start and end date, we only care IF they were changed)
                // If valid: update form data in listing database
                // Else invalid: check each form field, highlighting those that are invalid in red
                if (validationObject.getTitleValid() &&
                                validationObject.getDescriptionValid() &&
                                validationObject.getInitPriceValid() &&
                                validationObject.getMinBidValid() &&
                                validationObject.getCategoryValid() &&
                                (validationObject.getStartDateAndTimeValid() || !startDateChanged) &&
                                (validationObject.getEndDateAndTimeValid() || !endDateChanged)) {

                    CopShopHub.getListingService().updateListing(listingId, listingObject);

                    // Make sure all form fields are set back to black on success
                    findViewById(R.id.txtStartDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtListingTitle).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtInitialPrice).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtMinBid).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtEndDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtCategory).setBackgroundResource(R.drawable.txt_field_black_border);

                    // Goto listing list page
                    startActivity(new Intent(EditListingActivity.this, ListingListActivity.class));
                } else {

                    // Check listing title
                    if (!validationObject.getTitleValid()) {
                        findViewById(R.id.txtListingTitle).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtListingTitle).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check listing initial price
                    if (!validationObject.getInitPriceValid()) {
                        findViewById(R.id.txtInitialPrice).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtInitialPrice).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check listing minimum bet increase amount
                    if (!validationObject.getMinBidValid()) {
                        findViewById(R.id.txtMinBid).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtMinBid).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check all fields relating to listing start date and time
                    if (!validationObject.getStartDateAndTimeValid() && startDateChanged) {
                        findViewById(R.id.txtStartDate).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtStartDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check all fields relating to listing end date and time
                    if (!validationObject.getEndDateAndTimeValid() && endDateChanged) {
                        findViewById(R.id.txtEndDate).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtEndDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check listing description
                    if (!validationObject.getDescriptionValid()) {
                        findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check category
                    if (!validationObject.getCategoryValid()) {
                        findViewById(R.id.txtCategory).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtCategory).setBackgroundResource(R.drawable.txt_field_black_border);
                    }
                }
            }
        });
    }


    protected void populateListingInfo(String listingId) {

        // Text for user if logged in
        TextView editTextListingTitle = findViewById(R.id.txtListingTitle);
        TextView editTextInitialPrice = findViewById(R.id.txtInitialPrice);
        TextView editTextMinBid = findViewById(R.id.txtMinBid);
        TextView editTextStartDate = findViewById(R.id.txtStartDate);
        TextView editTextEndDate = findViewById(R.id.txtEndDate);
        TextView editTextAreaDescription = findViewById(R.id.txtAreaDescription);
        TextView editTextCategory = findViewById(R.id.txtCategory);

        ListingObject listingObj = CopShopHub.getListingService().fetchListing(listingId);

        // Never should be null, but check anyway
        if (listingObj != null) {
            if (editTextListingTitle != null) {
                editTextListingTitle.setText(listingObj.getTitle());
            }
            if (editTextInitialPrice != null) {
                editTextInitialPrice.setText(listingObj.getInitPrice());
            }
            if (editTextMinBid != null) {
                editTextMinBid.setText(listingObj.getMinBid());
            }
            if (editTextStartDate != null) {
                editTextStartDate.setText(listingObj.getAuctionStartDate());
            }
            if (editTextEndDate != null) {
                editTextEndDate.setText(listingObj.getAuctionEndDate());
            }
            if (editTextAreaDescription != null) {
                editTextAreaDescription.setText(listingObj.getCategory());
            }
            if (editTextCategory != null) {
                editTextCategory.setText(listingObj.getCategory());
            }
        }
    }

    public static void touchStartDate() {
        startDateChanged = true;
    }
    public static void touchEndDate() {
        endDateChanged = true;
    }
}
