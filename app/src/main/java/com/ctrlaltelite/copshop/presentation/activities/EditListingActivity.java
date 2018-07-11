package com.ctrlaltelite.copshop.presentation.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.presentation.classes.DatePickerFragment;
import com.ctrlaltelite.copshop.presentation.classes.TimePickerFragment;
import com.ctrlaltelite.copshop.presentation.utilities.ImageUtility;

import java.io.File;

public class EditListingActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    String imageData[];
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private Uri pictureUri;
    private int reqOrientationShift = 0;
    private static boolean startDateChanged = false;
    private static boolean endDateChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_edit_listing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String listingId = getIntent().getStringExtra("LISTING_ID");

        populateListingInfo(listingId);

        startDateChanged = false;
        endDateChanged = false;

        // Triggers date and time picker for Start date
        final Button startDateButton = findViewById(R.id.btnStartDate);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment startDateFragment = new DatePickerFragment();
                Bundle params = new Bundle();
                params.putInt("id", 1);
                startDateFragment.setArguments(params);
                startDateFragment.show(getSupportFragmentManager(), "startDatePicker");
            }
        });

        Button startTimeButton = findViewById(R.id.btnStartTime);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment startTimeFragment = new TimePickerFragment();
                Bundle params = new Bundle();
                params.putInt("id", 1);
                startTimeFragment.setArguments(params);
                startTimeFragment.show(getSupportFragmentManager(), "startTimePicker");
            }
        });

        // Triggers date and time picker for End date
        final Button endDateButton = findViewById(R.id.btnEndDate);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment endDateFragment = new DatePickerFragment();
                Bundle params = new Bundle();
                params.putInt("id", 2);
                endDateFragment.setArguments(params);
                endDateFragment.show(getSupportFragmentManager(), "endDatePicker");
            }
        });

        Button endTimeButton = findViewById(R.id.btnEndTime);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment endTimeFragment = new TimePickerFragment();
                Bundle params = new Bundle();
                params.putInt("id", 2);
                endTimeFragment.setArguments(params);
                endTimeFragment.show(getSupportFragmentManager(), "endTimePicker");
            }
        });

        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if an image is present
                if (ImageUtility.hasImage(imageView)) {
                    Bitmap bm = ImageUtility.uriToBitmap(EditListingActivity.this, pictureUri);
                    if (bm != null) {
                        reqOrientationShift++;
                        if (reqOrientationShift == 4) {
                            reqOrientationShift = 0;
                        }
                        bm = ImageUtility.rotateBitmap(bm, reqOrientationShift);
                        imageView.setImageBitmap(bm);
                        imageView.setTag(reqOrientationShift + " " + pictureUri.toString());
                    }
                }
            }
        });

        Button imgChangeButton = findViewById(R.id.btnChangeImage);
        imgChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditListingActivity.this);
                alertDialog.setCancelable(true);
                alertDialog.setTitle("Image Browser");
                alertDialog.setMessage("Please choose where to get image.");

                alertDialog.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (ContextCompat.checkSelfPermission(EditListingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            String pictureName = ImageUtility.getPictureName();
                            File imageFile = new File(pictureDirectory, pictureName);
                            pictureUri = Uri.fromFile(imageFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

                            if (intent.resolveActivity(getPackageManager()) != null) {
                                // Start the image capture intent to take photo
                                startActivityForResult(intent, 0);
                            }
                        } else {
                            ActivityCompat.requestPermissions(EditListingActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    }
                });

                alertDialog.setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

                    }
                });
                alertDialog.show();
            }
        });

        // Cancel button
        Button cancelSaveButton = findViewById(R.id.btnCancelSaveListing);
        cancelSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditListingActivity.this, ListingViewActivity.class);
                intent.putExtra("listingId", listingId);
                startActivity(intent);
            }
        });


        // Delete Listing button
        Button deleteButton = findViewById(R.id.btnDeleteListing);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CopShopHub.getListingService().deleteListing(listingId)) {
                    Intent intent = new Intent(EditListingActivity.this, ListingListActivity.class);
                    startActivity(intent);
                }
                // else, something is horribly wrong.
            }
        });


        // Save button
        Button submitButton = findViewById(R.id.btnSaveListing);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SubmitButton", "Clicked!");
                // Create
                ImageView imageView = findViewById(R.id.imageView);
                String imagePath = "";
                if (ImageUtility.hasImage(imageView)) {
                    imagePath = imageView.getTag().toString();
                }

                ListingObject listingObject = new ListingObject(
                        "ignored",
                        ((EditText) findViewById(R.id.txtListingTitle)).getText().toString(),
                        ((EditText) findViewById(R.id.txtAreaDescription)).getText().toString(),
                        ((EditText) findViewById(R.id.txtInitialPrice)).getText().toString(),
                        ((EditText) findViewById(R.id.txtMinBid)).getText().toString(),
                        ((TextView) findViewById(R.id.txtStartDate)).getText().toString(),
                        ((TextView) findViewById(R.id.txtEndDate)).getText().toString(),
                        ((TextView) findViewById(R.id.txtCategory)).getText().toString(),
                        imagePath,
                        CopShopHub.getUserSessionService().getUserID()
                );

                ListingFormValidationObject validationObject = CopShopHub.getCreateListingService().validate(listingObject);

                // Check validation object to see if all fields are valid
                // (In the case of start and end date, we only care IF they were changed)
                // If valid: update form data in listing database
                // Else invalid: check each form field, highlighting those that are invalid in red
                if (validationObject.getTitleValid() &&
                                validationObject.getDescriptionValid() &&
                                validationObject.getInitPriceValid() &&
                                validationObject.getMinBidValid() &&
                                validationObject.getCategoryValid() &&
                                (validationObject.getStartDateAndTimeValid() || !startDateChanged) &&
                                (validationObject.getEndDateAndTimeValid() || !endDateChanged)) {

                    CopShopHub.getListingService().updateListing(listingId, listingObject);

                    // Make sure all form fields are set back to black on success
                    findViewById(R.id.txtStartDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtListingTitle).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtInitialPrice).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtMinBid).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtEndDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_black_border);
                    findViewById(R.id.txtCategory).setBackgroundResource(R.drawable.txt_field_black_border);

                    // Goto listing list page
                    startActivity(new Intent(EditListingActivity.this, ListingListActivity.class));
                } else {

                    // Check listing title
                    if (!validationObject.getTitleValid()) {
                        findViewById(R.id.txtListingTitle).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtListingTitle).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check listing initial price
                    if (!validationObject.getInitPriceValid()) {
                        findViewById(R.id.txtInitialPrice).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtInitialPrice).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check listing minimum bet increase amount
                    if (!validationObject.getMinBidValid()) {
                        findViewById(R.id.txtMinBid).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtMinBid).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check all fields relating to listing start date and time
                    if (!validationObject.getStartDateAndTimeValid() && startDateChanged) {
                        findViewById(R.id.txtStartDate).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtStartDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check all fields relating to listing end date and time
                    if (!validationObject.getEndDateAndTimeValid() && endDateChanged) {
                        findViewById(R.id.txtEndDate).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtEndDate).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check listing description
                    if (!validationObject.getDescriptionValid()) {
                        findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtAreaDescription).setBackgroundResource(R.drawable.txt_field_black_border);
                    }

                    // Check category
                    if (!validationObject.getCategoryValid()) {
                        findViewById(R.id.txtCategory).setBackgroundResource(R.drawable.txt_field_red_border);
                    } else {
                        findViewById(R.id.txtCategory).setBackgroundResource(R.drawable.txt_field_black_border);
                    }
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView imageView = findViewById(R.id.imageView);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    reqOrientationShift = 0;
                    imageView.setImageURI(pictureUri);
                    imageView.setTag(reqOrientationShift + " " + pictureUri.toString());
                }

                break;

            case 1:
                if (resultCode == RESULT_OK) {
                    if (ContextCompat.checkSelfPermission(EditListingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        reqOrientationShift = 0;
                        pictureUri = imageReturnedIntent.getData();
                        imageView.setImageURI(pictureUri);
                        imageView.setTag(reqOrientationShift + " " + pictureUri.toString());
                    } else {
                        ActivityCompat.requestPermissions(EditListingActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    String pictureName = ImageUtility.getPictureName();
                    File imageFile = new File(pictureDirectory, pictureName);
                    pictureUri = Uri.fromFile(imageFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Start the image capture intent to take photo
                        startActivityForResult(intent, 0);
                    }
                } else {
                    // permission denied,
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditListingActivity.this);
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
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageView image = findViewById(R.id.imageView);
                    reqOrientationShift = Integer.parseInt(imageData[0]);
                    pictureUri = Uri.parse(imageData[1]);
                    Bitmap bm = ImageUtility.uriToBitmap(EditListingActivity.this, pictureUri);
                    bm = ImageUtility.rotateBitmap(bm, reqOrientationShift);
                    image.setImageBitmap(bm);
                    image.setTag(reqOrientationShift + " " + pictureUri.toString());
                } else {
                    // permission denied,
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditListingActivity.this);
                    alertDialog.setCancelable(true);
                    alertDialog.setTitle("Permission Denied");
                    alertDialog.setMessage("Unable to display image for editing. Please allow CopShop Storage access via device settings.");
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

    protected void populateListingInfo(String listingId) {

        // Text for user if logged in
        TextView editTextListingTitle = findViewById(R.id.txtListingTitle);
        TextView editTextInitialPrice = findViewById(R.id.txtInitialPrice);
        TextView editTextMinBid = findViewById(R.id.txtMinBid);
        TextView editTextStartDate = findViewById(R.id.txtStartDate);
        TextView editTextEndDate = findViewById(R.id.txtEndDate);
        TextView editTextAreaDescription = findViewById(R.id.txtAreaDescription);
        TextView editTextCategory = findViewById(R.id.txtCategory);
        ImageView editImageView = findViewById(R.id.imageView);

        ListingObject listingObj = CopShopHub.getListingService().fetchListing(listingId);

        // Never should be null, but check anyway
        if (listingObj != null) {
            if (editTextListingTitle != null) {
                editTextListingTitle.setText(listingObj.getTitle());
            }
            if (editTextInitialPrice != null) {
                editTextInitialPrice.setText(listingObj.getInitPrice());
            }
            if (editTextMinBid != null) {
                editTextMinBid.setText(listingObj.getMinBid());
            }
            if (editTextStartDate != null) {
                editTextStartDate.setText(listingObj.getAuctionStartDate());
            }
            if (editTextEndDate != null) {
                editTextEndDate.setText(listingObj.getAuctionEndDate());
            }
            if (editTextAreaDescription != null) {
                editTextAreaDescription.setText(listingObj.getDescription());
            }
            if (editTextCategory != null) {
                editTextCategory.setText(listingObj.getCategory());
            }
            if (editImageView != null) {
                if (!listingObj.getImageData().isEmpty() && listingObj.getImageData() != "") {
                    imageData = ImageUtility.imageDecode(listingObj.getImageData());
                    if (ContextCompat.checkSelfPermission(EditListingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        reqOrientationShift = Integer.parseInt(imageData[0]);
                        pictureUri = Uri.parse(imageData[1]);
                        Bitmap bm = ImageUtility.uriToBitmap(EditListingActivity.this, pictureUri);
                        bm = ImageUtility.rotateBitmap(bm, reqOrientationShift);
                        editImageView.setImageBitmap(bm);
                        editImageView.setTag(reqOrientationShift + " " + pictureUri.toString());
                    } else {
                        ActivityCompat.requestPermissions(EditListingActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }
            }
        }
    }

    public static void touchStartDate() {
        startDateChanged = true;
    }
    public static void touchEndDate() {
        endDateChanged = true;
    }
}
