package com.ctrlaltelite.copshop.presentation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.logic.CopShopApp;
import com.ctrlaltelite.copshop.logic.classes.ListingObjectArrayAdapter;
import com.ctrlaltelite.copshop.objects.ListingObject;

import java.util.ArrayList;

public class ListingListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listingView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listing_list);

        // Setup top bar on list page
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        setSupportActionBar(toolbar);

        // Setup menu drawer
        if (drawer == null) {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        }

        // Setup drawer toggle
        if (toggle == null) {
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        }
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Setup drawer slide out page
        if (navigationView == null) {
            navigationView = (NavigationView) findViewById(R.id.nav_view);
        }
        navigationView.setNavigationItemSelectedListener(this);

        // Set text for logged in user
        // TODO

        // Populate list of listings
        ArrayList<ListingObject> listingItems = CopShopApp.listingListService.fetchListings();
        ListingObjectArrayAdapter listAdapter;
        if (listingView == null) {
            listingView = (ListView) findViewById(R.id.listing_list);
        }
        listAdapter = new ListingObjectArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                listingItems);
        listingView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_listings) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_login) {

        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
