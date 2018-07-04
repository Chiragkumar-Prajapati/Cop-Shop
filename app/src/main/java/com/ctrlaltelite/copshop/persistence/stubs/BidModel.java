package com.ctrlaltelite.copshop.persistence.stubs;

import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class BidModel implements IBidModel {
    private static String TABLE_NAME = "Bids";
    private IDatabase database;

    public BidModel(IDatabase database) {
        this.database = database;
    }

    @Override
    public String createNew(BidObject newBid) {
        Hashtable<String, String> newRow = new Hashtable<>();

        newRow.put("id", newBid.getId());
        newRow.put("buyerId", newBid.getBuyerId());
        newRow.put("listingId", newBid.getListingId());
        newRow.put("bidAmt", newBid.getBidAmt());

        return this.database.insertRow(TABLE_NAME, newRow);
    }

    @Override
    public BidObject fetch(String id) {
        return new BidObject(
                id,
                this.database.fetchColumn(TABLE_NAME, id, "listingId"),
                this.database.fetchColumn(TABLE_NAME, id, "buyerId"),
                this.database.fetchColumn(TABLE_NAME, id, "bidAmt")
        );
    }

    @Override
    public boolean delete(String id) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }

        if (this.database.rowExists(TABLE_NAME, id)) {
            return (null != this.database.deleteRow(TABLE_NAME, id));
        }

        return false;
    }

    @Override
    public List<BidObject> findAllByListing(String listingId) {
        List<BidObject> bidObjects = new ArrayList<>();
        List<String> bidIds = this.database.findByColumnValue(TABLE_NAME, "listingId", listingId);

        for (int i = 0; i < bidIds.size(); i++) {
            String id = bidIds.get(i);
            BidObject bid = fetch(id);
            bidObjects.add(bid);
        }

        return bidObjects;
    }
}
