package com.ctrlaltelite.copshop.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.logic.CopShopApp;
import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.logic.services.stubs.CreateListingService;
import com.ctrlaltelite.copshop.objects.ListingObject;

public class CreateListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submitButton = (Button) findViewById(R.id.btnCreateListing);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SubmitButton", "Clicked!");
                //create

                ListingObject listingObject = new ListingObject(
                        "ignored",
                        ((EditText) findViewById(R.id.txtListingTitle)).getText().toString(),
                        ((EditText) findViewById(R.id.txtAreaDescription)).getText().toString(),
                        ((EditText) findViewById(R.id.txtInitialPrice)).getText().toString(),
                        ((EditText) findViewById(R.id.txtMinBid)).getText().toString(),
                        ((EditText) findViewById(R.id.txtStartDay)).getText().toString() + "/" + ((EditText) findViewById(R.id.txtStartMonth)).getText().toString() + "/" + ((EditText) findViewById(R.id.txtStartYear)).getText().toString(),
                        ((EditText) findViewById(R.id.txtStartTimeHour)).getText().toString() + ":" + ((EditText) findViewById(R.id.txtStartTimeMinute)).getText().toString(),
                        ((EditText) findViewById(R.id.txtEndDay)).getText().toString() + "/" +  ((EditText) findViewById(R.id.txtEndMonth)).getText().toString() + "/" + ((EditText) findViewById(R.id.txtEndYear)).getText().toString(),
                        ((EditText) findViewById(R.id.txtEndTimeHour)).getText().toString() + ":" + ((EditText) findViewById(R.id.txtEndTimeMinute)).getText().toString(),
                        "1" // TODO: currently logged in seller's id
                );

                ListingFormValidationObject validationObject = CopShopApp.createListingService.create(listingObject);

                //TODO: CheckForValidationErrors
                // if validationObject.isAllValid() != true, set appropriate fields to have red border,
                // indicating to the user that input is invalid and needs to be fixed

                //System.out.println(listingObject.toString());

            }
        });
    }

}
