package com.ctrlaltelite.copshop.persistence.stubs.hsqldb;

import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.logic.services.utilities.DateUtility;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.IListingModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class ListingModelHSQLDB implements IListingModel {
    private static String TABLE_NAME = "LISTINGS";
    private Connection dbConn;

    public ListingModelHSQLDB(final String dbPath) {
        this.dbConn = HSQLDBUtil.getConnection(dbPath);
    }

    @Override
    public void finalize() {
        HSQLDBUtil.closeConnection(dbConn);
    }

    @Override
    public String createNew(ListingObject newListing) {
        if (null == newListing) { throw new IllegalArgumentException("newListing cannot be null"); }

        PreparedStatement st = null;
        ResultSet generatedKeys = null;

        try {
            st = dbConn.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " " +
                        "(title,description,initprice,minbid,auctionstartdate,auctionenddate,category,sellerid) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS);
            st.setString(1, newListing.getTitle());
            st.setString(2, newListing.getDescription());
            st.setString(3, newListing.getInitPrice());
            st.setString(4, newListing.getMinBid());
            st.setString(5, newListing.getAuctionStartDate());
            st.setString(6, newListing.getAuctionEndDate());
            st.setString(7, newListing.getCategory());
            st.setInt(8, Integer.parseInt(newListing.getSellerId()));
            int updated = st.executeUpdate();

            if (updated >= 1) {
                generatedKeys = st.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    return String.valueOf(newId);
                }
                generatedKeys.close();
            }
            return null;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(generatedKeys);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public boolean delete(String id) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }

        PreparedStatement st = null;

        try {
            st = dbConn.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE id = ?");
            st.setInt(1, Integer.parseInt(id));
            int affected = st.executeUpdate();

            return affected > 0;
        } catch (final SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public boolean update(String id, ListingObject updatedListing) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }
        if (null == updatedListing) { throw new IllegalArgumentException("updatedListing cannot be null"); }

        PreparedStatement st = null;

        try {
            st = dbConn.prepareStatement(
                    "UPDATE " + TABLE_NAME + " SET " +
                        "title = ?, " +
                        "description = ?, " +
                        "initprice = ?, " +
                        "minbid = ?, " +
                        "auctionstartdate = ?, " +
                        "auctionenddate = ?, " +
                        "category = ?, " +
                        "sellerid = ? " +
                        "WHERE id = ?");
            st.setString(1, updatedListing.getTitle());
            st.setString(2, updatedListing.getDescription());
            st.setString(3, updatedListing.getInitPrice());
            st.setString(4, updatedListing.getMinBid());
            st.setString(5, updatedListing.getAuctionStartDate());
            st.setString(6, updatedListing.getAuctionEndDate());
            st.setString(7, updatedListing.getCategory());
            st.setInt(8, Integer.parseInt(updatedListing.getSellerId()));
            st.setInt(9, Integer.parseInt(id));
            st.executeUpdate();
            return true;

        } catch (final SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public ListingObject fetch(String id) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE id = ?");
            st.setInt(1, Integer.parseInt(id));
            rs = st.executeQuery();

            ListingObject listingObject = null;
            if (rs.next()) {
                listingObject = fromResultSet(rs);
            }
            return listingObject;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public List<ListingObject> fetchByName(String name) {
        if (null == name) { throw new IllegalArgumentException("name cannot be null"); }

        List<ListingObject> results = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            if(!name.isEmpty()) {
                st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME);
                rs = st.executeQuery();

                while (rs.next()) {
                    ListingObject listingObject = fromResultSet(rs);
                    if(listingObject.getTitle().contains(name) || listingObject.getDescription().contains(name)) {
                        results.add(listingObject);
                    }
                }
            }
            else {
                // select all
                st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME);
                rs = st.executeQuery();

                while (rs.next()) {
                    ListingObject listingObject = fromResultSet(rs);
                    results.add(listingObject);
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }

        return results;
    }

    @Override
    public List<ListingObject> fetchBySellerID(String sellerID) {

        if (null == sellerID) { throw new IllegalArgumentException("sellerID cannot be null"); }

        List<ListingObject> results = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            if(!sellerID.isEmpty()) {
                st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE sellerid = ?");
                st.setInt(1, Integer.parseInt(sellerID));
                rs = st.executeQuery();

                while (rs.next()) {
                    ListingObject listingObject = fromResultSet(rs);
                    results.add(listingObject);
                }
            }
            else {
                // select all
                st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME);
                rs = st.executeQuery();

                while (rs.next()) {
                    ListingObject listingObject = fromResultSet(rs);
                    results.add(listingObject);
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }

        return results;
    }

    @Override
    public List<ListingObject> fetchByCategory(String category) {
        if (null == category) { throw new IllegalArgumentException("category cannot be null"); }

        List<ListingObject> results = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            if(!category.isEmpty()) {
                st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE category = ?");
                st.setString(1, category);
                rs = st.executeQuery();

                while (rs.next()) {
                    ListingObject listingObject = fromResultSet(rs);
                    results.add(listingObject);
                }
            }
            else {
                // select all
                st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME);
                rs = st.executeQuery();

                while (rs.next()) {
                    ListingObject listingObject = fromResultSet(rs);
                    results.add(listingObject);
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }

        return results;
    }

    @Override
    public List<ListingObject> fetchByStatus(String status) {
        if (null == status) { throw new IllegalArgumentException("status cannot be null"); }

        if (status.compareToIgnoreCase("Active") != 0 && status.compareToIgnoreCase("Inactive") != 0 && !status.isEmpty()) {
            throw new IllegalArgumentException("invalid status");
        }

        List<ListingObject> results = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            if(!status.isEmpty()) {
                // get all listings
                st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME);
                rs = st.executeQuery();

                Calendar startCal;
                Calendar endCal;

                if (status.compareToIgnoreCase("Active") == 0) {
                    // figure out which listings are active and add to results
                    while (rs.next()) {
                        ListingObject listingObject = fromResultSet(rs);
                        startCal = DateUtility.convertToDateObj(listingObject.getAuctionStartDate());
                        endCal = DateUtility.convertToDateObj(listingObject.getAuctionEndDate());

                        if (startCal.before(Calendar.getInstance(Locale.CANADA)) && endCal.after(Calendar.getInstance(Locale.CANADA))) {
                            results.add(listingObject);
                        }
                    }
                } else {
                    // figure out which listings are inactive and add to results
                    while (rs.next()) {
                        ListingObject listingObject = fromResultSet(rs);
                        startCal = DateUtility.convertToDateObj(listingObject.getAuctionStartDate());
                        endCal = DateUtility.convertToDateObj(listingObject.getAuctionEndDate());

                        if (startCal.after(Calendar.getInstance(Locale.CANADA)) || endCal.before(Calendar.getInstance(Locale.CANADA))) {
                            results.add(listingObject);
                        }
                    }
                }
            }
            else {
                // select all
                st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME);
                rs = st.executeQuery();

                while (rs.next()) {
                    ListingObject listingObject = fromResultSet(rs);
                    results.add(listingObject);
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }

        return results;
    }

    @Override
    public List<ListingObject> fetchByFilters(String name, String sellerID, String category, String status) {

        // Ensure name, sellerID, category and status are not null
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

        // get lists that contain listings that match each individual filter
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
        List<ListingObject> results = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME);
            rs = st.executeQuery();

            while (rs.next()) {
                ListingObject listingObject = fromResultSet(rs);
                results.add(listingObject);
            }

        } catch (final SQLException e) {
            e.printStackTrace();

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }

        return results;
    }

    @Override
    public int getNumCategories() {
        int numCategories = 0;

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT COUNT (DISTINCT category) AS NumCategories FROM " + TABLE_NAME);
            rs = st.executeQuery();

            if (rs.next()) {
                numCategories = Integer.parseInt(HSQLDBUtil.getStringFromResultSet(rs, "NumCategories"));
            }
            return numCategories;

        } catch (final SQLException e) {
            e.printStackTrace();
            return -1;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    @Override
    public String[] getAllCategories() {
        String[] categories = new String[getNumCategories()+1];

        PreparedStatement st = null;
        ResultSet rs = null;

        // get all seller table rows
        try {
            st = dbConn.prepareStatement("SELECT DISTINCT category FROM " + TABLE_NAME);
            rs = st.executeQuery();

            categories[0] = "";

            int i = 1;
            while (rs.next()) {
                // populate array with the locations
                categories[i] = HSQLDBUtil.getStringFromResultSet(rs, "category");
                i++;
            }

            return categories;

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }
    }

    private ListingObject fromResultSet(final ResultSet rs) throws SQLException {
        if (null == rs) { throw new IllegalArgumentException("resultSet cannot be null"); }

        String title = HSQLDBUtil.getStringFromResultSet(rs, "title");
        String desc = HSQLDBUtil.getStringFromResultSet(rs, "description");
        String initPrice = HSQLDBUtil.getStringFromResultSet(rs, "initprice");
        String minBid = HSQLDBUtil.getStringFromResultSet(rs, "minbid");
        String startDate = HSQLDBUtil.getStringFromResultSet(rs, "auctionstartdate");
        String endDate = HSQLDBUtil.getStringFromResultSet(rs, "auctionenddate");
        String category = HSQLDBUtil.getStringFromResultSet(rs, "category");
        String sellerId = HSQLDBUtil.getIntAsStringFromResultSet(rs, "sellerid");
        String id = HSQLDBUtil.getIntAsStringFromResultSet(rs, "id");

        //System.out.println("Created Listing Object: " + id + ", " + title + ", " + desc + ", " + initPrice + ", " + minBid + ", " + startDate + ", " + endDate + ", " + sellerId);
        return new ListingObject(id, title, desc, initPrice, minBid, startDate, endDate, category, sellerId);
    }
}
