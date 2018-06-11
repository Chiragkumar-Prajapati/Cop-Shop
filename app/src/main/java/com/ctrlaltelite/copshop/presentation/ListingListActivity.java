package com.ctrlaltelite.copshop.presentation;

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
import android.widget.ListView;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.logic.CopShopApp;
import com.ctrlaltelite.copshop.logic.classes.ListingObjectArrayAdapter;
import com.ctrlaltelite.copshop.objects.ListingObject;
import java.util.ArrayList;

public class ListingListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_list);
        //for accessing user info
        SharedPreferences sharedPreferences = getSharedPreferences("currentUser", 0);

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

        //text for user if logged in
        TextView greeting = (TextView) findViewById(R.id.textHello);
        if((sharedPreferences.getString("username", "-1")).equals("-1")) {
            //sharedPreferences returns defValue if nothing there
            //nothing there if user not logged in
            greeting.setText("Please Login, Stranger.");
        }
        else {
            String name = sharedPreferences.getString("username", "-1");
            greeting.setText("Hello, "+name+"!");
        }

        // Populate list of listings
        ArrayList<ListingObject> listingItems = CopShopApp.listingListService.fetchListings();
        ListingObjectArrayAdapter listAdapter;
        ListView listingView = (ListView) findViewById(R.id.listing_list);

        listAdapter = new ListingObjectArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                listingItems);
        listingView.setAdapter(listAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listing_list, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_listings) {
            // Goto listing list page
            startActivity(new Intent(this, ListingListActivity.class));
        } else if (id == R.id.nav_account) {
            // TODO: Goto view account page
        } else if (id == R.id.nav_new_listing) {
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
