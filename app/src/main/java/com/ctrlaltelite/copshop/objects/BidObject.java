package com.ctrlaltelite.copshop.objects;

import android.support.annotation.NonNull;

public class BidObject implements Comparable<BidObject> {
    private String id;
    private String listingId;
    private String buyerId;
    private String bidAmt;

    public BidObject(String id, String listingId, String buyerId, String bidAmt)  {
        this.id = id;
        this.listingId = listingId;
        this.buyerId = buyerId;
        this.bidAmt = bidAmt;
    }

    public String getId() {
        return id;
    }

    public String getBidAmt() {
        return bidAmt;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getListingId() {
        return listingId;
    }

    @Override
    public int compareTo(@NonNull BidObject otherBid) {
        float thisBidAmt = Float.parseFloat(this.getBidAmt());
        float otherBidAmt = Float.parseFloat(otherBid.getBidAmt());
        return Float.compare(thisBidAmt, otherBidAmt)*(-1); // Reverse sort direction
    }
}
