package com.ctrlaltelite.copshop.persistence.hsqldb;

import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class BidModelHSQLDB implements IBidModel {
    private static String TABLE_NAME = "BIDS";
    private Connection dbConn;

    public BidModelHSQLDB(final String dbPath) {
        this.dbConn = HSQLDBUtil.getConnection(dbPath);
    }

    @Override
    public void finalize() {
        HSQLDBUtil.closeConnection(dbConn);
    }

    @Override
    public String createNew(BidObject newBid) {
        if (null == newBid) { throw new IllegalArgumentException("newBid cannot be null"); }

        PreparedStatement st = null;
        ResultSet generatedKeys = null;

        try {
            st = dbConn.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " " +
                            "(buyerid,listingid,bidamount) " +
                            "VALUES (?, ?, ?)",
                    RETURN_GENERATED_KEYS);
            st.setInt(1, Integer.parseInt(newBid.getBuyerId()));
            st.setInt(2, Integer.parseInt(newBid.getListingId()));
            st.setString(3, newBid.getBidAmt());
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
    public BidObject fetch(String id) {
        if (null == id) { throw new IllegalArgumentException("id cannot be null"); }

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE id = ?");
            st.setInt(1, Integer.parseInt(id));
            rs = st.executeQuery();

            BidObject listingObject = null;
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
    public List<BidObject> findAllByListing(String listingId) {
        if (null == listingId) { throw new IllegalArgumentException("listingId cannot be null"); }

        List<BidObject> bidObjects = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE listingid=?");
            st.setString(1, listingId);
            rs = st.executeQuery();

            while (rs.next()) {
                BidObject bidObject = fromResultSet(rs);
                bidObjects.add(bidObject);
            }

        } catch (final SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            HSQLDBUtil.quietlyClose(rs);
            HSQLDBUtil.quietlyClose(st);
        }

        return bidObjects;
    }

    private BidObject fromResultSet(final ResultSet rs) throws SQLException {
        if (null == rs) { throw new IllegalArgumentException("resultSet cannot be null"); }

        String listingId = HSQLDBUtil.getIntAsStringFromResultSet(rs, "listingid");
        String buyerId = HSQLDBUtil.getIntAsStringFromResultSet(rs, "buyerid");
        String bidAmt = HSQLDBUtil.getStringFromResultSet(rs, "bidamount");
        String id = HSQLDBUtil.getIntAsStringFromResultSet(rs, "id");

        return new BidObject(id, listingId,buyerId,bidAmt);
    }
}
