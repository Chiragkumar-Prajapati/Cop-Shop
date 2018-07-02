package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.presentation.classes.ListingObjectArrayAdapter;
import com.ctrlaltelite.copshop.objects.ListingObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ListingListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_list);
        copyDatabaseToDevice();

        // Setup top bar on list page
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup menu drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Setup drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Setup drawer slide out page
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Populate list of listings
        Intent intent = getIntent();
        List<ListingObject> listingItems = null;

        if (intent.hasExtra("name")) {
            // Populate with filtered Listings
            listingItems = CopShopHub.getListingService().fetchListingsByFilters(intent.getStringExtra("name"),
                                                                                 intent.getStringExtra("location"),
                                                                                 intent.getStringExtra("category"),
                                                                                 intent.getStringExtra("status"));
        }
        else {
            // Populate with all Listings
            listingItems = CopShopHub.getListingService().fetchListings();
        }

        final ListView listingView = (ListView) findViewById(R.id.listing_list);
        ListingObjectArrayAdapter listAdapter = new ListingObjectArrayAdapter(this, listingItems);

        // Set clicked listeners for listings
        listingView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListingObject clickedListing = (ListingObject)listingView.getItemAtPosition(position);

                Intent intent = new Intent(ListingListActivity.this, ListingViewActivity.class);
                intent.putExtra("listingId", clickedListing.getId());

                startActivity(intent);
            }
        });

        listingView.setAdapter(listAdapter);

    }

    public void updateListingsByFilters(String name, String location, String category, String status) {
        // Populate list of listings
        List<ListingObject> listingItems = CopShopHub.getListingService().fetchListingsByFilters(name, location, category, status);
        final ListView listingView = (ListView) findViewById(R.id.listing_list);

        ListingObjectArrayAdapter listAdapter;
        listAdapter = new ListingObjectArrayAdapter(this, listingItems);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // For accessing user info
        SharedPreferences sharedPreferences = getSharedPreferences("currentUser", 0);

        // Text for user if logged in
        TextView greeting = (TextView) findViewById(R.id.nav_header_greeting);
        if (null != greeting) {
            if ((sharedPreferences.getString("email", "-1")).equals("-1")) {
                // SharedPreferences returns defValue if nothing there
                // Nothing there if user not logged in
                greeting.setText("Please Login, Stranger.");
            } else {
                String loggedInEmail = sharedPreferences.getString("email", "-1");
                greeting.setText(loggedInEmail);
            }
        }

        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.listing_list, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_new_listing) {
            // Goto create new listing page
            startActivity(new Intent(this, CreateListingActivity.class));
        } else if (id == R.id.nav_login) {
            // Goto login page
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_filter) {
             // Goto filtering page
             startActivity(new Intent(this, FilterListingsActivity.class));
         }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);

            CopShopHub.setDBPath(dataDirectory.toString() + "/" + CopShopHub.getDBPath());

        } catch (final IOException ioe) {
            System.out.println("Unable to access application data: " + ioe.getMessage());
        }
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (true || !outFile.exists()) { // Remove true to persist DB between project builds in dev (or break everything - most likely)
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }

}
