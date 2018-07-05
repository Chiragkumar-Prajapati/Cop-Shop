package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.logic.services.stubs.ListingService;

import java.util.List;

public class FilterListingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_listings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Category spinner(drop-down list)*/
        // get array containing all locations
        List<String> categoriesList = CopShopHub.getListingModel().getAllCategories();
        String[] categories = categoriesList.toArray(new String[categoriesList.size()]);
        // add locations to location spinner
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        // Specify the layout to use when the list of choices appears
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(categoryAdapter);

        /*Location spinner(drop-down list)*/
        // get array containing all locations
        List<String> locationsList = CopShopHub.getSellerModel().getAllSellerNames();
        String[] locations = locationsList.toArray(new String[locationsList.size()]);
        // add locations to location spinner
        Spinner locnSpinner = (Spinner) findViewById(R.id.locations_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> locnAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        // Specify the layout to use when the list of choices appears
        locnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        locnSpinner.setAdapter(locnAdapter);

        /*Status spinner(drop-down list)*/
        Spinner statusSpinner = (Spinner) findViewById(R.id.status_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        statusSpinner.setAdapter(statusAdapter);

        // Handle search button click
        Button searchButton = (Button) findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Update listings and goto ListingList page
                Intent intent = new Intent(FilterListingsActivity.this, ListingListActivity.class);
                intent.putExtra("name", ((EditText) findViewById(R.id.txtFilterName)).getText().toString());
                intent.putExtra("location", ((Spinner) findViewById(R.id.locations_spinner)).getSelectedItem().toString());
                intent.putExtra("category", ((Spinner) findViewById(R.id.category_spinner)).getSelectedItem().toString());
                intent.putExtra("status", ((Spinner) findViewById(R.id.status_spinner)).getSelectedItem().toString());
                startActivity(intent);
            }
        });

        // Handle clear button click
        Button clearButton = (Button) findViewById(R.id.btnCancel);
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Goto ListingList page
                Intent intent = new Intent(FilterListingsActivity.this, ListingListActivity.class);
                startActivity(intent);
            }
        });
    }

}
