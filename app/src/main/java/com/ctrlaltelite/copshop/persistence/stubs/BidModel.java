package com.ctrlaltelite.copshop.persistence.stubs;

import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;

import java.util.ArrayList;
import java.util.List;

public class BidModel implements IBidModel {
    private static String TABLE_NAME = "Bids";
    private IDatabase database;

    public BidModel(IDatabase database) {
        this.database = database;
    }

    @Override
    public String createNew(BidObject bid) {
        return "PLACEHOLDER";
    }

    @Override
    public boolean delete(String id) {
        return true;
    }

    @Override
    public List findByListing(String listingId) {
        return new ArrayList();
    }
}
