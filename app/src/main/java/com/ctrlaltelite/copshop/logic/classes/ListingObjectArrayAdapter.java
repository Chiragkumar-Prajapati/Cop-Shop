package com.ctrlaltelite.copshop.logic.classes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.logic.utilities.StringUtility;
import com.ctrlaltelite.copshop.objects.ListingObject;

import java.util.ArrayList;
import java.util.List;

public class ListingObjectArrayAdapter extends ArrayAdapter<ListingObject> {
    private Context context;
    private List<ListingObject> listingInfo;

    public ListingObjectArrayAdapter(Context context, int resource, ArrayList<ListingObject> objects) {
        super(context, resource, objects);

        this.context = context;
        this.listingInfo = objects;
    }

    // Called when rendering the list
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the listing to display
        ListingObject info = listingInfo.get(position);

        // Inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listing_row, null);

        TextView title = view.findViewById(R.id.listing_list_title);
        TextView location = view.findViewById(R.id.listing_list_location);
        TextView description = view.findViewById(R.id.listing_list_description);
        TextView price = view.findViewById(R.id.listing_list_price);
        TextView bids = view.findViewById(R.id.listing_list_bid_count);
        TextView timeLeft = view.findViewById(R.id.listing_list_time_left);
        ImageView image = view.findViewById(R.id.listing_list_image);

        // Trim and set the description and other fields
        title.setText(info.getTitle());
        description.setText(StringUtility.ellipsize(info.getDescription(), 80));
        location.setText(info.getLocation());
        price.setText("$" + info.getPrice() + ",");
        bids.setText(info.getNumBids() + " bids");
        timeLeft.setText(info.getTimeLeft() + " days"); // TODO: Calculate unit of time left

        // Get the image associated with this listing
//        int imageID = context.getResources().getIdentifier(info.getImage(), "drawable", context.getPackageName());
//        image.setImageResource(imageID);

        return view;
    }
}