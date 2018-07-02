package com.ctrlaltelite.copshop.persistence.stubs;

import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ListingModel implements IListingModel {
    private static String TABLE_NAME = "Listings";
    private IDatabase database;

    public ListingModel(IDatabase database) {
        this.database = database;
    }

    @Override
    public String createNew(ListingObject newListing) {
        if (null == newListing) { throw new IllegalArgumentException("newListing cannot be null"); }

        Hashtable<String, String> newRow = new Hashtable<>();

        newRow.put("title", newListing.getTitle());
        newRow.put("description", newListing.getDescription());
        newRow.put("initPrice", newListing.getInitPrice());
        newRow.put("minBid", newListing.getMinBid());
        newRow.put("auctionStartDate", newListing.getAuctionStartDate());
        newRow.put("auctionEndDate", newListing.getAuctionEndDate());
        newRow.put("sellerId", newListing.getSellerId());

        return this.database.insertRow(TABLE_NAME, newRow);
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
    public boolean update(String id, ListingObject updatedListing) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }
        if (null == updatedListing) { throw new IllegalArgumentException("updatedListing cannot be null"); }

        if (this.database.rowExists(TABLE_NAME, id)) {
            boolean success = true;

            success = (null != this.database.updateColumn(TABLE_NAME, id, "title", updatedListing.getTitle()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "description", updatedListing.getDescription()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "initPrice", updatedListing.getInitPrice()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "minBid", updatedListing.getMinBid()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "auctionStartDate", updatedListing.getAuctionStartDate()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "auctionEndDate", updatedListing.getAuctionEndDate()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "sellerId", updatedListing.getSellerId()));

            return success;
        }
        return false;
    }

    @Override
    public ListingObject fetch(String id) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }

        if (this.database.rowExists(TABLE_NAME, id)) {
            return new ListingObject(
                    id,
                    this.database.fetchColumn(TABLE_NAME, id, "title"),
                    this.database.fetchColumn(TABLE_NAME, id, "description"),
                    this.database.fetchColumn(TABLE_NAME, id, "initPrice"),
                    this.database.fetchColumn(TABLE_NAME, id, "minBid"),
                    this.database.fetchColumn(TABLE_NAME, id, "auctionStartDate"),
                    this.database.fetchColumn(TABLE_NAME, id, "auctionEndDate"),
                    this.database.fetchColumn(TABLE_NAME, id, "category"),
                    this.database.fetchColumn(TABLE_NAME, id, "sellerId")
            );
        }
        return null;
    }

    @Override
    public List<ListingObject> fetchByName(String name) {
        return null;
    }

    @Override
    public List<ListingObject> fetchByLocation(String location) {
        return null;
    }

    @Override
    public List<ListingObject> fetchByCategory(String category) {
        return null;
    }

    @Override
    public List<ListingObject> fetchByStatus(String status) {
        return null;
    }

    @Override
    public List<ListingObject> fetchByFilters(String name, String location, String category, String status) {
        return null;
    }

    @Override
    public List<ListingObject> fetchAll() {
        List<Hashtable<String, String>> hashTableRows = this.database.getAllRows(TABLE_NAME);
        List<ListingObject> results = new ArrayList<>();

        if (null != hashTableRows) {
            for (int id = hashTableRows.size()-1; id >= 0; id--) {
                Hashtable<String, String> row = hashTableRows.get(id);
                String stringId = Integer.toString(id);

                ListingObject listing = new ListingObject(
                        stringId,
                        row.get("title"),
                        row.get("description"),
                        row.get("initPrice"),
                        row.get("minBid"),
                        row.get("auctionStartDate"),
                        row.get("auctionEndDate"),
                        row.get("category"),
                        row.get("sellerId")
                );

                results.add(listing);
            }
        }

        return results;
    }

    @Override
    public int getNumCategories() {
        return 0;
    }

    @Override
    public String[] getAllCategories() {
        return new String[0];
    }
}
