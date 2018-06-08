package com.ctrlaltelite.copshop.persistence.models;

import com.ctrlaltelite.copshop.logic.interfaces.IListingModel;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.database.interfaces.IDatabase;

import java.util.ArrayList;
import java.util.Hashtable;

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
        newRow.put("location", newListing.getLocation());
        newRow.put("num_bids", newListing.getNumBids());
        newRow.put("price", newListing.getPrice());
        newRow.put("time_left", newListing.getTimeLeft());

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

            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "title", updatedListing.getTitle()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "description", updatedListing.getDescription()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "location", updatedListing.getLocation()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "num_bids", updatedListing.getNumBids()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "price", updatedListing.getPrice()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "time_left", updatedListing.getTimeLeft()));

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
                this.database.fetchColumn(TABLE_NAME, id, "location"),
                this.database.fetchColumn(TABLE_NAME, id, "price"),
                this.database.fetchColumn(TABLE_NAME, id, "num_bids"), // TODO: Count bids
                this.database.fetchColumn(TABLE_NAME, id, "time_left") // TODO: Calculate time left
            );
        }
        return null;
    }

    @Override
    public ArrayList<ListingObject> fetchAll() {
        ArrayList<Hashtable<String, String>> hashTableRows = this.database.getAllRows(TABLE_NAME);
        ArrayList<ListingObject> results = new ArrayList<>();

        if (null != hashTableRows) {
            for (int id = hashTableRows.size()-1; id >= 0; id--) {
                Hashtable<String, String> row = hashTableRows.get(id);
                String stringId = Integer.toString(id);

                ListingObject listing = new ListingObject(
                        stringId,
                        row.get("title"),
                        row.get("description"),
                        row.get("location"),
                        row.get("price"),
                        row.get("num_bids"),
                        row.get("time_left")
                );

                results.add(listing);
            }
        }

        return results;
    }
}
