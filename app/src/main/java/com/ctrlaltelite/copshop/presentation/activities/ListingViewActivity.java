package com.ctrlaltelite.copshop.presentation.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import com.ctrlaltelite.copshop.presentation.utilities.ImageUtility;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;

public class ListingViewActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    String imageData[];

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

            // Disable bid button if not buyer logged in
            if (!CopShopHub.getUserSessionService().userLoggedIn() || !CopShopHub.getUserSessionService().getUserType().equals("buyer")) {
                System.out.println("Disabled bid button");
                bidButton.setEnabled(false);
                bidButton.setBackgroundColor(Color.LTGRAY);
            }
			
			// Get the image associated with this listing
            if (!listing.getImageData().isEmpty() && listing.getImageData() != "") {
                imageData = ImageUtility.imageDecode(listing.getImageData());
                if (ContextCompat.checkSelfPermission(ListingViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    int rotationAmount = Integer.parseInt(imageData[0]);
                    Uri imageUri = Uri.parse(imageData[1]);
                    Bitmap bm = ImageUtility.uriToBitmap(ListingViewActivity.this, imageUri);
                    bm = ImageUtility.rotateBitmap(bm, rotationAmount);
                    image.setImageBitmap(bm);
                } else {
                    ActivityCompat.requestPermissions(ListingViewActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
			}

            // Hide the bid button, list, and field until bidding feature is complete
            // bidInput.setHint(CopShopHub.getViewListingService().getNextBidTotal(listing));
            bidInput.setVisibility(View.INVISIBLE);
            bidList.setVisibility(View.INVISIBLE);
            bidButton.setVisibility(View.INVISIBLE);
			// Attach listener to implicit field submit?

            // Attach listener to button
            bidButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Context context = getApplicationContext();
                    final String suggestedBid = CopShopHub.getListingService().getNextBidTotal(listing);
                    String buyerId = CopShopHub.getUserSessionService().getUserID();
                    boolean success = CopShopHub.getBidService().createBid(suggestedBid, listingId, bidInput.getText().toString(), buyerId);
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
            if (email != null) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageView image = (ImageView) findViewById(R.id.view_listing_image);
                    int rotationAmount = Integer.parseInt(imageData[0]);
                    Uri imageUri = Uri.parse(imageData[1]);
                    Bitmap bm = ImageUtility.uriToBitmap(ListingViewActivity.this, imageUri);
                    bm = ImageUtility.rotateBitmap(bm, rotationAmount);
                    image.setImageBitmap(bm);
                } else {
                    // permission denied,
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListingViewActivity.this);
                    alertDialog.setCancelable(true);
                    alertDialog.setTitle("Permission Denied");
                    alertDialog.setMessage("Unable to access feature. Please allow CopShop Write access via device settings.");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        }
    }

}
