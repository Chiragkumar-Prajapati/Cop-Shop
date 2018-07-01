package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class EditListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final String listingId = getIntent().getStringExtra("LISTING_ID");

        populateListingInfo(listingId);

        // Start Now button
        Button startNowButton = findViewById(R.id.btnStartNow);
        startNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Set the auction start time to the current time.
            }
        });

        // End Now button
        Button endNowButton = findViewById(R.id.btnEndNow);
        endNowButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Set the auction end time to the current time.
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
                        ((EditText) findViewById(R.id.txtStartTime)).getText().toString(),
                        ((EditText) findViewById(R.id.txtEndTime)).getText().toString(),
                        ((EditText) findViewById(R.id.txtStartTime)).getText().toString(),
                        ((EditText) findViewById(R.id.txtEndTime)).getText().toString(),
                        "0" // Currently logged in seller's id, change when differentiated login is implemented
                );

                ListingFormValidationObject validationObject = CopShopHub.getCreateListingService().validate(listingObject);

                // Check validation object to see if all fields are valid
                // If valid: store form data in listing database
                // Else invalid: check each form field, highlighting those that are invalid in red
                if (validationObject.isAllValid()) {
                    CopShopHub.getListingService().updateListing(listingId, listingObject);

                    // Make sure all form fields are set back to black on success
                    findViewById(R.id.txtStartTime).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtListingTitle).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtInitialPrice).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtMinBid).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtEndTime).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_black_border);

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
                    if (!validationObject.getStartDateAndTimeValid()) {
                        findViewById(R.id.txtStartTime).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtStartTime).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check all fields relating to listing end date and time
                    if (!validationObject.getEndDateAndTimeValid()) {
                        findViewById(R.id.txtEndTime).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtEndTime).setBackgroundResource(R.drawable.txt_field_black_border);
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


    protected void populateListingInfo(String listingId) {

        // Text for user if logged in
        TextView editTextListingTitle = findViewById(R.id.txtListingTitle);
        TextView editTextInitialPrice = findViewById(R.id.txtInitialPrice);
        TextView editTextMinBid = findViewById(R.id.txtMinBid);
        TextView editTextStartTime = findViewById(R.id.txtStartTime);
        TextView editTextEndTime = findViewById(R.id.txtEndTime);
        TextView editTextAreaDescription = findViewById(R.id.txtAreaDescription);

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
            if (editTextStartTime != null) {
                editTextStartTime.setText(listingObj.getAuctionStartTime());
            }
            if (editTextEndTime != null) {
                editTextEndTime.setText(listingObj.getAuctionEndTime());
            }
            if (editTextAreaDescription != null) {
                editTextAreaDescription.setText(listingObj.getDescription());
            }
        }
    }

}
