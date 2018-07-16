package com.ctrlaltelite.copshop.presentation.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.objects.ListingObject;

import java.util.List;
import java.util.Locale;

public class BidObjectArrayAdapter extends ArrayAdapter<BidObject> {
    private Context context;
    private List<BidObject> bidInfo;

    public BidObjectArrayAdapter(Context context, List<BidObject> objects) {
        super(context, 0, objects);
        this.context = context;
        this.bidInfo = objects;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the bid info to display
        BidObject info = bidInfo.get(position);

        // Inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bid_row, null);

        TextView amount = (TextView) view.findViewById(R.id.listing_view_bid_amount);
        TextView buyer = (TextView) view.findViewById(R.id.listing_view_bid_buyer);

        // Get buyer name
        String userId = info.getBuyerId();
        String buyerName = CopShopHub.getAccountService().getBuyerName(userId);

        // Set fields
        ListingObject listing = CopShopHub.getListingService().fetchListing(info.getListingId());
        Float bidAmount = Float.parseFloat(info.getBidAmt());

        amount.setText(String.format(Locale.CANADA, "$%1$.2f", bidAmount));
        buyer.setText(buyerName);

        if (CopShopHub.getUserSessionService().userLoggedIn()){
            if (CopShopHub.getUserSessionService().loggedInUserIsBuyer() && userId.equals(CopShopHub.getUserSessionService().getUserID())) {
                buyer.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }

        return view;
    }
}