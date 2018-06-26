package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.ctrlaltelite.copshop.presentation.classes.ListingObjectArrayAdapter;
import com.ctrlaltelite.copshop.objects.ListingObject;
import java.util.List;

public class ListingListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_list);

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

        // Populate list of listings
        List<ListingObject> listingItems = CopShopApp.listingService.fetchListings();
        final ListView listingView = (ListView) findViewById(R.id.listing_list);

        ListingObjectArrayAdapter listAdapter;
        listAdapter = new ListingObjectArrayAdapter(this, listingItems);

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
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
