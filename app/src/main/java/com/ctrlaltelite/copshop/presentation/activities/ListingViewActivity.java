package com.ctrlaltelite.copshop.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.presentation.classes.BidObjectArrayAdapter;
import java.util.List;

public class ListingViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get listing info
        Bundle extras = getIntent().getExtras();
        final String listingId;

        if (null != extras) {
            listingId = extras.getString("listingId");
        } else {
            listingId = null;
        }

        final ListingObject listing;
        if (null != listingId) {
            listing = CopShopHub.getListingService().fetchListing(listingId);
        } else {
            listing = null;
        }

        // Render all information on page
        if (null != listing) {
            final TextView title = (TextView) findViewById(R.id.view_listing_title);
            final TextView postedBy = (TextView) findViewById(R.id.view_listing_posted_by);
            final TextView description = (TextView) findViewById(R.id.view_listing_description);
            final Button bidButton = (Button) findViewById(R.id.view_listing_bid_button);
            final TextView bidInput = (TextView) findViewById(R.id.view_listing_bid_input);
            final ListView bidList = (ListView) findViewById(R.id.view_listing_bid_list);
            final TextView minBid = (TextView) findViewById(R.id.view_listing_min_bid);
            final TextView timeLeftDaysHours = (TextView) findViewById(R.id.view_listing_ending_in_days_hours);
            final TextView timeLeftTimeDate = (TextView) findViewById(R.id.view_listing_ending_in_time_date);
            final TextView timeLeftLabel = (TextView) findViewById(R.id.view_listing_ending_in_label);
            final ImageView image = (ImageView) findViewById(R.id.view_listing_image);
            final Button editButton = (Button) findViewById(R.id.view_listing_edit_button);

            // Set title, desc, and postedBy
            title.setText(listing.getTitle());
            description.setText(listing.getDescription());
            postedBy.setText(CopShopHub.getListingService().getSellerName(listing.getSellerId()));

            // Fetch all bids
            List<BidObject> bidObjects = CopShopHub.getBidService().fetchBidsForListing(listingId);
            BidObjectArrayAdapter bidAdapter;
            bidAdapter = new BidObjectArrayAdapter(this, bidObjects);
            bidList.setAdapter(bidAdapter);

            // Set next bid total
            String suggestedBid = CopShopHub.getListingService().getNextBidTotal(listing);
            bidInput.setHint(suggestedBid);
            bidInput.setText(bidInput.getHint());
            minBid.setText("$" + suggestedBid);

            // Attach listener to implicit field submit?

            // Attach listener to button
            bidButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    final String suggestedBid = CopShopHub.getListingService().getNextBidTotal(listing);
                    boolean success = CopShopHub.getBidService().createBid(suggestedBid, listingId, bidInput.getText().toString(), "0"); // TODO: replace placeholder buyer ID
                    if (success) {
                        // Refresh activity to get updated data (Replace this with dynamically adjusting page instead?)
                        finish();
                        startActivity(getIntent());
                    } else {
                        bidInput.setTextColor(Color.RED);
                    }
                }
            });

            // Hide the auction timer until auction stuff is complete and date-time processing is ready
            timeLeftDaysHours.setVisibility(View.INVISIBLE);
            timeLeftTimeDate.setVisibility(View.INVISIBLE);
            timeLeftLabel.setVisibility(View.INVISIBLE);

            // Edit button
            final String theListingId = listingId;
            boolean thisIsSeller = false;
            String email = CopShopHub.getUserSessionService().getUserEmail();
            if (!(email == null)) {
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
