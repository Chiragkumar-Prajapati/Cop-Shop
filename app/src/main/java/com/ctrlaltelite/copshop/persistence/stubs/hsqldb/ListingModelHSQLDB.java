package com.ctrlaltelite.copshop.persistence.stubs.hsqldb;

import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.IListingModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                        "(title,description,initprice,minbid,auctionstartdate,auctionenddate,sellerid) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS);
            st.setString(1, newListing.getTitle());
            st.setString(2, newListing.getDescription());
            st.setString(3, newListing.getInitPrice());
            st.setString(4, newListing.getMinBid());
            st.setString(5, newListing.getAuctionStartDate());
            st.setString(6, newListing.getAuctionEndDate());
            st.setInt(7, Integer.parseInt(newListing.getSellerId()));
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
                        "sellerid = ? " +
                        "WHERE id = ?");
            st.setString(1, updatedListing.getTitle());
            st.setString(2, updatedListing.getDescription());
            st.setString(3, updatedListing.getInitPrice());
            st.setString(4, updatedListing.getMinBid());
            st.setString(5, updatedListing.getAuctionStartDate());
            st.setString(6, updatedListing.getAuctionEndDate());
            st.setInt(7, Integer.parseInt(updatedListing.getSellerId()));
            st.setInt(8, Integer.parseInt(id));
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

    private ListingObject fromResultSet(final ResultSet rs) throws SQLException {
        if (null == rs) { throw new IllegalArgumentException("resultSet cannot be null"); }

        String title = HSQLDBUtil.getStringFromResultSet(rs, "title");
        String desc = HSQLDBUtil.getStringFromResultSet(rs, "description");
        String initPrice = HSQLDBUtil.getStringFromResultSet(rs, "initprice");
        String minBid = HSQLDBUtil.getStringFromResultSet(rs, "minbid");
        String startDate = HSQLDBUtil.getStringFromResultSet(rs, "auctionstartdate");
        String endDate = HSQLDBUtil.getStringFromResultSet(rs, "auctionenddate");
        String sellerId = HSQLDBUtil.getIntAsStringFromResultSet(rs, "sellerid");
        String id = HSQLDBUtil.getIntAsStringFromResultSet(rs, "id");

        //System.out.println("Created Listing Object: " + id + ", " + title + ", " + desc + ", " + initPrice + ", " + minBid + ", " + startDate + ", " + endDate + ", " + sellerId);
        return new ListingObject(id, title, desc, initPrice, minBid, startDate, endDate, sellerId);
    }
}
