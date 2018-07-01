package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;

public class ListingViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get listing info
        Bundle extras = getIntent().getExtras();
        String listingId = null;
        ListingObject listing = null;
        if (null != extras) {
            listingId = extras.getString("listingId");
        }
        if (null != listingId) {
            listing = CopShopHub.getListingService().fetchListing(listingId);
        }

        // Render all information on page
        if (null != listing) {
            TextView title = (TextView) findViewById(R.id.view_listing_title);
            TextView postedBy = (TextView) findViewById(R.id.view_listing_posted_by);
            TextView description = (TextView) findViewById(R.id.view_listing_description);
            Button bidButton = (Button) findViewById(R.id.view_listing_bid_button);
            TextView bidInput = (TextView) findViewById(R.id.view_listing_bid_input);
            ListView bidList = (ListView) findViewById(R.id.view_listing_bid_list);
            TextView timeLeftDaysHours = (TextView) findViewById(R.id.view_listing_ending_in_days_hours);
            TextView timeLeftTimeDate = (TextView) findViewById(R.id.view_listing_ending_in_time_date);
            TextView timeLeftLabel = (TextView) findViewById(R.id.view_listing_ending_in_label);
            ImageView image = (ImageView) findViewById(R.id.view_listing_image);
            Button editButton = (Button) findViewById(R.id.view_listing_edit_button);

            title.setText(listing.getTitle());
            description.setText(listing.getDescription());
            postedBy.setText(CopShopHub.getListingService().getSellerName(listing.getSellerId()));

            // Hide the bid button, list, and field until bidding feature is complete
            // bidInput.setHint(CopShopHub.getViewListingService().getNextBidTotal(listing));
            bidInput.setVisibility(View.INVISIBLE);
            bidList.setVisibility(View.INVISIBLE);
            bidButton.setVisibility(View.INVISIBLE);

            // Hide the auction timer until auction stuff is complete and date-time processing is ready
            timeLeftDaysHours.setVisibility(View.INVISIBLE);
            timeLeftTimeDate.setVisibility(View.INVISIBLE);
            timeLeftLabel.setVisibility(View.INVISIBLE);




            // Edit button
            final String theListingId = listingId;
            boolean thisIsSeller = false;
            SharedPreferences sharedPreferences = getSharedPreferences("currentUser", 0);
            String email = sharedPreferences.getString("email", "-1");
            if (!email.equals("-1")) {
                AccountObject account = CopShopHub.getAccountService().fetchAccountByEmail(email);
                if (account != null &&
                        account instanceof SellerAccountObject &&
                        account.getId().equals(listing.getSellerId())) {
                    thisIsSeller = true;
                }
            }
            if (thisIsSeller) {
                editButton.setVisibility(View.VISIBLE);
            }
            else {
                editButton.setVisibility(View.GONE);
            }
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(ListingViewActivity.this, EditListingActivity.class);
                    intent.putExtra("LISTING_ID", theListingId);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListingViewActivity.this, ListingListActivity.class);
        startActivity(intent);
    }
}
