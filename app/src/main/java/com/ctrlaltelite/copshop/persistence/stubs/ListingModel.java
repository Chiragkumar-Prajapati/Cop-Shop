package com.ctrlaltelite.copshop.persistence.stubs;

import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.logic.services.utilities.DateUtility;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

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
        newRow.put("category", newListing.getCategory());
		newRow.put("imageData", newListing.getImageData());
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
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "category", updatedListing.getCategory()));
            success = success && (null != this.database.updateColumn(TABLE_NAME, id, "sellerId", updatedListing.getImageData()));
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
                    this.database.fetchColumn(TABLE_NAME, id, "imageData"),
                    this.database.fetchColumn(TABLE_NAME, id, "sellerId")
            );
        }
        return null;
    }

    @Override
    public List<ListingObject> fetchByName(String name) {
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
                        row.get("imagedata"),
                        row.get("sellerId")
                );
                if (!name.isEmpty()) {
                    if (listing.getTitle().contains(name) || (listing.getDescription()).contains(name)){
                        results.add(listing);
                    }
                }
                else {
                    results.add(listing);
                }
            }
        }

        return results;
    }

    @Override
    public List<ListingObject> fetchBySellerID(String sellerID) {
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
                        row.get("imagedata"),
                        row.get("sellerId")
                );
                if (!sellerID.isEmpty()) {
                    if ((listing.getSellerId()).compareToIgnoreCase(sellerID) == 0) {
                        results.add(listing);
                    }
                }
                else {
                    results.add(listing);
                }
            }
        }

        return results;
    }

    @Override
    public List<ListingObject> fetchByCategory(String category) {
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
                        row.get("imagedata"),
                        row.get("sellerId")
                );
                if (!category.isEmpty()) {
                    if ((listing.getCategory()).compareToIgnoreCase(category) == 0) {
                        results.add(listing);
                    }
                }
                else {
                    results.add(listing);
                }
            }
        }

        return results;
    }

    @Override
    public List<ListingObject> fetchByStatus(String status) {
        List<Hashtable<String, String>> hashTableRows = this.database.getAllRows(TABLE_NAME);
        List<ListingObject> results = new ArrayList<>();

        Calendar startCal;
        Calendar endCal;

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
                        row.get("imagedata"),
                        row.get("sellerId")
                );
                if (!status.isEmpty()) {
                    if (status.compareToIgnoreCase("Active") == 0) {
                        startCal = DateUtility.convertToDateObj(listing.getAuctionStartDate());
                        endCal = DateUtility.convertToDateObj(listing.getAuctionEndDate());

                        if (startCal.before(Calendar.getInstance(Locale.CANADA)) && endCal.after(Calendar.getInstance(Locale.CANADA))) {
                            results.add(listing);
                        }
                    }
                    else if (status.compareToIgnoreCase("Inactive") == 0) {
                        startCal = DateUtility.convertToDateObj(listing.getAuctionStartDate());
                        endCal = DateUtility.convertToDateObj(listing.getAuctionEndDate());

                        if (startCal.after(Calendar.getInstance(Locale.CANADA)) || endCal.before(Calendar.getInstance(Locale.CANADA))) {
                            results.add(listing);
                        }
                    }
                }
                else {
                    results.add(listing);
                }
            }
        }

        return results;
    }

    @Override
    public List<ListingObject> fetchByFilters(String name, String sellerID, String category, String status) {

        // ensure name, sellerID, category and status are not null
        if (null == name) { throw new IllegalArgumentException("name cannot be null"); }

        if (null == sellerID) { throw new IllegalArgumentException("sellerID cannot be null"); }

        if (null == category) { throw new IllegalArgumentException("category cannot be null"); }

        if (null == status) { throw new IllegalArgumentException("status cannot be null"); }

        // Status can be "Active", "Inactive" or empty string, in which case all listings are returned
        if (status.compareToIgnoreCase("Active") != 0 && status.compareToIgnoreCase("Inactive") != 0 && !status.isEmpty()) {
            throw new IllegalArgumentException("invalid status");
        }

        // Results is the list we will return, contains listings that match all filters
        List<ListingObject> results = new ArrayList<>();

        // Get lists that contain listings that match each individual filter
        List<ListingObject> resultsName = fetchByName(name);
        List<ListingObject> resultsLocation = fetchBySellerID(sellerID);
        List<ListingObject> resultsCategory = fetchByCategory(category);
        List<ListingObject> resultsStatus = fetchByStatus(status);

        /* Add listings with name, location, category and status provided as a parameter.
           Horrible, 4 nested for-loops, has to be a better way, just haven't found it yet.
           On Average case isn't that bad, for each posting in one of the lists (resultsName),
           checks if it matches the other lists and so on for the other two.
           Worst case is when the listing exists in all the lists, i.e. matches all filters.
           Possible fix would be to overwrite List's contains method, which to get better results
           would need something better than iterative searching. This would mean custom lists so
           more issues associated with that? */
        for (ListingObject listingName : resultsName) {
            if ( !results.contains(listingName) ) {
                for (ListingObject listingLocation : resultsLocation) {
                    if ((listingName.getId().compareToIgnoreCase(listingLocation.getId())) == 0) {
                        for (ListingObject listingCategory : resultsCategory) {
                            if ((listingName.getId().compareToIgnoreCase(listingCategory.getId())) == 0) {
                                for (ListingObject listingStatus : resultsStatus) {
                                    if ((listingName.getId().compareToIgnoreCase(listingStatus.getId())) == 0) {
                                        results.add(listingName);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return results;
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
                        row.get("imageData"),
                        row.get("sellerId")
                );

                results.add(listing);
            }
        }

        return results;
    }

    @Override
    public int getNumCategories() {
        List<Hashtable<String, String>> allRows = this.database.getAllRows(TABLE_NAME);
        List<String> categories = new ArrayList<String>();

        for (int i = 0; i < allRows.size(); i++) {
            if (!categories.contains((allRows.get(i).get("category")))) {
                categories.add((allRows.get(i).get("category")));
            }
        }

        return  categories.size();
    }

    @Override
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<String>();

        // get all seller table rows
        List<Hashtable<String, String>> allRows = this.database.getAllRows(TABLE_NAME);

        categories.add(0, "");

        int totalCategories = 1;
        boolean categoryAdded = false;

        // populate array with the categories
        for (int i = 0; i < allRows.size(); i++) {
            categoryAdded = false;
            for (int j = 0; !categoryAdded && j < categories.size(); j++) {
                if (categories.get(j).equalsIgnoreCase(allRows.get(i).get("category"))) {
                    categoryAdded = true;
                }
            }
            if (!categoryAdded) {
                categories.add(totalCategories++, (allRows.get(i)).get("category"));
            }
        }

        return categories;
    }
}
