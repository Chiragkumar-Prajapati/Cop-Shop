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
import com.ctrlaltelite.copshop.application.CopShopApp;
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

        setupMenu(navigationView);
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
            listingItems = CopShopHub.getListingService().fetchListingsByFilters(
                intent.getStringExtra("name"),
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
    public void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupMenu(navigationView);

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
        } else if (id == R.id.nav_buyer_account_details) {
             // Goto account details page
             startActivity(new Intent(this, ViewBuyerAccountActivity.class));
         } else if (id == R.id.nav_seller_account_details) {
             // Goto account details page
             startActivity(new Intent(this, ViewSellerAccountActivity.class));
         } else if (id == R.id.nav_filter) {
             // Goto filtering page
             startActivity(new Intent(this, FilterListingsActivity.class));
         } else if (id == R.id.nav_logout) {
             // Logout and restart activity
             CopShopHub.getUserSessionService().logoutUser();
             finish();
             startActivity(getIntent());
         }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupMenu(NavigationView navigationView) {
       setEmailDisplay(navigationView);

       if (CopShopHub.getUserSessionService().userLoggedIn()) {
           // Populate menu
           if (CopShopHub.getUserSessionService().getUserType().equals("buyer")) {
               navigationView.getMenu().clear();
               navigationView.inflateMenu(R.menu.nav_menu_logged_in_bidder);
           } else if (CopShopHub.getUserSessionService().getUserType().equals("seller")) {
               navigationView.getMenu().clear();
               navigationView.inflateMenu(R.menu.nav_menu_logged_in_seller);
           } else {
               System.out.println("Invalid user type: " + CopShopHub.getUserSessionService().getUserType());
           }
       } else {
           System.out.println("Not logged in");
       }
    }

    private void setEmailDisplay(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        TextView greeting = (TextView) header.findViewById(R.id.nav_header_greeting); // Text for user if logged in

        if (CopShopHub.getUserSessionService().userLoggedIn()) {
            String email = CopShopHub.getUserSessionService().getUserEmail();
            if (null != email) {
                greeting.setText(email);
            } else {
                greeting.setText("Please Login, Stranger.");
            }
        } else {
            greeting.setText("Please Login, Stranger.");
        }
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

            if (!outFile.exists()) {
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
