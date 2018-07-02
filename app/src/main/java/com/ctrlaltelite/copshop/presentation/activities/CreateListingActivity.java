package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.presentation.classes.DatePickerFragment;
import com.ctrlaltelite.copshop.presentation.classes.TimePickerFragment;

public class CreateListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Triggers date and time picker for Start date
        final Button startDateButton = findViewById(R.id.btnStartDate);
        startDateButton.setOnClickListener(new OnClickListener() {
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
        startTimeButton.setOnClickListener(new OnClickListener() {
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
        endDateButton.setOnClickListener(new OnClickListener() {
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
        endTimeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment endTimeFragment = new TimePickerFragment();
                Bundle params = new Bundle();
                params.putInt("id", 2);
                endTimeFragment.setArguments(params);
                endTimeFragment.show(getSupportFragmentManager(), "endTimePicker");
            }
        });

        Button submitButton = findViewById(R.id.btnCreateListing);
        submitButton.setOnClickListener(new OnClickListener() {
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
                        "0" // Currently logged in seller's id, change when differentiated login is implemented
                );

                ListingFormValidationObject validationObject = CopShopHub.getCreateListingService().validate(listingObject);

                // Check validation object to see if all fields are valid
                // If valid: store form data in listing database
                // Else invalid: check each form field, highlighting those that are invalid in red
                if (validationObject.isAllValid()) {
                    CopShopHub.getCreateListingService().saveNewListing(listingObject);

                    // Make sure all form fields are set back to black on success
                    findViewById(R.id.txtListingTitle).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtInitialPrice).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtMinBid).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtStartDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtEndDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtCategory).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_black_border);

                    // Goto listing list page
                    startActivity(new Intent(CreateListingActivity.this, ListingListActivity.class));
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
                    if (!validationObject.getStartDateAndTimeValid()) {
                        findViewById(R.id.txtStartDate).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtStartDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check all fields relating to listing end date and time
                    if (!validationObject.getEndDateAndTimeValid()) {
                        findViewById(R.id.txtEndDate).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtEndDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check all fields relating to listing end date and time
                    if (!validationObject.getEndDateAndTimeValid()) {
                        findViewById(R.id.txtCategory).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtCategory).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check listing description
                    if (!validationObject.getDescriptionValid()) {
                        findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_black_border);
                    }
                }
            }
        });
    }
}
